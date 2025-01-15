package com.example.game_application_server.infrastructure;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // クライアントへの送り口の設定
        config.enableSimpleBroker("/topic");
        // クライアントからの受け取り口の設定
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // クライアントが最初にWebSocketを繋ぐ際の繋ぎ口
        registry.addEndpoint("/app-websocket")
                .setAllowedOrigins(
                        "http://localhost:3000",
                        "http://172.31.120.116:3000",
                        "http://172.31.125.54:3000",
                        "http://172.30.161.76:3000",
                        "http://172.31.94.191:3000"
                )
                .withSockJS();
    }
}

