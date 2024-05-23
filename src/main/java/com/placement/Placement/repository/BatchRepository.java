package com.placement.Placement.repository;

import com.placement.Placement.constant.ERegion;
import com.placement.Placement.model.entity.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BatchRepository extends JpaRepository<Batch, String> {
    @Query("SELECT b FROM Batch b WHERE b.name = :name AND b.region = :region")
    Optional<Batch> findByNameAndRegion(@Param("name") String name, @Param("region") ERegion region);
}
