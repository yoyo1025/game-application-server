package com.example.game_application_server.presentation;

import com.example.game_application_server.dto.PlayerInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GameController {

    @PostMapping("/init-player-info")
    public ResponseEntity<List<PlayerInfo>> initPlayerInfo(@RequestBody List<PlayerInfo> playersInfo) {
        // 受け取ったデータをログに出力
        playersInfo.forEach(player -> System.out.println(
                "userId: " + player.getUserId() + ", userName: " + player.getUserName() + ", isDemon: " + player.isDemon()
        ));

        // すべてのプレイヤー情報をレスポンスとして返す
        return ResponseEntity.ok(playersInfo);
    }
}
