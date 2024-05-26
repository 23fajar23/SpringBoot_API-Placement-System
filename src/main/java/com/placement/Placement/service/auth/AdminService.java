package com.placement.Placement.service.auth;

import com.placement.Placement.model.request.AdminRequest;
import com.placement.Placement.model.response.AdminResponse;
import org.springframework.http.ResponseEntity;

public interface AdminService {
    ResponseEntity<Object> getAll();
    ResponseEntity<Object> getById(String id);
    AdminResponse findById(String id);
    AdminResponse save(AdminResponse adminResponse);
    ResponseEntity<Object> update(AdminRequest adminRequest);
    ResponseEntity<Object> remove(String id);
    ResponseEntity<Object> getAllByName(String name, Integer page, Integer size);
    AdminResponse findByUserCredentialId(String userCredential);
}