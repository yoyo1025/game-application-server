package com.example.game_application_server.dto;

import com.example.game_application_server.domain.entity.*;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class GameStateDTO {
    @JsonProperty("players")
    public List<Player> players;

    @JsonProperty("field")
    public Field field;

    @JsonProperty("turn")
    public Turn turn;

    @JsonProperty("playerPositions")
    public Map<Integer, Position> playerPositions; // 修正：キーをPlayerのidに変更

    public GameStateDTO(List<Player> players, Field field, Turn turn, Map<Integer, Position> playerPositions) {
        this.players = players;
        this.field = field;
        this.turn = turn;
        this.playerPositions = playerPositions;
    }
}
