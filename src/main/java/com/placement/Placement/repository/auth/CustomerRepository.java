package com.placement.Placement.repository.auth;

import com.placement.Placement.model.entity.auth.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,String>, JpaSpecificationExecutor<Customer> {
    @Query("SELECT c FROM Customer c WHERE c.userCredential.id = :userCredential")
    Optional<Customer> findByUserCredentialId(@Param("userCredential") String userCredential);
}