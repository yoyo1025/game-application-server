package com.example.game_application_server.application;

import com.example.game_application_server.domain.service.GameState;
import org.springframework.stereotype.Component;

@Component
public class GameStateManager {
    public GameState gameState;

    public void setGameState(GameState gameState) {
        if (this.gameState != null) {
            throw new IllegalStateException("A game is already in progress");
        }
        this.gameState = gameState;
    }

    public GameState getGameState() {
        if (this.gameState == null) {
            throw new IllegalStateException("No game is in progress");
        }
        return this.gameState;
    }

    // ゲーム終了などでGameStateを破棄
    public void endGame() {
        this.gameState = null;
    }

    // ゲームが進行中かどうかを返す
    public boolean isGameInProgress() {
        return this.gameState != null;
    }
}
