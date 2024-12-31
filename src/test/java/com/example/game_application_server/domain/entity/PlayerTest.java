package com.example.game_application_server.domain.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    // コンストラクタとGetterの動作確認を行うテスト
    @Test
    void testPlayerConstructorAndGetter() {
        Player player = new Player(1, 1001,"John", true, false);

        // フィールドが正しく初期化されているかを検証
        assertEquals(1, player.getId(), "Player ID should be 1");
        assertEquals(1001, player.getUserId(), "User ID should be 1001");
        assertEquals("John", player.getName(), "Player name should be John");
        assertTrue(player.isConnected(), "Player should be connected");
        assertFalse(player.isOnBreak(), "Player should not be on break");
    }

    // Setterの動作確認を行うテスト
    @Test
    void testPlayerSetter() {
        Player player = new Player(1, 1001, "John", true, false);

        // フィールドの値を変更
        player.setId(2);
        player.setUserId(1002);
        player.setName("Jane");
        player.setConnected(false);
        player.setOnBreak(true);


        // 変更後の値が正しく反映されているかを検証
        assertEquals(2, player.getId(), "Player ID should be updated to 2");
        assertEquals(1002, player.getUserId(), "User ID should be updated to 1002");
        assertEquals("Jane", player.getName(), "Player name should be updated to Jane");
        assertFalse(player.isConnected(), "Player should not be connected");
        assertTrue(player.isOnBreak(), "Player should be on break");
    }

    // toString() メソッドの出力が期待通りであることを確認するテスト
    @Test
    void testToString() {
        Player player = new Player(1, 1001, "John", true, false);

        String toStringResult = player.toString();

        assertEquals("Player { id = 1, userId = 1001, name = John, isConnected = true, isOnBreak = false }",
                toStringResult, "toString output should match");
    }

    // equals メソッドの動作確認を行うテスト
    @Test
    void testEquals() {
        Player player1 = new Player(1, 1001, "John", true, false);
        Player player2 = new Player(1, 1001,  "John", true, false);
        Player player3 = new Player(2,1002, "Jane", false, true);

        // 同一IDを持つプレイヤーは等価とみなされる
        assertEquals(player1, player2, "Players with the same ID should be equal");
        // 異なるIDを持つプレイヤーは等価ではない
        assertNotEquals(player1, player3, "Players with different IDs should not be equal");
        // nullと比較すると等価ではない
        assertNotEquals(player1, null, "Player should not be equal to null");
        // 同一インスタンスは等価
        assertEquals(player1, player1, "Same instance should be equal to itself");
    }

    // hashCode メソッドの動作確認を行うテスト
    @Test
    void testHashCode() {
        Player player1 = new Player(1,1001, "John", true, false);
        Player player2 = new Player(1,1001,"John", true, false);
        Player player3 = new Player(2,1002, "Jane", false, true);

        // 同一IDを持つプレイヤーのハッシュコードは等しい
        assertEquals(player1.hashCode(), player2.hashCode(), "Hash codes for players with the same ID should be equal");
        // 異なるIDを持つプレイヤーのハッシュコードは異なる可能性が高い
        assertNotEquals(player1.hashCode(), player3.hashCode(), "Hash codes for players with different IDs should not be equal");
    }

    // IDが負の値の場合のテスト
    @Test
    void testNegativeId() {
        Player player = new Player(-1, 999, "NegativeIDPlayer", true, false);

        assertEquals(-1, player.getId(), "Player ID should allow negative values");
        assertEquals("NegativeIDPlayer", player.getName(), "Player name should match");
    }

    // null値の扱いのテスト（Setterによる変更）
    @Test
    void testNullValues() {
        Player player = new Player(1, 1001,"John", true, false);

        // 名前をnullに設定
        player.setName(null);

        assertNull(player.getName(), "Player name should allow null values");
    }
}
