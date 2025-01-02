package com.example.game_application_server.dto;

import com.example.game_application_server.domain.entity.Position;

public class MoveRequestDTO {
    public int userId;
    public int currentPlayerIndex;
    public Position targetPosition;

    // GetterとSetter
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public Position getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(Position targetPosition) {
        this.targetPosition = targetPosition;
    }
}
