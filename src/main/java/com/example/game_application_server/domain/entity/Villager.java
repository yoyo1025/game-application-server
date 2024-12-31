package com.example.game_application_server.domain.entity;

public class Villager extends Player{

    // 村人が所持するポイント。初期値はゼロ。
    public int points;

    // 生死情報を表す。初期値はtrueで鬼に捕まるとfalseになる。
    public boolean isAlive;

    public Villager(int id, int userId, String name, boolean isConnected, boolean isOnBreak) {
        super(id, userId, name, isConnected, isOnBreak);
        this.points = 0;
        this.isAlive = true;
    }

    public int getPoints() {
        return this.points;
    }

    public void addPoints() {
        this.points += 1;
    }

    public boolean isAlive() {
        return this.isAlive;
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    @Override
    public String toString() {
        return "Villager { id = " + super.id + ", userId " + super.userId + ", name = " + super.name +
                ", isConnected = " + super.isConnected + ", isOnBreak = " + super.isOnBreak +
                ", points = " + this.points + ", isAlive = " + this.isAlive + " }";
    }

}
