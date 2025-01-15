package com.example.game_application_server.application;

import com.example.game_application_server.application.GameStateManager;
import com.example.game_application_server.domain.entity.*;
import com.example.game_application_server.domain.service.GameState;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MoveUsecase {
    public GameStateManager gameStateManager;
    public final EndGameUsecase endGameUsecase;

    private final SimpMessagingTemplate messagingTemplate;

    public MoveUsecase(GameStateManager gameStateManager, EndGameUsecase endGameUsecase, SimpMessagingTemplate messagingTemplate) {
        this.gameStateManager = gameStateManager;
        this.endGameUsecase = endGameUsecase;
        this.messagingTemplate = messagingTemplate;
    }

    public GameState excute(Position targetPosition, int userId) {
        GameState gameState = gameStateManager.getGameState();

        Player currentPlayer = gameState.players.get(gameState.turn.getCurrentPlayerIndex() - 1);

        // ターンチェック
        if (currentPlayer.getUserId() != userId) {
            throw new IllegalStateException("あなたのターンではありません");
        }

        // 位置が他のプレイヤーと重複しているかを確認
        Optional<Map.Entry<Player, Position>> capturedEntry = gameState.playerPositions.entrySet().stream()
                .filter(entry -> entry.getValue().equals(targetPosition))
                .findFirst();

        capturedEntry.ifPresent(entry -> {
            Player capturedPlayer = entry.getKey();
            System.out.println("Captured player: " + capturedPlayer.getName());

            Villager villager = (Villager) capturedPlayer;
            Demon demon = (Demon) currentPlayer;
            demon.capture(villager);
            System.out.println("Villager captured by Demon: " + villager.getName());
        });

        // プレイヤーの位置を更新
        gameState.setPlayerPosition(currentPlayer, targetPosition);

        if (!gameState.field.eventPositions.contains(targetPosition)) {
            // ★ ここで「ターンが15 & currentPlayerが鬼(Demon)」かを判定
            if (gameState.turn.getCurrentTurn() == 5 && currentPlayer instanceof Demon) {
                // ゲーム終了処理へ
                endGameProcedure(gameState, (Demon) currentPlayer);
            }

            // 次のプレイヤーのターンに移行
            gameState.turn.nextPlayerIndex();
        }


        // 全員が死んでいるケースなどを避けるため、最大人数分だけループする
        int skipCount = 0;
        int maxPlayers = gameState.players.size();

        // 全員が死んでいると無限ループになる可能性があるため、回数制限を設ける
        while (skipCount < maxPlayers) {
            // ---- ここからが「死んだプレイヤーのターンをスキップ」する処理 ----
            Player nextPlayer = gameState.players.get(gameState.turn.getCurrentPlayerIndex() - 1);
            // 次のプレイヤーが村人 かつ その村人が死んでいる場合はそのプレイヤーのターンをスキップ
            if (nextPlayer instanceof Villager && !((Villager) nextPlayer).isAlive()) {
                gameState.turn.nextPlayerIndex();
                skipCount++;
            } else if (nextPlayer.isOnBreak) {
                System.out.println(nextPlayer.getName() + " is currently on break, skipping...");
                nextPlayer.setOnBreak(false);  // 次のターンではプレイできるようにフラグをリセット
                gameState.turn.nextPlayerIndex();
                skipCount++;
            } else {
                // 生きている村人 or 鬼ならターンを確定
                break;
            }
        }
        return gameState;
    }

    /**
     * ★ 15ターン目に鬼が行動を終えた際のゲーム終了処理
     */
    private void endGameProcedure(GameState gameState, Demon demon) {
        System.out.println("=== 15ターン目が終了(デモ:5ターン)。鬼が行動を終えました。ゲーム終了 ===");

        // 結果オブジェクト
        Result result = new Result();
        result.addDemon(demon);

        boolean hasAliveVillager = false;
        for (Player p : gameState.players) {
            if (p instanceof Villager vill) {
                result.addVillager(vill);
                if (vill.isAlive) {
                    hasAliveVillager = true;
                }
            }
        }

        // 勝敗判定
        if (hasAliveVillager) {
            result.setDemonVictory(false);
            System.out.println("村人が勝利しました！生存者がいます。");
        } else {
            result.setDemonVictory(true);
            System.out.println("鬼が勝利しました！全員が捕まりました。");
        }

        // DB保存 → リストを受け取り
        List<BattleRecord> battleRecords = endGameUsecase.execute(result);

        // ★ Spring による自動シリアライズで送信
        messagingTemplate.convertAndSend("/topic/end-game", battleRecords);

        gameState.isGameFinished = true;
    }
}


