package com.example.game_application_server.infrastructure;

import com.example.game_application_server.presentation.GameController;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketEventListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // WebSocketが確立された（ユーザーがページを開き、接続した）タイミングで発火するイベント
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        // 接続時のイベント
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        String userId = headerAccessor.getFirstNativeHeader("userId"); // ヘッダーからユーザーIDを取得

        if (userId != null) {
            GameController.addUser(sessionId, userId);
            System.out.println("現在のプレイヤー数: " + GameController.connectedUsers.size());
            System.out.println("User Connected: " + userId);
        }
    }

    // ブラウザを閉じたりリロードしたり、もしくは通信が途切れたりしたことでWebSocket接続が切断された時に発火するイベント
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        // 切断時のイベント
        // StompHeaderAccessor: STOMPプロトコルのメッセージヘッダーを解析するためのクラス
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();

        // 切断したユーザーを管理リストから削除
        String userId = GameController.connectedUsers.get(sessionId);
        if (userId != null) {
            GameController.removeUser(sessionId);
            System.out.println("現在のプレイヤー数: " + GameController.connectedUsers.size());
            System.out.println("User Disconnected: " + userId);

            // 他のクライアントへ切断を通知
            messagingTemplate.convertAndSend("/topic/user-disconnected", userId + " has disconnected.");
        }
    }
}
