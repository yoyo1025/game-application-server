package com.example.game_application_server.domain.entity;

public class Square {
    public Position position; // 位置情報
    public Event event;       // スクエアに関連付けられたイベント

    // コンストラクタ
    public Square(Position position, Event event) {
        this.position = position;
        this.event = event;
    }

    // 位置情報を取得
    public Position getPosition() {
        return this.position;
    }

    // イベントを取得
    public Event getEvent() {
        return this.event;
    }

    // イベントを設定
    public void setEvent(Event event) {
        this.event = event;
    }

    // イベントが関連付けられているかを確認
    public boolean hasEvent() {
        return this.event != null;
    }

    @Override
    public String toString() {
        return "Square{" +
                "position=" + position +
                ", event=" + (event != null ? event.toString() : "No Event") +
                '}';
    }
}
