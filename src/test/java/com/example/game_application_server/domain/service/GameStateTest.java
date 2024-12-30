package com.example.game_application_server.domain.service;

import com.example.game_application_server.domain.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {

    private GameState gameState;

    @BeforeEach
    void setUp() {
        // テストごとに新しい GameState インスタンスを用意
        gameState = new GameState("player1", "player2", "player3", "player4");
    }

    @Test
    void testInitialSetup() {
        assertEquals(4, gameState.players.size(), "プレイヤー数が正しく初期化されていません。期待値: 4");

        assertNotNull(gameState.field, "フィールドが正しく初期化されていません。");
        assertEquals(9, gameState.field.getSize(), "フィールドサイズが正しく初期化されていません。期待値: 9");

        assertNotNull(gameState.turn, "ターン情報が正しく初期化されていません。");
        assertEquals(15, gameState.turn.getMaxTurn(), "最大ターン数が正しく初期化されていません。期待値: 15");
        assertEquals(1, gameState.turn.getCurrentTurn(), "現在のターン数が正しく初期化されていません。期待値: 1");

        assertEquals(4, gameState.playerPositions.size(), "プレイヤー位置情報が正しく初期化されていません。期待値: 4つのエントリ");

        assertFalse(gameState.isGameFinished, "ゲーム終了フラグが正しく初期化されていません。期待値: false");
    }

    @Test
    void testGetNumberOfAlivePlayers() {
        assertEquals(3, gameState.getNumberOfAlivePlayers(), "初期状態で生存している村人の数が正しくありません。期待値: 3");

        Player demon = gameState.players.get(3);
        Player villager1 = gameState.players.get(0);
        if (demon instanceof Demon && villager1 instanceof Villager) {
            ((Demon) demon).capture((Villager) villager1);
        }

        assertEquals(2, gameState.getNumberOfAlivePlayers(), "村人が1人捕まった後の生存数が正しくありません。期待値: 2");
    }

    @Test
    void testSetPlayerPositionAndGetPlayerPosition() {
        Player villager1 = gameState.players.get(0);
        Position newPos = new Position(3, 3);
        gameState.setPlayerPosition(villager1, newPos);

        Position retrievedPos = gameState.getPlayerPosition(villager1);
        assertEquals(newPos, retrievedPos, "プレイヤーの位置が正しく更新されていません。期待値: (3,3)");
    }

    @Test
    void testCalculatePossibleMovesForVillager() {
        Player villager1 = gameState.players.get(0);
        assertTrue(villager1 instanceof Villager, "最初のプレイヤーが村人ではありません。");

        List<Position> possibleMoves = gameState.calculatePossibleMoves(villager1, gameState.playerPositions, 1, gameState.field.getSize());

        Set<Position> expectedPositions = new HashSet<>();
        expectedPositions.add(new Position(1, 2));
        expectedPositions.add(new Position(3, 2));
        expectedPositions.add(new Position(2, 1));
        expectedPositions.add(new Position(2, 3));

        Set<Position> actualPositions = new HashSet<>(possibleMoves);

        assertNotNull(possibleMoves, "移動可能マスのリストが null です。");
        assertEquals(expectedPositions, actualPositions, "村人が移動可能なマスが正しくありません。期待値: " + expectedPositions);
        assertEquals(4, possibleMoves.size(), "移動可能なマスの数が正しくありません。期待値: 4");

        assertTrue(possibleMoves.contains(new Position(1, 2)), "(1,2) への移動が可能ではありません。");
        assertTrue(possibleMoves.contains(new Position(3, 2)), "(3,2) への移動が可能ではありません。");
    }

    @Test
    void testCalculatePossibleMovesForDemon() {
        Player demon = gameState.players.get(3);
        assertTrue(demon instanceof Demon, "4番目のプレイヤーが鬼ではありません。");

        List<Position> possibleMoves = gameState.calculatePossibleMoves(demon, gameState.playerPositions, 1, gameState.field.getSize());
        System.out.println(possibleMoves);

        Set<Position> expectedPositions = new HashSet<>();
        expectedPositions.add(new Position(1, 6));
        expectedPositions.add(new Position(3, 6));
        expectedPositions.add(new Position(2, 5));
        expectedPositions.add(new Position(2, 7));

        Set<Position> actualPositions = new HashSet<>(possibleMoves);

        assertNotNull(possibleMoves, "移動可能マスのリストが null です。");
        assertEquals(expectedPositions, actualPositions, "鬼が移動可能なマスが正しくありません。期待値: " + expectedPositions);
        assertEquals(4, possibleMoves.size(), "移動可能なマスの数が正しくありません。期待値: 4");

        assertTrue(possibleMoves.contains(new Position(1, 6)), "(1,6) への移動が可能ではありません。");
        assertTrue(possibleMoves.contains(new Position(2, 5)), "(2,5) への移動が可能ではありません。");
    }

    @Test
    void testToString_Detailed() {
        String result = gameState.toString();

        assertNotNull(result, "toString() の結果が null です。");

        assertTrue(result.contains("Players:"), "プレイヤー情報セクションが含まれていません。");
        assertTrue(result.contains("player1"), "player1 の情報が含まれていません。");
        assertTrue(result.contains("player4"), "player4 の情報が含まれていません。");

        assertTrue(result.contains("Field:"), "フィールド情報セクションが含まれていません。");
        assertTrue(result.contains("Size: 9"), "フィールドサイズ情報が正しく含まれていません。");
        assertTrue(result.contains("Event Positions: [Position{x=1, y=1}, Position{x=7, y=1}, Position{x=1, y=7}, Position{x=7, y=7}]"), "イベント位置情報が正しく含まれていません。");

        assertTrue(result.contains("Turn:"), "ターン情報セクションが含まれていません。");
        assertTrue(result.contains("Current Turn: 1"), "現在のターン数が正しく記載されていません。");
        assertTrue(result.contains("Total Turns: 15"), "総ターン数が正しく記載されていません。");

        assertTrue(result.contains("Player Positions:"), "プレイヤー位置情報セクションが含まれていません。");
        assertTrue(result.contains("player1 -> Position{x=2, y=2}"), "player1 の位置情報が正しく記載されていません。");
        assertTrue(result.contains("player4 -> Position{x=2, y=6}"), "player4 の位置情報が正しく記載されていません。");

        assertTrue(result.contains("Game Finished: false"), "ゲーム終了フラグ情報が正しく記載されていません。");
    }
}
