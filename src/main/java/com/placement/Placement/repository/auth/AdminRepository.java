package com.placement.Placement.repository.auth;

import com.placement.Placement.model.entity.auth.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin,String> {

}