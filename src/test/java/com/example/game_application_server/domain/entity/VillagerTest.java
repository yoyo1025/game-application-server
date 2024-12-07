package com.example.game_application_server.domain.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VillagerTest {

    // コンストラクタと初期値の動作確認を行うテスト
    @Test
    void testVillagerConstructorAndInitialValues() {
        Villlager villager = new Villlager(1, "John", true, false);

        // Assert: 初期値が正しく設定されているかを検証
        assertEquals(1, villager.getId(), "Villager ID should be 1");
        assertEquals("John", villager.getName(), "Villager name should be John");
        assertTrue(villager.isConnected, "Villager should be connected");
        assertFalse(villager.isOnBreak, "Villager should not be on break");
        assertEquals(0, villager.getPoints(), "Initial points should be 0");
        assertTrue(villager.isAlive, "Villager should initially be alive");
    }

    // addPoints() メソッドの動作確認を行うテスト
    @Test
    void testAddPoints() {
        Villlager villager = new Villlager(1, "John", true, false);

        // addPoints() を2回呼び出してポイントを加算
        villager.addPoints();
        villager.addPoints();

        // ポイントが正しく加算されているかを検証
        assertEquals(2, villager.getPoints(), "Points should be 2 after adding twice");
    }

    // setAlive() メソッドで生死状態を変更するテスト
    @Test
    void testSetAlive() {
        Villlager villager = new Villlager(1, "John", true, false);

        // setAlive(false) を呼び出して死亡状態に設定
        villager.setAlive(false);

        // isAlive フィールドが false になっているかを検証
        assertFalse(villager.isAlive(), "Villager should be dead after setting isAlive to false");

        // setAlive(true) を呼び出して生存状態に戻す
        villager.setAlive(true);

        // isAlive フィールドが true になっているかを検証
        assertTrue(villager.isAlive(), "Villager should be alive after setting isAlive to true");
    }

    // toString() メソッドの出力確認を行うテスト
    @Test
    void testToString() {
        Villlager villager = new Villlager(1, "John", true, false);

        // toString() の結果を取得
        String toStringResult = villager.toString();

        // 期待される文字列と一致するかを検証
        assertEquals("Villager { id = 1, name = John, isConnected = true, isOnBreak = false, points = 0, isAlive = true }",
                toStringResult, "toString output should match the expected format");
    }
}
