package com.example.game_application_server.domain.entity;

import java.util.Random;
public class Dice {
    public int numberOfFace;

    //初期化用コンストラクタ
    public Dice(int numberOfFaces){
        this.numberOfFace=numberOfFaces;
    }

    //さいころの出目を返すrollメソッド
    public int roll(){
        Random rnd=new Random();
        return rnd.nextInt(numberOfFace-1)+1;//ランダム生成が0~引数なので引数を一つ減らして出力に+1して1~にする
    }

    //さいころの面の数を表すgetNumberOfFacesメソッド
    public int getNumberOfFaces(){return numberOfFace;}
    public String toString(){
        return "Dice { numberOfFace = "+numberOfFace+" } ";
    }
}
