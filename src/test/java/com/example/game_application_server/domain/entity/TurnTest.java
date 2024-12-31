package com.example.game_application_server.domain.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TurnTest {

    // コンストラクタとGetterの動作確認を行うテスト
    @Test
    void testTurnConstructorAndGetter(){

        Turn turn = new Turn(10,0);
        assertEquals(10,turn.getMaxTurn(),"MaxTurn should be 10");
        assertEquals(0,turn.getCurrentTurn(),"CurrentTurn should be 0");

    }

    //ターンを正しく進められているかのテスト
    @Test
    void testNextTurn(){
        Turn turn = new Turn(10,0);
        turn.nextTurn();
        assertEquals(1,turn.getCurrentTurn(),"CurrentTurn should be 1");
    }

    //現在のターンが最長ターンに達したかを確認するテスト
    @Test
    void testIsMaxTurnReached(){
        Turn turn1 = new Turn(10,10);
        Turn turn2 = new Turn(10,0);

        assertTrue(turn1.isMaxTurnReached(),"Turn is Max");
        assertFalse(turn2.isMaxTurnReached(),"Turn is not Max");
    }

    // toString() メソッドの出力が期待通りであることを確認するテスト
    @Test
    void testToString() {
        Turn turn = new Turn(10,0);

        String toStringResult = turn.toString();

        assertEquals("Turn { max_turn = 10, current_turn = 0 }",
                toStringResult, "toString output should match");
    }

    // 現在のプレイヤーインデックスを正しく取得できることを確認するテスト
    @Test
    void testGetCurrentPlayerIndex() {
        Turn turn = new Turn(10,0);
        assertEquals(1, turn.getCurrentPlayerIndex(), "Initial player index should be 1");
    }

    // プレイヤーインデックスが正しく進むことを確認するテスト
    @Test
    void testNextPlayerIndex() {
        Turn turn = new Turn(10,0);

        turn.nextPlayerIndex();
        assertEquals(2, turn.getCurrentPlayerIndex(), "Player index should be 2");

        turn.nextPlayerIndex();
        assertEquals(3, turn.getCurrentPlayerIndex(), "Player index should be 3");

        turn.nextPlayerIndex();
        assertEquals(4, turn.getCurrentPlayerIndex(), "Player index should be 4");

        turn.nextPlayerIndex();
        assertEquals(1, turn.getCurrentPlayerIndex(), "Player index should loop back to 1");
    }
}
