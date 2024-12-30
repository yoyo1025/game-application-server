package com.example.game_application_server.domain.entity;

import java.util.Objects;

public class Player {
    // プレイヤーID
    public int id;
    // プレイヤー名
    public String name;
    // WebSocketの接続状態を表す。接続中ならtrue。
    public boolean isConnected;
    // 休憩中か否かを表す。休憩中ならtrue。
    public boolean isOnBreak;

    public Player(int id, String name, boolean isConnected, boolean isOnBreak) {
        this.id = id;
        this.name = name;
        this.isConnected = isConnected;
        this.isOnBreak = isOnBreak;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isConnected() {
        return this.isConnected;
    }

    public void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public boolean isOnBreak() {
        return this.isOnBreak;
    }

    public void setOnBreak(boolean isOnBreak) {
        this.isOnBreak = isOnBreak;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id == player.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Player { id = " + id + ", name = " + name + ", isConnected = " + isConnected + ", isOnBreak = " + isOnBreak + " }";
    }
}
