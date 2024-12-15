package com.example.game_application_server.domain.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class EventTest {

    private Dice mockDice; // サイコロのモックオブジェクト
    private Event event;   // テスト対象の Event オブジェクト

    private Player player1; // プレイヤー1
    private Player player2; // プレイヤー2

    private Map<Player, Position> playerPositionMap; // プレイヤーとその位置を管理するマップ
    private List<Player> players; // プレイヤーのリスト

    @BeforeEach
    void setUp() {
        // サイコロのモックを作成
        mockDice = Mockito.mock(Dice.class);
        event = new Event(EventKind.ROLL_AGAIN);
        event.dice = mockDice;

        // プレイヤーを初期化
        player1 = new Player(1, "Alice", true, false);
        player2 = new Player(2, "Bob", true, false);

        // プレイヤーリストを作成
        players = Arrays.asList(player1, player2);

        // プレイヤーの位置を設定
        playerPositionMap = new HashMap<>();
        playerPositionMap.put(player1, new Position(3, 3));
        playerPositionMap.put(player2, new Position(4, 3));
    }

    @Test
    void testExecuteRollAgain() {
        // サイコロの目を3に固定
        when(mockDice.roll()).thenReturn(3);

        // Player1がROLL_AGAINイベントを実行
        List<Position> positions = event.executeRollAgain(playerPositionMap, players, 0);

        // 結果がnullでないことを確認
        assertNotNull(positions);

        // 現在地 (3, 3) と他プレイヤーの位置 (4, 3) が除外されていることを確認
        assertFalse(positions.contains(new Position(3, 3))); // 現在地
        assertFalse(positions.contains(new Position(4, 3))); // 他プレイヤーの位置

        // サイコロの目を基に移動可能な座標が正しく含まれていることを確認
        assertTrue(positions.contains(new Position(3, 6))); // 上方向に3マス
        assertTrue(positions.contains(new Position(6, 3))); // 右方向に3マス
    }

    @Test
    void testExecuteRollAgain_NoPosition() {
        // player1の位置情報を削除
        playerPositionMap.remove(player1);

        // サイコロの目を2に固定
        when(mockDice.roll()).thenReturn(2);

        // プレイヤーの位置が存在しない場合に例外がスローされることを確認
        assertThrows(IllegalArgumentException.class, () -> event.executeRollAgain(playerPositionMap, players, 0));
    }

    @Test
    void testExecuteSkipTurn() {
        // SKIP_TURN イベントを設定
        event.setKind(EventKind.SKIP_TURN);

        // Player2を休憩状態に設定
        event.executeSkipTurn(players, 1);

        // Player2が休憩状態になっていることを確認
        assertTrue(player2.isOnBreak);
    }

    @Test
    void testExecuteWarp() {
        // WARP イベントを設定
        event.setKind(EventKind.WARP);

        // プレイヤー1 (3, 3) とプレイヤー2 (4, 3) の位置を入れ替え
        event.executeWarp(playerPositionMap, players, 0, 1);

        // 位置が正しく入れ替わっていることを確認
        assertEquals(new Position(4, 3), playerPositionMap.get(player1));
        assertEquals(new Position(3, 3), playerPositionMap.get(player2));
    }

    @Test
    void testExecuteEvent() {
        // ROLL_AGAIN イベントの動作確認
        when(mockDice.roll()).thenReturn(3);
        event.setKind(EventKind.ROLL_AGAIN);

        List<Position> positions = event.executeEvent(playerPositionMap, players, 1, 2);
        // 結果がnullでないことを確認
        assertNotNull(positions);

        // 現在地 (3, 3) と他プレイヤーの位置 (4, 3) が除外されていることを確認
        assertFalse(positions.contains(new Position(3, 3))); // 現在地
        assertFalse(positions.contains(new Position(4, 3))); // 他プレイヤーの位置

        // サイコロの目を基に移動可能な座標が正しく含まれていることを確認
        assertTrue(positions.contains(new Position(3, 6))); // 上方向に3マス
        assertTrue(positions.contains(new Position(6, 3))); // 右方向に3マス

        // SKIP_TURN イベントの動作確認
        event.setKind(EventKind.SKIP_TURN);
        event.executeEvent(playerPositionMap, players, 1, 2);
        assertTrue(player2.isOnBreak);

        // WARP イベントの動作確認
        event.setKind(EventKind.WARP);
        event.executeEvent(playerPositionMap, players, 1, 2);
        assertEquals(new Position(4, 3), playerPositionMap.get(player1));
    }

    @Test
    void testExecuteEvent_UnknownKind() {
        // イベント種別が無効な場合に例外がスローされることを確認
        event.setKind(null);
        assertThrows(NullPointerException.class, () -> event.executeEvent(playerPositionMap, players, 1, 2));
    }

    @Test
    void testToString() {
        // toString メソッドの結果が期待通りであることを確認
        assertEquals("Event{kind=ROLL_AGAIN}", event.toString());
    }
}
