package com.example.game_application_server.application;

import com.example.game_application_server.domain.entity.Player;
import com.example.game_application_server.domain.entity.Villager;
import com.example.game_application_server.domain.service.GameState;
import org.springframework.stereotype.Service;

@Service
public class GetPointUsecase {
    public GameStateManager gameStateManager;

    public GetPointUsecase(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
    }

    public GameState excute(int userId) {
        GameState gameState = gameStateManager.getGameState();

        Player currentPlayer = gameState.players.get(gameState.turn.getCurrentPlayerIndex() - 1);

        if (currentPlayer.getUserId() != userId) {
            throw new IllegalStateException("あなたのターンではありません");
        }

        if (currentPlayer instanceof Villager) {
            ((Villager) currentPlayer).addPoints();
        }
        return gameState;
    }
}
