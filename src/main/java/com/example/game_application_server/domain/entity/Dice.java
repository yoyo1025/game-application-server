package com.example.game_application_server.domain.entity;

import java.util.Random;
public class Dice {
    public int numberOfFaces;

    //初期化用コンストラクタ
    public Dice(int numberOfFaces){
        this.numberOfFaces=numberOfFaces;
    }

    //さいころの出目を返すrollメソッド
    public int roll(){
        Random rnd=new Random();
        return rnd.nextInt(numberOfFaces-1)+1;//ランダム生成が0~引数なので引数を一つ減らして出力に+1して1~にする
    }

    //さいころの面の数を表すgetNumberOfFacesメソッド
    public int getNumberOfFaces(){return numberOfFaces;}
    public String toString(){
        return "Dice { numberOfFaces = "+numberOfFaces+" } ";
    }
}