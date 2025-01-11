package com.example.game_application_server.domain.entity;

import com.example.game_application_server.domain.entity.EventKind;
import com.example.game_application_server.domain.entity.Square;

import java.util.List;

public class Field {
    public int size;
    public Square[][] squares;
    public List<Position> eventPositions;

    //初期化用コンストラクタ
    public Field(int size, List<Position> eventPositions){
        this.size=size;
        this.eventPositions=eventPositions;
        this.squares=new Square[size][size];
        //フィールド情報の初期化
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Position position = new Position(i, j);
                Event event = eventPositions.stream().anyMatch(pos -> pos.equals(position)) ? new Event(EventKind.ROLL_AGAIN) : null;
                squares[i][j] = new Square(position, event);
            }
        }
    }

    //指定したマスオブジェクトを取得
    public Square getSquare(int x, int y){
        return squares[x][y];
    }

    //フィールドサイズを取得
    public int getSize(){
        return size;
    }

    //イベントマスの集合を変えす
    public List<Position> getEventPositions(){
        return eventPositions;
    }
}
