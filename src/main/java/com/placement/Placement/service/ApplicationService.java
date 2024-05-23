package com.placement.Placement.service;

import com.placement.Placement.model.request.ApplicationRequest;
import com.placement.Placement.model.request.ApproveTestRequest;
import org.springframework.http.ResponseEntity;

public interface ApplicationService {
    ResponseEntity<Object> getAll();
    ResponseEntity<Object> getById(String id);
    ResponseEntity<Object> create(ApplicationRequest applicationRequest);
    ResponseEntity<Object> approve(ApproveTestRequest approveTestRequest);
}
