package com.example.game_application_server.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerInfoTest {

    @Test
    void testDeserialization() throws Exception {
        // テスト用JSONデータ
        String json = """
        {
            "userId": 3,
            "userName": "player1",
            "isDemon": true
        }
        """;

        // Jackson ObjectMapperを使用してJSONをデシリアライズ
        ObjectMapper objectMapper = new ObjectMapper();
        PlayerInfo playerInfo = objectMapper.readValue(json, PlayerInfo.class);

        // フィールドの値を検証
        assertEquals(3, playerInfo.getUserId());
        assertEquals("player1", playerInfo.getUserName());
        assertTrue(playerInfo.isDemon());
    }

    @Test
    void testSerialization() throws Exception {
        // テスト用のPlayerInfoオブジェクト
        PlayerInfo playerInfo = new PlayerInfo();
        playerInfo.setUserId(3);
        playerInfo.setUserName("player1");
        playerInfo.setDemon(true);

        // Jackson ObjectMapperを使用してJSONをシリアライズ
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(playerInfo);

        // JSON文字列に正しいデータが含まれているか確認
        assertTrue(json.contains("\"userId\":3"));
        assertTrue(json.contains("\"userName\":\"player1\""));
        assertTrue(json.contains("\"isDemon\":true"));
    }
}
