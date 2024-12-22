package com.example.game_application_server.presentation;

import com.example.game_application_server.dto.Greeting;
import com.example.game_application_server.dto.HelloMessage;
import com.example.game_application_server.dto.PlayerInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api")
public class GameController {

    public final SimpMessagingTemplate messagingTemplate;

    // 接続中のユーザーIDを保存(sessionId -> userId)
    public static final ConcurrentHashMap<String, String> connectedUsers = new ConcurrentHashMap<>();

    public GameController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/init-player-info")
    public ResponseEntity<List<PlayerInfo>> initPlayerInfo(@RequestBody List<PlayerInfo> playersInfo) {
        // 受け取ったデータをログに出力
        playersInfo.forEach(player -> System.out.println(
                "userId: " + player.getUserId() + ", userName: " + player.getUserName() + ", isDemon: " + player.isDemon()
        ));

        // すべてのプレイヤー情報をレスポンスとして返す
        return ResponseEntity.ok(playersInfo);
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        return new Greeting("Hello, " + message.getName() + "! Message: " + message.getMessage());
    }

    public static void addUser(String sessionId, String userId) {
        connectedUsers.put(sessionId, userId);
    }

    public static void removeUser(String sessionId) {
        connectedUsers.remove(sessionId);
    }
}
