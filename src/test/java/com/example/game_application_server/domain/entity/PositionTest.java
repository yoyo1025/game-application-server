package com.example.game_application_server.domain.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    void testConstructorAndGetters() {
        Position position = new Position(5, 10);

        // Check initial values
        assertEquals(5, position.getX());
        assertEquals(10, position.getY());
    }

    @Test
    void testSetters() {
        Position position = new Position(0, 0);

        // Update x and y values
        position.setX(15);
        position.setY(20);

        // Check updated values
        assertEquals(15, position.getX());
        assertEquals(20, position.getY());
    }

    @Test
    void testGetPosition() {
        Position position = new Position(7, 14);

        // Get new Position instance
        Position newPosition = position.getPosition();

        // Check values of the new Position
        assertEquals(7, newPosition.getX());
        assertEquals(14, newPosition.getY());

        // Ensure the original and new positions are not the same instance
        assertNotSame(position, newPosition);
    }

    @Test
    void testToString() {
        Position position = new Position(3, 6);

        // Check string representation
        String expected = "Position{x=3, y=6}";
        assertEquals(expected, position.toString());
    }
}
