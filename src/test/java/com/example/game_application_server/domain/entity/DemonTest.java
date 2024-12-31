package com.example.game_application_server.domain.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DemonTest {

    // Demonクラスのコンストラクタと初期値の確認
    @Test
    void testDemonConstructorAndInitialValues() {
        // Arrange & Act
        Demon demon = new Demon(1, 1004, "Oni", true, false);

        // Assert
        assertEquals(1, demon.getId(), "Demon ID should be 1");
        assertEquals("Oni", demon.getName(), "Demon name should be Oni");
        assertTrue(demon.isConnected, "Demon should be connected");
        assertFalse(demon.isOnBreak, "Demon should not be on break");
    }

    // captureメソッドの動作確認
    @Test
    void testCaptureVillager() {
        // Arrange
        Demon demon = new Demon(1, 1004, "Oni", true, false);
        Villager villager = new Villager(2,1004, "John", true, false);

        // Act
        demon.capture(villager);

        // Assert
        assertFalse(villager.isAlive(), "Villager should be dead after capture");
    }

    // toString() メソッドの確認
    @Test
    void testToString() {
        // Arrange
        Demon demon = new Demon(1,1004, "Oni", true, false);

        // Act
        String toStringResult = demon.toString();

        // Assert
        assertEquals("Demon { id = 1, name = Oni, isConnected = true, isOnBreak = false }",
                toStringResult, "toString output should match the expected format");
    }
}
