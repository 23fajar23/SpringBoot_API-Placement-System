package com.placement.Placement.repository.auth;

import com.placement.Placement.model.entity.auth.SuperAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SuperAdminRepository extends JpaRepository<SuperAdmin,String> {
    @Query("SELECT sa FROM SuperAdmin sa WHERE sa.userCredential.id = :userCredential")
    Optional<SuperAdmin> findByUserCredentialId(@Param("userCredential") String userCredential);
}
