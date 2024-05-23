package com.placement.Placement.service;

import com.placement.Placement.model.request.BatchRequest;
import com.placement.Placement.model.response.BatchResponse;
import org.springframework.http.ResponseEntity;

public interface BatchService {
    ResponseEntity<Object> getAll();
    ResponseEntity<Object> getById(String id);
    BatchResponse findById(String id);
    ResponseEntity<Object> create(BatchRequest batchRequest);
    ResponseEntity<Object> update(BatchRequest batchRequest);
    ResponseEntity<Object> remove(String id);
}
