package com.placement.Placement.service.auth;

import com.placement.Placement.model.entity.auth.SuperAdmin;
import com.placement.Placement.model.request.SuperAdminRequest;
import com.placement.Placement.model.response.SuperAdminResponse;
import org.springframework.http.ResponseEntity;

public interface SuperAdminService {
    ResponseEntity<Object> getAll();
    ResponseEntity<Object> getById(String id);
    SuperAdmin save(SuperAdmin superAdmin);
    ResponseEntity<Object> update(SuperAdminRequest superAdminRequest);
    ResponseEntity<Object> remove(String id);
    SuperAdminResponse findByUserCredentialId(String userCredential);
}
