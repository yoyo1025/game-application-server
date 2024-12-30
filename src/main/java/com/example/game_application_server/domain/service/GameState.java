package com.example.game_application_server.domain.service;

import com.example.game_application_server.domain.entity.*;

import java.util.*;

public class GameState {
    public List<Player> players; // 各プレイヤーのリスト

    public Field field; // フィールド

    public Turn turn; // ターン

    public Map<Player, Position> playerPositions; // プレイヤーの位置を表す

    public boolean isGameFinished; // ゲームが終了したかのフラグ

    public GameState() {
        // フィールド初期化
        this.players = new ArrayList<>();
        this.playerPositions = new HashMap<>();

        // プレイヤー初期化
        Villager villager1 = new Villager(1, "player1", true, false);
        Villager villager2 = new Villager(2, "player2", true, false);
        Villager villager3 = new Villager(3, "player3", true, false);
        Demon demon = new Demon(4, "player4", true, false);
        this.players.add(villager1);
        this.players.add(villager2);
        this.players.add(villager3);
        this.players.add(demon);

        // フィールド初期化
        List<Position> eventPositions = new ArrayList<>();
        eventPositions.add(new Position(1, 1));
        eventPositions.add(new Position(7, 1));
        eventPositions.add(new Position(1, 7));
        eventPositions.add(new Position(7, 7));
        this.field = new Field(9, eventPositions);

        // ターン初期化
        this.turn = new Turn(15, 1);

        // プレイヤーの位置を初期化
        Position villager1FirstPosition = new Position(2, 2);
        Position villager2FirstPosition = new Position(6, 2);
        Position villager3FirstPosition = new Position(6, 6);
        Position demonFirstPosition = new Position(2, 6);
        this.playerPositions.put(villager1, villager1FirstPosition);
        this.playerPositions.put(villager2, villager2FirstPosition);
        this.playerPositions.put(villager3, villager3FirstPosition);
        this.playerPositions.put(demon, demonFirstPosition);

        // ゲーム終了フラグの初期化
        this.isGameFinished = false;
    }

    // プレイヤーの位置を設定する
    public void setPlayerPosition(Player player, Position position) {
        this.playerPositions.put(player, position);
    }

    // 引数で渡したプレイヤーの位置を返す
    public Position getPlayerPosition(Player player) {
        return this.playerPositions.get(player);
    }

    // 移動できるマスを返す
    public List<Position> calculatePossibleMoves(Player currentPlayer, List<Player> players, Map<Player, Position> playerPositions , int steps, int fieldSize) {
        Position currentPlayerPosition = playerPositions.get(currentPlayer);
        if (currentPlayerPosition == null) {
            throw new IllegalArgumentException("Position not found");
        }

        // Setを用いることで座標の重複を排除する
        Set<Position> possiblePositions = new HashSet<>();
        List<Position> occupiedPositions = new ArrayList<>(playerPositions.values());

        for (int dx = 0; dx <= steps; dx++) {
            for (int dy = 0; dy <= steps - dx; dy++) {
                // 現在地は除外
                if (dx == 0 && dy == 0) {
                    continue;
                }

                int x = currentPlayerPosition.getX();
                int y = currentPlayerPosition.getY();

                // 現在地から見た第一象限 (x+dx, y+dy)
                addPositionIfValid(possiblePositions, occupiedPositions, x + dx, y + dy, fieldSize);

                // dx,dyが0でない場合のみ対称位置を計算
                // 第2象限 (x-dx, y+dy)
                if (dx != 0) {
                    addPositionIfValid(possiblePositions, occupiedPositions, x - dx, y + dy, fieldSize);
                }
                // 第3象限 (x-dx, y-dy)
                if (dx != 0 && dy != 0) {
                    addPositionIfValid(possiblePositions, occupiedPositions, x - dx, y - dy, fieldSize);
                }
                // 第4象限 (x+dx, y-dy)
                if (dy != 0) {
                    addPositionIfValid(possiblePositions, occupiedPositions, x + dx, y - dy, fieldSize);
                }
            }
        }
        // SetをListに変換して返す
        return new ArrayList<>(possiblePositions);
    }

    public void addPositionIfValid(Set<Position> possiblePositions, List<Position> occupiedPositions, int x, int y, int fieldSize) {
        if (x >= 0 && x < fieldSize && y >= 0 && y < fieldSize) {
            Position newPosition = new Position(x, y);
            if (!occupiedPositions.contains(newPosition)) {
                possiblePositions.add(newPosition);
            }
        }
    }

}
