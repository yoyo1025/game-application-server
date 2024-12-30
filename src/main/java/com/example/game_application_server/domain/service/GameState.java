package com.example.game_application_server.domain.service;

import com.example.game_application_server.domain.entity.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameState {
    public List<Player> players; // 各プレイヤーのリスト

    public Field field; // フィールド

    public Turn turn; // ターン

    public Map<Player, Position> playerPositions; // プレイヤーの位置を表す

    public boolean isGameFinished; // ゲームが終了したかのフラグ

    public GameState() {
        // フィールド初期化
        this.players = new ArrayList<>();
        this.playerPositions = new HashMap<>();

        // プレイヤー初期化
        Villager villager1 = new Villager(1, "player1", true, false);
        Villager villager2 = new Villager(2, "player2", true, false);
        Villager villager3 = new Villager(3, "player3", true, false);
        Demon demon = new Demon(4, "player4", true, false);
        this.players.add(villager1);
        this.players.add(villager2);
        this.players.add(villager3);
        this.players.add(demon);

        // フィールド初期化
        List<Position> eventPositions = new ArrayList<>();
        eventPositions.add(new Position(1, 1));
        eventPositions.add(new Position(7, 1));
        eventPositions.add(new Position(1, 7));
        eventPositions.add(new Position(7, 7));
        this.field = new Field(9, eventPositions);

        // ターン初期化
        this.turn = new Turn(15, 1);

        // プレイヤーの位置を初期化
        Position villager1FirstPosition = new Position(2, 2);
        Position villager2FirstPosition = new Position(6, 2);
        Position villager3FirstPosition = new Position(6, 6);
        Position demonFirstPosition = new Position(2, 6);
        this.playerPositions.put(villager1, villager1FirstPosition);
        this.playerPositions.put(villager2, villager2FirstPosition);
        this.playerPositions.put(villager3, villager3FirstPosition);
        this.playerPositions.put(demon, demonFirstPosition);

        // ゲーム終了フラグの初期化
        this.isGameFinished = false;
    }
}
