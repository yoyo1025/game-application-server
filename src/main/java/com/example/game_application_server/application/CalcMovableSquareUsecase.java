package com.example.game_application_server.application;

import com.example.game_application_server.domain.entity.Player;
import com.example.game_application_server.domain.entity.Position;
import com.example.game_application_server.domain.service.GameState;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CalcMovableSquareUsecase {
    public GameStateManager gameStateManager;

    public CalcMovableSquareUsecase(GameStateManager gameStateManager){
        this.gameStateManager = gameStateManager;
    }

    public List<Position> excute(int steps) {
        GameState gameState = gameStateManager.getGameState();
        Player currentPlayer = gameState.players.get(gameState.turn.getCurrentPlayerIndex() - 1);
        return gameState.calculatePossibleMoves(
                currentPlayer,
                gameState.playerPositions ,
                steps,
                gameState.field.getSize()
        );
    }

}
