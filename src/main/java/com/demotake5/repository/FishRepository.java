package com.demotake5.repository;

import com.demotake5.entity.Fish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface FishRepository extends JpaRepository<Fish, Long> {
    List<Fish> findByTankId(Long tankId);

    @Query("SELECT t.id AS tankId, SUM(f.quantity) AS totalFish FROM Fish f JOIN f.tank t GROUP BY t.id")
    List<Map<String, Object>> countFishPerTank();
    @Query("SELECT COALESCE(SUM(f.quantity), 0) FROM Fish f WHERE f.tank.id = :tankId")
    int getTotalFishQuantityByTankId(@Param("tankId") Long tankId);
    }