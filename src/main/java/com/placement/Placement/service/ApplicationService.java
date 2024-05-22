package com.placement.Placement.service;

import com.placement.Placement.model.request.ApplicationRequest;
import org.springframework.http.ResponseEntity;

public interface ApplicationService {
    ResponseEntity<Object> getAll();
    ResponseEntity<Object> getById(String id);
    ResponseEntity<Object> create(ApplicationRequest applicationRequest);
    ResponseEntity<Object> update(ApplicationRequest applicationRequest);
}
