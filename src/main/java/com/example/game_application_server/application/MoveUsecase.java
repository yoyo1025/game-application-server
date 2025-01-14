package com.example.game_application_server.application;

import com.example.game_application_server.application.GameStateManager;
import com.example.game_application_server.domain.entity.*;
import com.example.game_application_server.domain.service.GameState;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class MoveUsecase {
    public GameStateManager gameStateManager;
    public final EndGameUsecase endGameUsecase;

    public MoveUsecase(GameStateManager gameStateManager, EndGameUsecase endGameUsecase) {
        this.gameStateManager = gameStateManager;
        this.endGameUsecase = endGameUsecase;
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
        System.out.println("=== 15ターン目が終了。鬼が行動を終えました。ゲーム終了 ===");
        // ここでResultを生成
        Result result = new Result();
        result.addDemon(demon);

        // 全プレイヤーを確認し、Villagerなら追加
        for (Player p : gameState.players) {
            if (p instanceof Villager vill) {
                result.addVillager(vill);
            }
        }

        // ここでは、デモとして鬼の勝利とする (実際の勝敗ロジックに合わせて変更)
        result.setDemonVictory(true); // 例: 鬼が勝ったとする

        // EndGameUsecaseを呼び出してDBに保存
        endGameUsecase.execute(result);

        // 必要ならGameStateに「終了フラグ」を設定
        gameState.isGameFinished = true;
    }
}


