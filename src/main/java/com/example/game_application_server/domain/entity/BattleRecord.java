package com.example.game_application_server.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class BattleRecord {
    @JsonProperty("user_id")
    private final Integer userId;

    @JsonProperty("user_name")
    private final String userName;

    @JsonProperty("role")
    private final String role;

    @JsonProperty("is_win")
    private final boolean isWin;

    @JsonProperty("point")
    private final int point;

    @JsonProperty("ranking")
    private final int ranking;

    // ローカル日時のフィールド（ISO形式で表示）
    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime createdAt;

    public BattleRecord(Integer userId, String userName, String role, boolean isWin, int point, int ranking) {
        this.userId = userId;
        this.userName = userName;
        this.role = role;
        this.isWin = isWin;
        this.point = point;
        this.ranking = ranking;
        this.createdAt = LocalDateTime.now(); // 生成時刻
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
