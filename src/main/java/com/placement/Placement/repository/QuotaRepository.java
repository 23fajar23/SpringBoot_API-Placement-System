package com.placement.Placement.repository;

import com.placement.Placement.model.entity.Quota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuotaRepository extends JpaRepository<Quota, String> {
    @Query("SELECT q FROM Quota q WHERE q.stage = :stageId")
    Optional<Quota> findByStageId(@Param("stageId") String id);
}
