package com.example.game_application_server.infrastructure;

import com.example.game_application_server.infrastructure.jpa.BattleRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BattleRecordJpaRepository extends JpaRepository<BattleRecordEntity, Long> {
    // ここにクエリメソッドを必要に応じて追加
}
