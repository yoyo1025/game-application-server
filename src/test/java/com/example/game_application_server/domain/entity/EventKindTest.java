package com.example.game_application_server.domain.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventKindTest {

    @Test
    void testEventKindValues() {
        // 全ての列挙値を取得
        EventKind[] eventKinds = EventKind.values();

        // 列挙値の数を確認
        assertEquals(3, eventKinds.length);

        // 列挙値の内容を確認
        assertEquals(EventKind.ROLL_AGAIN, eventKinds[0]);
        assertEquals(EventKind.SKIP_TURN, eventKinds[1]);
        assertEquals(EventKind.WARP, eventKinds[2]);
    }

    @Test
    void testValueOf() {
        // 文字列から列挙値を取得
        assertEquals(EventKind.ROLL_AGAIN, EventKind.valueOf("ROLL_AGAIN"));
        assertEquals(EventKind.SKIP_TURN, EventKind.valueOf("SKIP_TURN"));
        assertEquals(EventKind.WARP, EventKind.valueOf("WARP"));

        // 無効な文字列を使用した場合の例外を確認
        assertThrows(IllegalArgumentException.class, () -> EventKind.valueOf("INVALID_EVENT"));
    }

    @Test
    void testEnumToString() {
        // 各列挙値のtoStringが期待通りか確認
        assertEquals("ROLL_AGAIN", EventKind.ROLL_AGAIN.toString());
        assertEquals("SKIP_TURN", EventKind.SKIP_TURN.toString());
        assertEquals("WARP", EventKind.WARP.toString());
    }
}
