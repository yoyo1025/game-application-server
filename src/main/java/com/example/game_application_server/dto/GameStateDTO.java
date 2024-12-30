package com.example.game_application_server.dto;

import com.example.game_application_server.domain.entity.Field;
import com.example.game_application_server.domain.entity.Player;
import com.example.game_application_server.domain.entity.Position;
import com.example.game_application_server.domain.entity.Turn;

import java.util.List;
import java.util.Map;

public class GameStateDTO {
    public List<Player> players;

    public Field field;

    public Turn turn;

    public Map<Player, Position> playerPositions;

    public GameStateDTO(List<Player> players, Field field, Turn turn, Map<Player, Position> playerPositions){
        this.players = players;
        this.field = field;
        this.turn = turn;
        this.playerPositions = playerPositions;
    }
}
