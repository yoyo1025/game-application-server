package com.example.game_application_server.domain.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
public class Result {
    public List<Villager> villagers;
    public Demon demon;
    public boolean demonVictory;

    //フィールドの所期化
    public Result(){
        this.villagers=new ArrayList<>();
        this.demonVictory=false;
    }

    //村人追加メソッド
    public void addVillager(Villager villager){
        villagers.add(villager);
    }

    //鬼追加メソッド
    public void addDemon(Demon demon){
        this.demon=demon;
    }

    //鬼の勝利かどうか(trueが鬼勝利)
    public void setDemonVictory(boolean victory){
        demonVictory=victory;
    }

    //鬼が勝ったかを返す
    public boolean isDemonVictory(){
        return demonVictory;
    }

    public List<Villager> getVillagerRanking() {
        // 1) villagers をコピーして、新しいリストを作る
        List<Villager> sorted = new ArrayList<>(villagers);

        // 2) Comparator でソート
        //   a) 生きている村人を先に
        //   b) 同じ Alive/Dead 同士はポイント降順
        sorted.sort((v1, v2) -> {
            // ① どちらかが生存、片方が死亡なら生存側を先に
            if (v1.isAlive() && !v2.isAlive()) {
                return -1;  // v1 が先
            } else if (!v1.isAlive() && v2.isAlive()) {
                return 1;   // v2 が先
            }
            // ② 両方とも生存 or 両方とも死亡 → ポイント比較（降順）
            return Integer.compare(v2.getPoints(), v1.getPoints());
        });

        // 3) 同ポイントなら同順位を付ける
        //    - 生存/死亡の境界も判定
        int currentRank = 1;
        int previousPoints = -1;
        boolean previousAlive = true; // とりあえず初期値
        for (int i = 0; i < sorted.size(); i++) {
            Villager current = sorted.get(i);
            if (i == 0) {
                // リストの先頭は必ず rank=1
                current.setRank(currentRank);
                previousPoints = current.getPoints();
                previousAlive = current.isAlive();
            } else {
                // 前の村人と「alive状態 & ポイント」が同じなら同順位
                if (current.isAlive() == previousAlive && current.getPoints() == previousPoints) {
                    current.setRank(currentRank);
                } else {
                    // 違う場合は 「(現在のインデックス) + 1」が新しい順位
                    currentRank = i + 1;
                    current.setRank(currentRank);
                    previousPoints = current.getPoints();
                    previousAlive = current.isAlive();
                }
            }
        }

        // 4) ソート＆順位付けが完了した村人リストを返す
        return sorted;
    }

}
