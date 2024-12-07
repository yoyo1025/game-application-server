package com.example.game_application_server.domain.entity;

public class Demon extends Player{

    public Demon(int id, String name, boolean isConnected, boolean isOnBreak) {
        super(id, name, isConnected, isOnBreak);
    }

    public void capture(Villager villager) {
        villager.setAlive(false);
    }

    @Override
    public String toString() {
        return "Demon { id = " + super.id + ", name = " + super.name +
                ", isConnected = " + super.isConnected + ", isOnBreak = " + super.isOnBreak + " }";
    }
}
