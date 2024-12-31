package com.example.game_application_server.presentation;

import com.example.game_application_server.application.StartGameUsecase;
import com.example.game_application_server.domain.service.GameState;
import com.example.game_application_server.dto.GameStateDTO;
import com.example.game_application_server.dto.PlayerInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class GameController {

    public final SimpMessagingTemplate messagingTemplate;

    // 接続中のユーザーIDを保存(sessionId -> userId)
    public static final ConcurrentHashMap<String, String> connectedUsers = new ConcurrentHashMap<>();

    // 接続上限を定義
    public static final int MAX_CONNECTIONS = 4;

    public List<PlayerInfo> playersInfo;

    GameState gameState;

    public GameController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/init-player-info")
    public ResponseEntity<List<PlayerInfo>> initPlayerInfo(@RequestBody List<PlayerInfo> playersInfo) {
        this.playersInfo = playersInfo;
        // 受け取ったデータをログに出力
        playersInfo.forEach(player -> System.out.println(
                "userId: " + player.getUserId() + ", userName: " + player.getUserName() + ", isDemon: " + player.isDemon()
        ));

        // すべてのプレイヤー情報をレスポンスとして返す
        return ResponseEntity.ok(playersInfo);
    }

    @GetMapping("/start-game")
    public GameStateDTO startGame() {
        StartGameUsecase startGameUsecase = new StartGameUsecase();
        gameState = startGameUsecase.excute(playersInfo);

        return gameState.toDTO();
    }

    @GetMapping("/end-game")
    public GameStateDTO endGame() {
        gameState.turn.nextTurn();
        return gameState.toDTO();
    }

    // 接続中かチェック
    public static boolean checkDuplicatedUser(String userId) {
        return connectedUsers.containsValue(userId);
    }

    // ユーザーを追加する際に、上限に達していたら false を返す
    public static boolean addUser(String sessionId, String userId) {
        if (connectedUsers.size() >= MAX_CONNECTIONS) {
            return false;
        }
        connectedUsers.put(sessionId, userId);
        return true;
    }

    public static void removeUser(String sessionId) {
        connectedUsers.remove(sessionId);
    }
}
