package com.example.game_application_server.domain.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    // コンストラクタとGetterの動作確認を行うテスト
    @Test
    void testPlayerConstructorAndGetter() {
        Player player = new Player(1, "John", true, false);

        // フィールドが正しく初期化されているかを検証
        assertEquals(1, player.getId(), "Player ID should be 1");
        assertEquals("John", player.getName(), "Player name should be John");
        assertTrue(player.isConnected(), "Player should be connected");
        assertFalse(player.isOnBreak(), "Player should not be on break");
    }

    // Setterの動作確認を行うテスト
    @Test
    void testPlayerSetter() {
        Player player = new Player(1, "John", true, false);

        // フィールドの値を変更
        player.setId(2);
        player.setName("Jane");
        player.setConnected(false);
        player.setOnBreak(true);


        // 変更後の値が正しく反映されているかを検証
        assertEquals(2, player.getId(), "Player ID should be updated to 2");
        assertEquals("Jane", player.getName(), "Player name should be updated to Jane");
        assertFalse(player.isConnected(), "Player should not be connected");
        assertTrue(player.isOnBreak(), "Player should be on break");
    }

    // toString() メソッドの出力が期待通りであることを確認するテスト
    @Test
    void testToString() {
        Player player = new Player(1, "John", true, false);

        String toStringResult = player.toString();

        assertEquals("Player { id = 1, name = John, isConnected = true, isOnBreak = false }",
                toStringResult, "toString output should match");
    }
}
