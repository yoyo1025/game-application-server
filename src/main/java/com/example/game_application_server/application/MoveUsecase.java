package com.example.game_application_server.application;

import com.example.game_application_server.domain.entity.Player;
import com.example.game_application_server.domain.entity.Position;
import com.example.game_application_server.domain.service.GameState;
import org.springframework.stereotype.Service;

@Service
public class MoveUsecase {
    private final GameStateManager gameStateManager;

    public MoveUsecase(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
    }

    public GameState excute(Position targetPosition, int userId) {
        GameState gameState = gameStateManager.getGameState();

        Player currentPlayer = gameState.players.get(gameState.turn.getCurrentPlayerIndex() - 1);
        if (currentPlayer.getUserId() != userId) {
            throw new IllegalStateException("あなたのターンではありません");
        }

        // プレイヤーの位置を更新
        gameState.setPlayerPosition(currentPlayer, targetPosition);

        gameState.turn.nextPlayerIndex();

        return gameState;
    }
}


