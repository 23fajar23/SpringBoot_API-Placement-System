package com.placement.Placement.repository;

import com.placement.Placement.model.entity.TestStageResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestStageResultRepository extends JpaRepository<TestStageResult, String> {
    @Query("SELECT tsr FROM TestStageResult tsr WHERE tsr.application.id = :applicationId AND tsr.stage.id = :stageId")
    Optional<TestStageResult> findByCustomerTestAndStage(@Param("applicationId") String applicationId, @Param("stageId") String stageId);
}
