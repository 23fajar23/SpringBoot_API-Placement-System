package com.placement.Placement.repository;

import com.placement.Placement.model.entity.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EducationRepository extends JpaRepository<Education, String> {
    @Query("SELECT edu FROM Education edu WHERE edu.education = :education")
    Optional<Education> findByName(@Param("education") String education);
}
