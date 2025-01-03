package com.example.game_application_server.application;

import com.example.game_application_server.domain.entity.Player;
import com.example.game_application_server.domain.entity.Position;
import com.example.game_application_server.domain.service.GameState;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class ChangeUserPositionUsecase {
    public GameStateManager gameStateManager;

    public ChangeUserPositionUsecase(GameStateManager gameStateManager){this.gameStateManager=gameStateManager;}

    public GameState excute(Position targetPosition, int userId){
        GameState gameState = gameStateManager.getGameState();

        Player currentPlayer = gameState.players.get(gameState.turn.getCurrentPlayerIndex() - 1);

        //ターンチェック
        if (currentPlayer.getUserId() != userId) {
            throw new IllegalStateException("あなたのターンではありません");
        }

        //選択位置が他のプレイヤーと重複しているかを確認
        Optional<Map.Entry<Player, Position>> changedpositionEntry = gameState.playerPositions.entrySet().stream()
                .filter(entry -> entry.getValue().equals(targetPosition))
                .findFirst();

        changedpositionEntry.ifPresentOrElse(entry -> {
            Player changedPlayer = entry.getKey();
            System.out.println("Changed player: "+changedPlayer.getName());

            //位置の入れ替えを実施
            Position tmpPosition=gameState.getPlayerPosition(changedPlayer);
            gameState.setPlayerPosition(changedPlayer,gameState.getPlayerPosition(currentPlayer));
            gameState.setPlayerPosition(currentPlayer,tmpPosition);
            System.out.println("ChangePosition is done. "+currentPlayer.getName()+" & "+changedPlayer.getName());
        },() -> {throw new IllegalStateException("Select Unknown UserPosition");});//不明なポジションを入力する例外(フロント側で出来ないように制御するから起きない、、はず？)

        return gameState;
    }
}
