package com.example.game_application_server.domain.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResultTest {

    private Result result;

    @BeforeEach
    void setUp() {
        // 各テストの前にResultオブジェクトを初期化
        result = new Result();
    }

    @Test
    void testAddVillager() {
        // 村人をリストに追加するメソッドの動作確認
        Villager villager = new Villager(1, 1001, "Villager1", true, false);
        result.addVillager(villager);

        // 村人リストに正しく追加されているか検証
        assertEquals(1, result.villagers.size());
        assertEquals(villager, result.villagers.get(0));
    }

    @Test
    void testAddDemon() {
        // 鬼をフィールドに設定するメソッドの動作確認
        Demon demon = new Demon(1, 2001, "Demon1", true, false);
        result.addDemon(demon);

        // 鬼フィールドが正しく設定されているか検証
        assertNotNull(result.demon);
        assertEquals(demon, result.demon);
    }

    @Test
    void testSetAndIsDemonVictory() {
        // 鬼の勝利フラグを設定するメソッドと、値を取得するメソッドの動作確認
        result.setDemonVictory(true);
        // フラグがtrueに設定されているか検証
        assertTrue(result.isDemonVictory());

        result.setDemonVictory(false);
        // 初期実装ではfalseに更新されないため、その動作を確認
        assertFalse(result.isDemonVictory());
    }

    @Test
    void testVillagerRanking() {
        // 村人リストのランキングを取得するメソッドの動作確認

        // 3人の村人を作成
        Villager villager1 = new Villager(1, 1001, "Villager1", true, false);
        Villager villager2 = new Villager(2, 1002, "Villager2", true, false);
        Villager villager3 = new Villager(3, 1003, "Villager3", true, false);

        // ポイントを設定
        villager1.addPoints(); // 1ポイント
        villager2.addPoints(); villager2.addPoints(); // 2ポイント
        villager3.addPoints(); villager3.addPoints(); villager3.addPoints(); // 3ポイント

        // 村人をリストに追加
        result.addVillager(villager1);
        result.addVillager(villager2);
        result.addVillager(villager3);

        // ランキングを取得
        List<Villager> ranking = result.getVillagerRanking();

        // ポイント数が多い順に並び替えられているか検証
        assertEquals(3, ranking.size());
        assertEquals(villager3, ranking.get(0)); // 3ポイントの村人が1位
        assertEquals(villager2, ranking.get(1)); // 2ポイントの村人が2位
        assertEquals(villager1, ranking.get(2)); // 1ポイントの村人が3位
    }

}
