package com.example.game_application_server.domain.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SquareTest {

    @Test
    void testGetPosition() {
        Position position = new Position(3, 5);
        Square square = new Square(position, null);

        assertEquals(position, square.getPosition());
    }

    @Test
    void testGetEvent() {
        Position position = new Position(3, 5);
        Event event = new Event(EventKind.ROLL_AGAIN);
        Square square = new Square(position, event);

        assertEquals(event, square.getEvent());
    }

    @Test
    void testSetEvent() {
        Position position = new Position(3, 5);
        Event initialEvent = new Event(EventKind.SKIP_TURN);
        Square square = new Square(position, initialEvent);

        Event newEvent = new Event(EventKind.WARP);
        square.setEvent(newEvent);

        assertEquals(newEvent, square.getEvent());
    }

    @Test
    void testHasEvent() {
        Position position = new Position(3, 5);

        // イベントが設定されている場合
        Event event = new Event(EventKind.ROLL_AGAIN);
        Square squareWithEvent = new Square(position, event);
        assertTrue(squareWithEvent.hasEvent());

        // イベントが設定されていない場合
        Square squareWithoutEvent = new Square(position, null);
        assertFalse(squareWithoutEvent.hasEvent());
    }

    @Test
    void testToString() {
        Position position = new Position(3, 5);
        Event event = new Event(EventKind.ROLL_AGAIN);
        Square squareWithEvent = new Square(position, event);
        Square squareWithoutEvent = new Square(position, null);

        assertEquals("Square{position=Position{x=3, y=5}, event=Event{kind=ROLL_AGAIN}}", squareWithEvent.toString());
        assertEquals("Square{position=Position{x=3, y=5}, event=No Event}", squareWithoutEvent.toString());
    }
}
