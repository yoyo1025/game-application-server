package com.example.game_application_server.application;

import com.example.game_application_server.domain.entity.Demon;
import com.example.game_application_server.domain.entity.Villager;
import com.example.game_application_server.domain.service.GameState;
import com.example.game_application_server.dto.PlayerInfo;

import java.util.ArrayList;
import java.util.List;

public class StartGameUsecase {
    public void excute(List<PlayerInfo> players) {
        List<String> playerNameList = new ArrayList<>();

        // 村人をリストに追加
        for (PlayerInfo player : players) {
            if (!player.isDemon()) {
                playerNameList.add(player.getUserName());
            }
        }
        // 鬼をリストの最後に追加
        for (PlayerInfo player : players) {
            if (player.isDemon()) {
                playerNameList.add(player.getUserName());
            }
        }
        // デバッグ用出力
        System.out.println(playerNameList);
    }

}
