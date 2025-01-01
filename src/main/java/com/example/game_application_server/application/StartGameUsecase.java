package com.example.game_application_server.application;

import com.example.game_application_server.domain.entity.Demon;
import com.example.game_application_server.domain.entity.Villager;
import com.example.game_application_server.domain.service.GameState;
import com.example.game_application_server.dto.PlayerInfo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StartGameUsecase {

    public GameStateManager gameStateManager;

    public StartGameUsecase(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
    }
    public GameState excute(List<PlayerInfo> players) {
        List<PlayerInfo> playerList = new ArrayList<>();

        // 村人をリストに追加
        for (PlayerInfo player : players) {
            if (!player.isDemon()) {
                playerList.add(player);
            }
        }
        // 鬼をリストの最後に追加
        for (PlayerInfo player : players) {
            if (player.isDemon()) {
                playerList.add(player);
            }
        }

        GameState newGameState = new GameState(
                playerList.get(0).getUserId(),
                playerList.get(1).getUserId(),
                playerList.get(2).getUserId(),
                playerList.get(3).getUserId(),
                playerList.get(0).getUserName(),
                playerList.get(1).getUserName(),
                playerList.get(2).getUserName(),
                playerList.get(3).getUserName()
        );

        // Managerに登録
        gameStateManager.setGameState(newGameState);

        return newGameState;
    }
}
