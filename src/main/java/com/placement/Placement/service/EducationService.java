package com.placement.Placement.service;

import com.placement.Placement.model.request.EducationRequest;
import com.placement.Placement.model.response.EducationResponse;

import java.util.List;

public interface EducationService {
    List<EducationResponse> getAll();
    EducationResponse getById(String id);
    EducationResponse create(EducationRequest educationRequest);
    EducationResponse update(EducationRequest educationRequest);
    EducationResponse remove(String id);
}
