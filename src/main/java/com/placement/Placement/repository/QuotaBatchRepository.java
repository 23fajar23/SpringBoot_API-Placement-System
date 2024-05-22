package com.placement.Placement.repository;

import com.placement.Placement.model.entity.QuotaBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuotaBatchRepository extends JpaRepository<QuotaBatch, String> {
    @Query("SELECT qb FROM QuotaBatch qb WHERE qb.batch.id = :batchId AND qb.quota.id = :quotaId")
    Optional<QuotaBatch> findByBatchId(@Param("batchId") String batchId, @Param("quotaId") String quotaId);
}
