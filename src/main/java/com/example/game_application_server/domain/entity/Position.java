package com.example.game_application_server.domain.entity;

public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Position getPosition() {
        return new Position(this.x, this.y);
    }

    @Override
    public String toString() {
        return "Position{" + "x=" + x + ", y=" + y + '}';
    }
}
