package com.placement.Placement.service;

import com.placement.Placement.model.request.StageRequest;
import com.placement.Placement.model.response.StageResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface StageService {
    ResponseEntity<Object> getById(String id);
    ResponseEntity<Object> create(StageRequest stageRequest);
    ResponseEntity<Object> update(StageRequest stageRequest);
}
