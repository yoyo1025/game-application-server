
package com.example.game_application_server.domain.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FieldTest {

    private Field field;
    private int size = 5;
    private List<Position> eventPositions;

    @BeforeEach
    public void setUp() {
        eventPositions = new ArrayList<>();
        eventPositions.add(new Position(1, 1));
        eventPositions.add(new Position(2, 2));
        field = new Field(size, eventPositions);
    }

    @Test
    public void testGetSize() {
        assertEquals(size, field.getSize(), "Field size should be correctly returned.");
    }

    @Test
    public void testGetEventPositions() {
        assertEquals(eventPositions, field.getEventPositions(), "Event positions should match the initialized values.");
    }

    @Test
    public void testGetSquare() {
        Square square = field.getSquare(1, 1);
        assertNotNull(square, "Square should not be null.");
        assertEquals(new Position(1, 1), square.getPosition(), "Square position should match the requested coordinates.");
        assertNotNull(square.getEvent(), "Square at event position should have an event.");
        assertEquals(EventKind.ROLL_AGAIN, square.getEvent().getKind(), "Event kind should be ROLL_AGAIN.");
    }

    @Test
    public void testSquareInitialization() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Square square = field.getSquare(i, j);
                assertNotNull(square, "All squares should be initialized.");
                assertEquals(new Position(i, j), square.getPosition(), "Square position should be correctly set.");

                if (eventPositions.contains(new Position(i, j))) {
                    assertNotNull(square.getEvent(), "Event positions should have an event.");
                } else {
                    assertNull(square.getEvent(), "Non-event positions should not have an event.");
                }
            }
        }
    }

    @Test
    public void testSetAndGetSquareEvent() {
        Position pos = new Position(3, 3);
        Event event = new Event(EventKind.WARP);
        Square square = field.getSquare(pos.getX(), pos.getY());
        square.setEvent(event);

        assertTrue(square.hasEvent(), "Square should have an event set.");
        assertEquals(event, square.getEvent(), "Square's event should match the set event.");
    }
}