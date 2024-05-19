package com.placement.Placement.service;

import com.placement.Placement.model.request.StageRequest;
import com.placement.Placement.model.response.StageResponse;

import java.util.List;

public interface StageService {
    List<StageResponse> getAll();
    StageResponse getById(String id);
    StageResponse create(StageRequest stageRequest);
    StageResponse update(StageRequest stageRequest);
    StageResponse remove(String id);
}
