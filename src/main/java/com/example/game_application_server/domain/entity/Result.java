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

    //村人のランキングを返す
    public List<Villager> getVillagerRanking(){
        //ポイント数が大きい順に並び替え
        Collections.sort(villagers, new Comparator<Villager>() {
            @Override
            public int compare(Villager o1, Villager o2) {
                return Integer.compare(o2.getPoints(),o1.getPoints());
            }
        });
        return villagers;
    }
}
