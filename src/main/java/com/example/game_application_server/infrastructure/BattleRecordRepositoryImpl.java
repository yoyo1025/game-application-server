package com.example.game_application_server.infrastructure;

import com.example.game_application_server.domain.entity.BattleRecord;
import com.example.game_application_server.domain.repository.BattleRecordRepository;
import com.example.game_application_server.infrastructure.jpa.BattleRecordEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BattleRecordRepositoryImpl implements BattleRecordRepository {

    private final BattleRecordJpaRepository jpaRepository;

    public BattleRecordRepositoryImpl(BattleRecordJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void saveAll(List<BattleRecord> records) {
        // ドメインのBattleRecord -> JPAエンティティへ変換
        List<BattleRecordEntity> entities = records.stream()
                .map(r -> new BattleRecordEntity(
                        r.getUserId(),
                        r.getUserName(),
                        r.getRole(),
                        r.isWin(),
                        r.getPoint(),
                        r.getRanking(),
                        r.getCreatedAt()
                ))
                .toList();

        jpaRepository.saveAll(entities);
    }
}
