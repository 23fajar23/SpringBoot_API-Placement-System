package com.placement.Placement.service;

import com.placement.Placement.model.request.EducationRequest;
import com.placement.Placement.model.response.EducationResponse;
import org.springframework.http.ResponseEntity;

public interface EducationService {
    ResponseEntity<Object> getAll();
    EducationResponse findById(String id);
    ResponseEntity<Object> getById(String id);
    ResponseEntity<Object> create(EducationRequest educationRequest);
    ResponseEntity<Object> update(EducationRequest educationRequest);
    ResponseEntity<Object> remove(String id);
}
