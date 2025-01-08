package com.example.game_application_server.application;

import com.example.game_application_server.domain.entity.Player;
import com.example.game_application_server.domain.service.GameState;
import org.springframework.stereotype.Service;


@Service
public class SkipTurnUsecase {
    public GameStateManager gameStateManager;

    public SkipTurnUsecase(GameStateManager gameStateManager){
        this.gameStateManager=gameStateManager;
    }

    public GameState excute(int targetId){
        GameState gameState = gameStateManager.getGameState();

        Player targetPlayer = gameState.players.get(0);
        for (int i = 0; i < 4; i++){
            if ( targetId == gameState.players.get(i).userId) {
                targetPlayer = gameState.players.get(i);
            }
        }

        //重複チェック
        if (targetPlayer.isOnBreak) {
            throw new IllegalStateException("既に一回休み状態です。");
        }
            System.out.println("target player: "+targetPlayer.getName());
            targetPlayer.setOnBreak(true);
            System.out.println(targetPlayer.getName()+"'s next turn is skipped");

        return gameState;
    }
}
