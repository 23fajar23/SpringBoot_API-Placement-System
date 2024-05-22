package com.placement.Placement.repository;

import com.placement.Placement.model.entity.TestStageResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestStageResultRepository extends JpaRepository<TestStageResult, String> {
}
