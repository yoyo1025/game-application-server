package com.example.game_application_server.infrastructure.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;  // JPAアノテーションを使用する
import jakarta.persistence.Column;
import jakarta.persistence.Table;


import java.time.LocalDateTime;

/**
 * DBの`battlerecord`テーブルに対応するJPAエンティティクラス
 */
@Entity
@Table(name = "battlerecord")
public class BattleRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 自動生成されるID

    @Column(name = "userid", nullable = false)
    private Integer userId;  // ユーザーID

    @Column(name = "user_name", nullable = false)
    private String userName;  // ユーザー名

    @Column(name = "Role", nullable = false)
    private String role;  // 役割 ("Villager" or "Demon")

    @Column(name = "is_win", nullable = false)
    private boolean isWin;  // 勝利フラグ (true: 勝利, false: 敗北)

    @Column(name = "Point", nullable = false)
    private int point;  // 得点

    @Column(name = "Ranking", nullable = false)
    private int ranking;  // 順位

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;  // 作成日時

    // --- コンストラクタ ---
    public BattleRecordEntity() {
        // デフォルトコンストラクタ（JPAで使用）
    }

    public BattleRecordEntity(
            Integer userId,
            String userName,
            String role,
            boolean isWin,
            int point,
            int ranking,
            LocalDateTime createdAt
    ) {
        this.userId = userId;
        this.userName = userName;
        this.role = role;
        this.isWin = isWin;
        this.point = point;
        this.ranking = ranking;
        this.createdAt = createdAt;
    }

    // --- Getter ---
    public Long getId() {
        return id;
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

    // --- Setter ---
    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setWin(boolean win) {
        isWin = win;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // --- toString (オブジェクトの内容を文字列化) ---
    @Override
    public String toString() {
        return "BattleRecordEntity{" +
                "id=" + id +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", role='" + role + '\'' +
                ", isWin=" + isWin +
                ", point=" + point +
                ", ranking=" + ranking +
                ", createdAt=" + createdAt +
                '}';
    }
}
