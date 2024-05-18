package com.placement.Placement.repository.auth;

import com.placement.Placement.constant.ERole;
import com.placement.Placement.model.entity.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,String> {
    Optional<Role> findByName(ERole name);
}

