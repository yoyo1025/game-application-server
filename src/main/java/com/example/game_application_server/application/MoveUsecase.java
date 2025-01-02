package com.example.game_application_server.application;

import com.example.game_application_server.domain.entity.Demon;
import com.example.game_application_server.domain.entity.Player;
import com.example.game_application_server.domain.entity.Position;
import com.example.game_application_server.domain.entity.Villager;
import com.example.game_application_server.domain.service.GameState;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class MoveUsecase {
    private final GameStateManager gameStateManager;

    public MoveUsecase(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
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

        // 次のプレイヤーのターンに移行
        gameState.turn.nextPlayerIndex();

        // ---- ここからが「死んだプレイヤーのターンをスキップ」する処理 ----
        Player nextPlayer = gameState.players.get(gameState.turn.getCurrentPlayerIndex() - 1);

        // 全員が死んでいるケースなどを避けるため、最大人数分だけループする
        int skipCount = 0;
        int maxPlayers = gameState.players.size();

        // 全員が死んでいると無限ループになる可能性があるため、回数制限を設ける
        while (skipCount < maxPlayers) {
            // 次のプレイヤーが村人 かつ その村人が死んでいる場合はそのプレイヤーのターンをスキップ
            if (nextPlayer instanceof Villager && !((Villager) nextPlayer).isAlive()) {
                gameState.turn.nextPlayerIndex();
                nextPlayer = gameState.players.get(gameState.turn.getCurrentPlayerIndex() - 1);
                skipCount++;
            } else {
                // 生きている村人 or 鬼ならターンを確定
                break;
            }
        }
        return gameState;
    }

}


