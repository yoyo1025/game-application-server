package com.example.game_application_server.domain.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class DiceTest {

    // Diceクラスのコンストラクタと初期値の確認
    @Test
    void testDiceConstructorAndInitialValues(){
        Dice dice = new Dice(6);

        // Assert
        assertEquals(6, dice.numberOfFaces, "dice.getNumberOfFaces should be 6");
    }

    @Test
    void testRollMethod(){
        Dice dice=new Dice(6);
        int result;
        // Assert
        for(int i=0;i<10;i++){
            result= dice.roll();
            assertTrue(result>=1&&result<=6,"The result should be between 1 and 6 inclusive, but got: " + result);
        }
    }

    @Test
    void testGetNumberOfFaces(){
        Dice dice=new Dice(6);
        assertEquals(6,dice.getNumberOfFaces(),"dice.getNumberOfFaces() should be 6");
    }

    @Test
    void testToString(){
        Dice dice=new Dice(6);
        String toStringResult=dice.toString();
        assertEquals("Dice { numberOfFaces = 6 } ",toStringResult);
    }
}
