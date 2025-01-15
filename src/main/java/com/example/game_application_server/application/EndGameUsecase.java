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

    public List<BattleRecord> execute(Result gameResult) {
        boolean demonVictory = gameResult.isDemonVictory();

        List<BattleRecord> records = new ArrayList<>();

        // 村人ランキングの取得
        List<Villager> villagerRanking = new ArrayList<>(gameResult.getVillagerRanking());
        for (Villager villager : villagerRanking) {
            boolean isWin = !demonVictory; // 村人が勝ったかどうか
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

        // 鬼の記録を追加
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

        // DBに保存
        battleRecordRepository.saveAll(records);

        // 生成したレコードを返す
        return records;
    }

}
