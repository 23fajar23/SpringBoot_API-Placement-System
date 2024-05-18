package com.placement.Placement.repository.auth;

import com.placement.Placement.model.entity.auth.SuperAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuperAdminRepository extends JpaRepository<SuperAdmin,String> {
}
