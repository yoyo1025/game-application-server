package com.example.game_application_server.domain.entity;

public class Turn {

    public int max_turn;
    public int current_turn;

    public int currentPlayerIndex = 1; // 1 ~ 4 の値

    public int maxCurrentPlayerIndex = 4;


    //初期化のコンストラクタ
    public Turn(int max_Turn,int current_Turn){
     this.max_turn=max_Turn;
     this.current_turn=current_Turn;
    }

    //現在のターン数を返す
    public int getCurrentTurn(){

        return current_turn;

    }

    //ゲームの最長ターンを返す
    public int getMaxTurn(){

        return max_turn;
    }

    //ターンを進める
    public void nextTurn(){
        current_turn++;
    }


    //現在のターンが最長ターンに達したかを検証する
    public boolean isMaxTurnReached(){
        if(current_turn==max_turn){
            return true;
        }else{
            return false;
        }
    }

    public int getCurrentPlayerIndex() {
        return this.currentPlayerIndex;
    }

    // 次のプレイヤーインデックスに進める
    public void nextPlayerIndex() {
        this.currentPlayerIndex++;
        if (this.currentPlayerIndex > 4) {
            this.currentPlayerIndex = 1;
            this.current_turn++;
        }
    }

    //フィールドの全ての変数値をログに出力する
    public String toString() {
        return "Turn { max_turn = " + max_turn + ", current_turn = " + current_turn + " }";
    }
}


