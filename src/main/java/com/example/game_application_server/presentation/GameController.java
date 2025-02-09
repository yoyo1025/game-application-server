package com.example.game_application_server.presentation;

import com.example.game_application_server.application.*;
import com.example.game_application_server.domain.entity.Dice;
import com.example.game_application_server.domain.entity.Player;
import com.example.game_application_server.domain.entity.Position;
import com.example.game_application_server.domain.service.GameState;
import com.example.game_application_server.dto.GameStateDTO;
import com.example.game_application_server.dto.MoveRequestDTO;
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
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
public class GameController {

    public final SimpMessagingTemplate messagingTemplate;

    // 接続中のユーザーIDを保存(sessionId -> userId)
    public static final ConcurrentHashMap<String, String> connectedUsers = new ConcurrentHashMap<>();

    // 接続上限を定義
    public static final int MAX_CONNECTIONS = 4;

    public List<PlayerInfo> playersInfo;

    public StartGameUsecase startGameUsecase;
    public DiceUsecase diceUsecase;

    public CalcMovableSquareUsecase calcMovableSquareUsecase;

    public MoveUsecase moveUsecase;

    public GetPointUsecase getPointUsecase;

    public ChangeUserPositionUsecase changeUserPositionUsecase;
    public SkipTurnUsecase skipTurnUsecase;

    public GameController(
            SimpMessagingTemplate messagingTemplate,
            StartGameUsecase startGameUsecase,
            DiceUsecase diceUsecase,
            CalcMovableSquareUsecase calcMovableSquareUsecase,
            MoveUsecase moveUsecase,
            GetPointUsecase getPointUsecase,
            ChangeUserPositionUsecase changeUserPositionUsecase,
            SkipTurnUsecase skipTurnUsecase
    ) {
        this.messagingTemplate = messagingTemplate;
        this.startGameUsecase = startGameUsecase;
        this.diceUsecase = diceUsecase;
        this.calcMovableSquareUsecase = calcMovableSquareUsecase;
        this.moveUsecase = moveUsecase;
        this.getPointUsecase = getPointUsecase;
        this.changeUserPositionUsecase=changeUserPositionUsecase;
        this.skipTurnUsecase=skipTurnUsecase;
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
            List<Position> movableSquares = calcMovableSquareUsecase.excute(diceRoll);

            messagingTemplate.convertAndSend("/topic/dice", Map.of(
                    "userId", userId,
                    "diceRoll", diceRoll,
                    "movableSquares", movableSquares
            ));

            return ResponseEntity.ok(Map.of(
                    "userId", userId,
                    "diceRoll", diceRoll,
                    "movableSquares", movableSquares
            ));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        }
    }
    @PostMapping("/move")
    public ResponseEntity<?> move(@RequestBody MoveRequestDTO requestBody) {
        int userId = requestBody.getUserId();
        Position targetPosition = requestBody.getTargetPosition();

        try {
            GameState gameState = moveUsecase.excute(targetPosition, userId);

            // WebSocketで通知
            messagingTemplate.convertAndSend("/topic/newPosition", gameState.toDTO());

            // HTTPレスポンスとしても返却
            return ResponseEntity.ok(gameState.toDTO());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/get-point")
    public ResponseEntity<?> getPoint(@RequestBody Map<String, Integer> requestBody) {
        int userId = requestBody.get("userId");
        try {
            GameState gameState = getPointUsecase.excute(userId);

            messagingTemplate.convertAndSend("/topic/get-point", gameState.toDTO());

            return ResponseEntity.ok(gameState.toDTO());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    //イベントによる
    @PostMapping("/change-position")
    public ResponseEntity<?> changePosition(@RequestBody MoveRequestDTO requestBody) {
        int userId = requestBody.getUserId();
        Position targetPosition = requestBody.getTargetPosition();

        try {
            GameState gameState = changeUserPositionUsecase.excute(targetPosition, userId);

            // WebSocketで通知
            messagingTemplate.convertAndSend("/topic/change-position", gameState.toDTO());

            // HTTPレスポンスとしても返却
            return ResponseEntity.ok(gameState.toDTO());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        }
    }
    @PostMapping("/skip-turn")
    public ResponseEntity<?> skipTurn(@RequestBody PlayerInfo requestBody) {
        int targetPlayerId = requestBody.getUserId();

        try {
            GameState gameState = skipTurnUsecase.excute(targetPlayerId);

            // WebSocketで通知
            messagingTemplate.convertAndSend("/topic/skip-turn", gameState.toDTO());

            // HTTPレスポンスとしても返却
            return ResponseEntity.ok(gameState.toDTO());
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
