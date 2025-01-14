package com.example.game_application_server.application;

import com.example.game_application_server.domain.entity.*;
import com.example.game_application_server.domain.repository.BattleRecordRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EndGameUsecase {

    private final BattleRecordRepository battleRecordRepository;

    public EndGameUsecase(BattleRecordRepository battleRecordRepository) {
        this.battleRecordRepository = battleRecordRepository;
    }

    /**
     * ゲーム終了時にResultを受け取り、各プレイヤーの戦績をDB保存
     */
//    public void execute(Result gameResult) {
//        // ① Demonの勝利フラグを確認
//        boolean demonVictory = gameResult.isDemonVictory();
//
//        // ② DBに保存するBattleRecordを作成
//        List<BattleRecord> records = new ArrayList<>();
//
//        // Villagers
//        for (Villager villager : gameResult.getVillagerRanking()) {
//            boolean isWin = !demonVictory; // 鬼が負けたなら村人勝ち
//            int point = villager.getPoints();
//            int ranking = gameResult.getVillagerRanking().indexOf(villager) + 1;
//
//            BattleRecord record = new BattleRecord(
//                    villager.getUserId(),
//                    villager.getName(),
//                    "Villager",
//                    isWin,
//                    point,
//                    ranking
//            );
//            records.add(record);
//        }
//
//        // Demon
//        if (gameResult.demon != null) {
//            boolean isWin = demonVictory; // demonVictoryがtrueなら鬼の勝ち
//            // 鬼はvillagerのランキングと別枠なので暫定で 1 とか特別な値
//            int ranking = isWin ? 1 : gameResult.getVillagerRanking().size() + 1;
//
//            BattleRecord record = new BattleRecord(
//                    gameResult.demon.getUserId(),
//                    gameResult.demon.getName(),
//                    "Demon",
//                    isWin,
//                    0,
//                    ranking
//            );
//            records.add(record);
//        }
//
//        // ③ リポジトリに保存
//        battleRecordRepository.saveAll(records);
//    }

    public void execute(Result gameResult) {
        boolean demonVictory = gameResult.isDemonVictory();

        List<BattleRecord> records = new ArrayList<>();

        List<Villager> villagerRanking = new ArrayList<>(gameResult.getVillagerRanking()); // コピーを作成
        for (Villager villager : villagerRanking) {
            boolean isWin = !demonVictory;
            int point = villager.getPoints();
            int ranking = villagerRanking.indexOf(villager) + 1;

            BattleRecord record = new BattleRecord(
                    villager.getUserId(),
                    villager.getName(),
                    "Villager",
                    isWin,
                    point,
                    ranking
            );
            records.add(record);
        }

        if (gameResult.demon != null) {
            boolean isWin = demonVictory;
            int ranking = isWin ? 1 : villagerRanking.size() + 1;

            BattleRecord record = new BattleRecord(
                    gameResult.demon.getUserId(),
                    gameResult.demon.getName(),
                    "Demon",
                    isWin,
                    0,
                    ranking
            );
            records.add(record);
        }

        battleRecordRepository.saveAll(records);
    }

}
