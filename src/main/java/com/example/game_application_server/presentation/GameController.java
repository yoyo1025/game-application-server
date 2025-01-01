package com.example.game_application_server.presentation;

import com.example.game_application_server.application.DiceUsecase;
import com.example.game_application_server.application.StartGameUsecase;
import com.example.game_application_server.domain.entity.Dice;
import com.example.game_application_server.domain.entity.Player;
import com.example.game_application_server.domain.service.GameState;
import com.example.game_application_server.dto.GameStateDTO;
import com.example.game_application_server.dto.PlayerInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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

    public StartGameUsecase startGameUsecase;
    public DiceUsecase diceUsecase;


    public GameController(
            SimpMessagingTemplate messagingTemplate,
            StartGameUsecase startGameUsecase,
            DiceUsecase diceUsecase
    ) {
        this.messagingTemplate = messagingTemplate;
        this.startGameUsecase = startGameUsecase;
        this.diceUsecase = diceUsecase;
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
    public ResponseEntity<?> startGame() {
        try {
            GameState gameState = startGameUsecase.excute(playersInfo);

            // WebSocketを介してすべてのプレイヤーに通知
            messagingTemplate.convertAndSend("/topic/game-state", gameState.toDTO());

            return ResponseEntity.ok(gameState.toDTO());
        } catch (IllegalStateException e) {
            // すでにゲームがある状態で呼ばれたなど
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        }
    }
    @GetMapping("/game-state")
    public ResponseEntity<?> getGameState() {
        try {
            GameState gameState = startGameUsecase.gameStateManager.getGameState();
            return ResponseEntity.ok(gameState.toDTO());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No game is currently in progress."));
        }
    }
    @PostMapping("/dice")
    public ResponseEntity<?> dice(@RequestBody Map<String, Integer> requestBody) {
        int userId = requestBody.get("userId");
        try {
            int diceRoll = diceUsecase.excute(userId);

            // 必要であればWebSocketでブロードキャストする
            messagingTemplate.convertAndSend("/topic/dice", Map.of("userId", userId, "diceRoll", diceRoll));

            return ResponseEntity.ok(Map.of("diceRoll", diceRoll));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        }
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
