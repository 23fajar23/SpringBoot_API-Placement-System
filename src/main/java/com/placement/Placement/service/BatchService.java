package com.placement.Placement.service;

import com.placement.Placement.model.entity.Batch;
import com.placement.Placement.model.request.BatchRequest;
import com.placement.Placement.model.response.BatchResponse;

import java.util.List;

public interface BatchService {
    List<BatchResponse> getAll();
    BatchResponse getById(String id);
    BatchResponse create(BatchRequest batchRequest);
    BatchResponse update(BatchRequest batchRequest);
    BatchResponse remove(String id);
    BatchResponse getByName(String name);
}
