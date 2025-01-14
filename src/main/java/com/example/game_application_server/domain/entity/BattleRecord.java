package com.example.game_application_server.domain.entity;

import java.time.LocalDateTime;

public class BattleRecord {
    private final Integer userId;
    private final String userName;
    private final String role;     // "Villager" or "Demon"
    private final boolean isWin;   // 勝利かどうか
    private final int point;
    private final int ranking;
    private final LocalDateTime createdAt;

    public BattleRecord(Integer userId, String userName, String role, boolean isWin, int point, int ranking) {
        this.userId = userId;
        this.userName = userName;
        this.role = role;
        this.isWin = isWin;
        this.point = point;
        this.ranking = ranking;
        this.createdAt = LocalDateTime.now(); // 生成時刻を設定
    }

    public Integer getUserId() {
        return userId;
    }
    public String getUserName() {
        return userName;
    }
    public String getRole() {
        return role;
    }
    public boolean isWin() {
        return isWin;
    }
    public int getPoint() {
        return point;
    }
    public int getRanking() {
        return ranking;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
