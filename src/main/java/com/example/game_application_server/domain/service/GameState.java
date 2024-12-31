package com.example.game_application_server.domain.service;

import com.example.game_application_server.domain.entity.*;
import com.example.game_application_server.dto.GameStateDTO;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GameState {
    public List<Player> players; // 各プレイヤーのリスト

    public Field field; // フィールド

    public Turn turn; // ターン

    public Map<Player, Position> playerPositions; // プレイヤーの位置を表す

    public boolean isGameFinished; // ゲームが終了したかのフラグ

    public GameState(
            int user1Id,
            int user2Id,
            int user3Id,
            int user4Id,
            String player1Name,
            String player2Name,
            String player3Name,
            String player4Name)
    {
        // フィールド初期化
        this.players = new ArrayList<>();
        this.playerPositions = new HashMap<>();

        // プレイヤー初期化
        Villager villager1 = new Villager(1, user1Id, player1Name, true, false);
        Villager villager2 = new Villager(2, user2Id, player2Name, true, false);
        Villager villager3 = new Villager(3, user3Id, player3Name, true, false);
        Demon demon = new Demon(4, user4Id, player4Name, true, false);
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
    public List<Position> calculatePossibleMoves(Player currentPlayer, Map<Player, Position> playerPositions , int steps, int fieldSize) {
        Position currentPlayerPosition = playerPositions.get(currentPlayer);
        if (currentPlayerPosition == null) {
            throw new IllegalArgumentException("Position not found for current player");
        }

        // 現在のプレイヤーが鬼かどうかを判定
        boolean isDemon = (currentPlayer instanceof Demon);

        //　Setを用いることで座標の重複を排除する
        Set<Position> possiblePositions = new HashSet<>();

        // occupiedPositionsの作り方を分岐
        // 鬼以外の場合のみ「他プレイヤーの位置」を埋める（移動できないようにする）
        Set<Position> occupiedPositions = new HashSet<>();
        if (!isDemon) {
            // 全てのプレイヤーとその現在位置を順番に確認する
            // entrySet()でMap内のすべてのキー（プレイヤー）と値（位置）をセットとして取得する
            for (Map.Entry<Player, Position> entry : playerPositions.entrySet()) {
                Player otherPlayer = entry.getKey();
                // 自分自身は除外（同じマスにいても問題ないので）
                if (!otherPlayer.equals(currentPlayer)) {
                    occupiedPositions.add(entry.getValue());
                }
            }
        }

        // stepsの範囲内ですべての(dx, dy)を試し、4象限を考慮
        for (int dx = 0; dx <= steps; dx++) {
            for (int dy = 0; dy <= steps - dx; dy++) {
                // 現在地は除外
                if (dx == 0 && dy == 0) {
                    continue;
                }

                int x = currentPlayerPosition.getX();
                int y = currentPlayerPosition.getY();

                // 第一象限 (x+dx, y+dy)
                addPositionIfValid(possiblePositions, occupiedPositions, x + dx, y + dy, fieldSize);

                // dxが0でなければ第二象限 (x-dx, y+dy)
                if (dx != 0) {
                    addPositionIfValid(possiblePositions, occupiedPositions, x - dx, y + dy, fieldSize);
                }
                // dx, dyともに0でなければ第三象限 (x-dx, y-dy)
                if (dx != 0 && dy != 0) {
                    addPositionIfValid(possiblePositions, occupiedPositions, x - dx, y - dy, fieldSize);
                }
                // dyが0でなければ第四象限 (x+dx, y-dy)
                if (dy != 0) {
                    addPositionIfValid(possiblePositions, occupiedPositions, x + dx, y - dy, fieldSize);
                }
            }
        }
        // SetをListに変換して返す
        return new ArrayList<>(possiblePositions);
    }

    public void addPositionIfValid(Set<Position> possiblePositions, Set<Position> occupiedPositions, int x, int y, int fieldSize) {
        if (x >= 0 && x < fieldSize && y >= 0 && y < fieldSize) {
            Position newPosition = new Position(x, y);
            if (!occupiedPositions.contains(newPosition)) {
                possiblePositions.add(newPosition);
            }
        }
    }

    public int getNumberOfAlivePlayers() {
        int count = 0;
        for (Player player : players) {
            if (player instanceof Villager) { // 村人であるかを確認
                Villager villager = (Villager) player;
                if (villager.isAlive()) { // 生存しているかを確認
                    count++;
                }
            }
        }
        return count;
    }
    public GameStateDTO toDTO() {
        // プレイヤー位置情報の変換
        Map<Integer, Position> simplifiedPlayerPositions = new HashMap<>();
        playerPositions.forEach((player, position) -> {
            simplifiedPlayerPositions.put(player.getId(), position); // Playerのidをキーとして利用
        });

        // DTOに変換
        return new GameStateDTO(players, field, turn, simplifiedPlayerPositions);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // プレイヤー情報
        sb.append("Players:\n");
        for (Player player : players) {
            sb.append("  ").append(player.toString()).append("\n");
        }

        // フィールド情報
        sb.append("Field:\n");
        sb.append("  Size: ").append(field.getSize()).append("\n");
        sb.append("  Event Positions: ").append(field.getEventPositions()).append("\n");

        // ターン情報
        sb.append("Turn:\n");
        sb.append("  Current Turn: ").append(turn.getCurrentTurn()).append("\n");
        sb.append("  Total Turns: ").append(turn.getMaxTurn()).append("\n");

        // プレイヤー位置情報
        sb.append("Player Positions:\n");
        for (Map.Entry<Player, Position> entry : playerPositions.entrySet()) {
            sb.append("  ").append(entry.getKey().getName())
                    .append(" -> ").append(entry.getValue().toString()).append("\n");
        }

        // ゲーム終了フラグ
        sb.append("Game Finished: ").append(isGameFinished).append("\n");

        return sb.toString();
    }
}
