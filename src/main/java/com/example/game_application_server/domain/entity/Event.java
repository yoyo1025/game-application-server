package com.example.game_application_server.domain.entity;

import java.util.*;

public class Event {
    public EventKind kind;
    public Dice dice;

    // デフォルトコンストラクタ
    public Event() {
        this.kind = EventKind.ROLL_AGAIN; // デフォルト値を設定
    }

    // 引数付きコンストラクタ
    public Event(EventKind kind) {
        this.kind = kind;
    }

    // Getter
    public EventKind getKind() {
        return kind;
    }

    // Setter
    public void setKind(EventKind kind) {
        this.kind = kind;
    }

    /**
     * ROLL_AGAINイベントの場合は移動可能なPositionリストを返す。
     * 他のイベント（SKIP_TURN、WARP）の場合は値の変更処理を行い、voidを返す。
     *
     * @param playerPositionMap 全プレイヤーの位置を管理するマップ
     * @param players 全プレイヤーのリスト
     * @param eventPlayerId イベントを発生させたプレイヤーID
     * @param targetPlayerId SKIP_TURNやWARPでイベントの標的になるプレイヤーID
     * @return ROLL_AGAINの場合は移動可能なPositionリスト、それ以外はvoid
     */
    public List<Position> executeEvent(Map<Player, Position> playerPositionMap, List<Player> players, int eventPlayerId, int targetPlayerId) {
        // Idを0ベースに変換する
        int eventPlayerIndex = eventPlayerId - 1;
        int targetPlayerIndex = targetPlayerId - 1;

        if (this.kind == EventKind.ROLL_AGAIN) {
            return executeRollAgain(playerPositionMap, players, eventPlayerIndex);
        }

        // ROLL_AGAIN以外は値の変更処理
        switch (this.kind) {
            case SKIP_TURN -> executeSkipTurn(players, targetPlayerIndex);
            case WARP -> executeWarp(playerPositionMap, players, eventPlayerIndex, targetPlayerIndex);
            default -> throw new IllegalArgumentException("Unknown EventKind: " + this.kind);
        }

        // SKIP_TURN, WARPの場合はvoidとして終了
        return Collections.emptyList();
    }

    public List<Position> executeRollAgain(Map<Player, Position> playerPositionMap, List<Player> players, int eventPlayerIndex) {
        int diceResult = dice.roll();

        Player eventPlayer = players.get(eventPlayerIndex);
        Position currentPosition = playerPositionMap.get(eventPlayer);
        if (currentPosition == null) {
            throw new IllegalArgumentException("Position not found for player with ID: " + (eventPlayerIndex + 1));
        }

        // Setを用いることで座標の重複を排除する
        Set<Position> possiblePositions = new HashSet<>();
        List<Position> occupiedPositions = new ArrayList<>(playerPositionMap.values());
        int fieldSize = 8;

        for (int dx = 0; dx <= diceResult; dx++) {
            for (int dy = 0; dy <= diceResult - dx; dy++) {
                // 現在位置は除外
                if (dx == 0 && dy == 0) {
                    continue;
                }

                int x = currentPosition.getX();
                int y = currentPosition.getY();

                // 第1象限 (x+dx, y+dy)
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

    public void executeSkipTurn(List<Player> players, int targetPlayerIndex) {
        players.get(targetPlayerIndex).isOnBreak = true;
    }

    public void executeWarp(Map<Player, Position> playerPositionMap, List<Player> players, int eventPlayerIndex , int targetPlayerIndex) {
        // イベントプレイヤーとターゲットプレイヤーの位置を取得
        Player eventPlayer = players.get(eventPlayerIndex);
        Player targetPlayer = players.get(targetPlayerIndex);

        // イベントプレイヤーとターゲットプレイヤーの現在地を取得
        Position eventPlayerPosition = playerPositionMap.get(eventPlayer);
        Position targetPlayerPosition = playerPositionMap.get(targetPlayer);

        if (eventPlayerPosition == null || targetPlayerPosition == null) {
            throw new IllegalArgumentException("Position not found for one or both players.");
        }

        // 位置を入れ替える
        playerPositionMap.put(eventPlayer, targetPlayerPosition);
        playerPositionMap.put(targetPlayer, eventPlayerPosition);
    }

    @Override
    public String toString() {
        return "Event{kind=" + kind + "}";
    }
}
