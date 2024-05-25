package com.placement.Placement.service;

import com.placement.Placement.model.request.TestRequest;
import com.placement.Placement.model.response.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TestService {
    ResponseEntity<Object> getAll();
    ResponseEntity<Object> getById(String id);
    ResponseEntity<Object> create(TestRequest testRequest);
    ResponseEntity<Object> update(TestRequest testRequest);
    ResponseEntity<Object> remove(String id);
    ResponseEntity<Object> getAllByPlacementAndRole(String placement, String rolePlacement, Integer page, Integer size);
}
