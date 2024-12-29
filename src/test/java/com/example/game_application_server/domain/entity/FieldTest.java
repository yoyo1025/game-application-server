package com.example.game_application_server.domain.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FieldTest {

    private Field field;
    private int size = 5; // フィールドサイズを設定
    private List<Position> eventPositions; // イベントマスの位置を保持するリスト

    @BeforeEach
    public void setUp() {
        // テスト前の初期化処理
        eventPositions = new ArrayList<>();
        eventPositions.add(new Position(1, 1)); // イベント位置を追加
        eventPositions.add(new Position(2, 2)); // イベント位置を追加
        field = new Field(size, eventPositions); // フィールドを初期化
    }

    @Test
    public void testGetSize() {
        // フィールドサイズが正しく取得できることを確認
        assertEquals(size, field.getSize(), "Field size should be correctly returned.");
    }

    @Test
    public void testGetEventPositions() {
        // イベント位置が正しく初期化されていることを確認
        assertEquals(eventPositions, field.getEventPositions(), "Event positions should match the initialized values.");
    }

    @Test
    public void testGetSquare() {
        // 指定した座標のSquareオブジェクトを取得し、初期化が正しいことを確認
        Square square = field.getSquare(1, 1);
        assertNotNull(square, "Square should not be null."); // Squareオブジェクトがnullでないことを確認
        assertEquals(new Position(1, 1), square.getPosition(), "Square position should match the requested coordinates."); // 座標が一致することを確認
        assertNotNull(square.getEvent(), "Square at event position should have an event."); // イベントが設定されていることを確認
        assertEquals(EventKind.ROLL_AGAIN, square.getEvent().getKind(), "Event kind should be ROLL_AGAIN."); // イベントの種類を確認
    }

    @Test
    public void testSquareInitialization() {
        // フィールド内のすべてのSquareオブジェクトが正しく初期化されていることを確認
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Square square = field.getSquare(i, j);
                assertNotNull(square, "All squares should be initialized."); // Squareオブジェクトがnullでないことを確認
                assertEquals(new Position(i, j), square.getPosition(), "Square position should be correctly set."); // 座標が一致することを確認

                if (eventPositions.contains(new Position(i, j))) {
                    assertNotNull(square.getEvent(), "Event positions should have an event."); // イベントマスにイベントが設定されていることを確認
                } else {
                    assertNull(square.getEvent(), "Non-event positions should not have an event."); // 非イベントマスにイベントが設定されていないことを確認
                }
            }
        }
    }

    @Test
    public void testSetAndGetSquareEvent() {
        // Squareにイベントを設定し、それが正しく取得できることを確認
        Position pos = new Position(3, 3);
        Event event = new Event(EventKind.WARP); // 新しいイベントを作成
        Square square = field.getSquare(pos.getX(), pos.getY()); // 指定位置のSquareを取得
        square.setEvent(event); // イベントを設定

        assertTrue(square.hasEvent(), "Square should have an event set."); // イベントが設定されていることを確認
        assertEquals(event, square.getEvent(), "Square's event should match the set event."); // 設定したイベントが正しく取得できることを確認
    }
}
