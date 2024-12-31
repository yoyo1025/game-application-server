package com.example.game_application_server.application;

import com.example.game_application_server.domain.entity.Dice;
import com.example.game_application_server.domain.entity.Player;
import com.example.game_application_server.domain.service.GameState;
import org.springframework.stereotype.Service;

@Service
public class DiceUsecase {
    public GameStateManager gameStateManager;

    public DiceUsecase(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
    }

    public int excute(int userId) {
        GameState gameState = gameStateManager.getGameState();

        Player currentPlayer = gameState.players.get(gameState.turn.getCurrentPlayerIndex() - 1);
        if (currentPlayer.getUserId() != userId) {
            throw new IllegalStateException("あなたのターンではありません");
        }

        Dice dice = new Dice(4);
        int diceRoll = dice.roll();

        return diceRoll;
    }
}
