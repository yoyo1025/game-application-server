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

        // 5人の村人を作成
        Villager villager1 = new Villager(1, 1001, "Villager1", true, false); // 生存、1ポイント
        Villager villager2 = new Villager(2, 1002, "Villager2", true, false); // 生存、2ポイント
        Villager villager3 = new Villager(3, 1003, "Villager3", true, false); // 生存、3ポイント
        Villager villager4 = new Villager(4, 1004, "Villager4", true, false); // 生存、2ポイント
        Villager villager5 = new Villager(5, 1005, "Villager5", true, false); // 死亡、0ポイント

        // ポイントを設定
        villager1.addPoints(); // 1ポイント
        villager2.addPoints(); villager2.addPoints(); // 2ポイント
        villager3.addPoints(); villager3.addPoints(); villager3.addPoints(); // 3ポイント
        villager4.addPoints(); villager4.addPoints(); // 2ポイント
        villager5.setAlive(false); // 死亡状態

        // 村人をリストに追加
        result.addVillager(villager1);
        result.addVillager(villager2);
        result.addVillager(villager3);
        result.addVillager(villager4);
        result.addVillager(villager5);

        // ランキングを取得
        List<Villager> ranking = result.getVillagerRanking();

        // 順位確認
        assertEquals(5, ranking.size());

        // 生存者のポイント順
        assertEquals(villager3, ranking.get(0)); // 1位（3ポイント）
        assertEquals(villager2, ranking.get(1)); // 2位（2ポイント）
        assertEquals(villager4, ranking.get(2)); // 2位（2ポイント、同順位）
        assertEquals(villager1, ranking.get(3)); // 4位（1ポイント）

        // 死亡者は最後
        assertEquals(villager5, ranking.get(4)); // 最下位（死亡）
    }


}
