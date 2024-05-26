package com.placement.Placement.repository.auth;

import com.placement.Placement.model.entity.auth.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin,String>, JpaSpecificationExecutor<Admin> {
    @Query("SELECT a FROM Admin a WHERE a.userCredential.id = :userCredential")
    Optional<Admin> findByUserCredentialId(@Param("userCredential") String userCredential);
}