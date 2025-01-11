package com.example.game_application_server.application;

import com.example.game_application_server.domain.entity.Player;
import com.example.game_application_server.domain.entity.Villager;
import com.example.game_application_server.domain.service.GameState;
import org.springframework.stereotype.Service;


@Service
public class SkipTurnUsecase {
    public GameStateManager gameStateManager;

    public SkipTurnUsecase(GameStateManager gameStateManager){
        this.gameStateManager=gameStateManager;
    }

    public GameState excute(int targetId){
        GameState gameState = gameStateManager.getGameState();

        Player targetPlayer = gameState.players.get(0);
        for (int i = 0; i < 4; i++){
            if ( targetId == gameState.players.get(i).userId) {
                targetPlayer = gameState.players.get(i);
            }
        }

        //重複チェック
        if (targetPlayer.isOnBreak) {
            throw new IllegalStateException("既に一回休み状態です。");
        }
        System.out.println("target player: "+targetPlayer.getName());
        targetPlayer.setOnBreak(true);
        gameState.turn.nextPlayerIndex();

        // 死亡していない&休憩中でないプレイヤーを次のターンのプレイヤーとして選出する
        int skipCount = 0;
        int maxPlayers = gameState.players.size();
        while (skipCount < maxPlayers) {
            // ---- ここからが「死んだプレイヤーのターンをスキップ」する処理 ----
            Player nextPlayer = gameState.players.get(gameState.turn.getCurrentPlayerIndex() - 1);
            // 次のプレイヤーが村人 かつ その村人が死んでいる場合はそのプレイヤーのターンをスキップ
            if (nextPlayer instanceof Villager && !((Villager) nextPlayer).isAlive()) {
                gameState.turn.nextPlayerIndex();
                skipCount++;
            } else if (nextPlayer.isOnBreak) {
                System.out.println(nextPlayer.getName() + " is currently on break, skipping...");
                nextPlayer.setOnBreak(false);  // 次のターンではプレイできるようにフラグをリセット
                gameState.turn.nextPlayerIndex();
                skipCount++;
            } else {
                // 生きている村人 or 鬼ならターンを確定
                break;
            }
        }

        System.out.println(targetPlayer.getName()+"'s next turn is skipped");
        return gameState;
    }
}
