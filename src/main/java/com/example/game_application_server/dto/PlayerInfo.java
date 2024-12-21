package com.example.game_application_server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerInfo {
    @JsonProperty("userId")
    private int userId;
    @JsonProperty("userName")
    private String userName;

    @JsonProperty("isDemon") // JSONのキー名を「isDemon」に明示的に指定
    private boolean isDemon;

    // Getter and Setter for userId
    @JsonProperty("userId")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    // Getter and Setter for userName
    @JsonProperty("userName")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    // Getter for isDemon
    @JsonProperty("isDemon")  // JSONのシリアライズ/デシリアライズ時に「isDemon」を使用
    public boolean isDemon() {
        return isDemon;
    }

    // Setter for isDemon
    public void setDemon(boolean isDemon) {
        this.isDemon = isDemon;
    }

    @Override
    public String toString() {
        return "PlayerInfo{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", isDemon=" + isDemon +
                '}';
    }
}
