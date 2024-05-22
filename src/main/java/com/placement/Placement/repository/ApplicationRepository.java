package com.placement.Placement.repository;

import com.placement.Placement.model.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, String> {
    @Query("SELECT a FROM Application a WHERE a.test.id = :testId AND a.customer.id = :customerId")
    Optional<Application> findByTestAndCustomer(@Param("testId") String testId, @Param("customerId") String customerId);
}
