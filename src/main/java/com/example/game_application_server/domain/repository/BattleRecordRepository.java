package com.example.game_application_server.domain.repository;

import com.example.game_application_server.domain.entity.BattleRecord;

import java.util.List;

public interface BattleRecordRepository {
    void saveAll(List<BattleRecord> records);
}