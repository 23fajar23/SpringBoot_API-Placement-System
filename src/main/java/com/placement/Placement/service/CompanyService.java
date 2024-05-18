package com.placement.Placement.service;

import com.placement.Placement.model.request.CompanyRequest;
import com.placement.Placement.model.response.CompanyResponse;

import java.util.List;

public interface CompanyService {
    List<CompanyResponse> getAll();
    CompanyResponse getById(String id);
    CompanyResponse create(CompanyRequest request);
    CompanyResponse update(CompanyRequest request);
    CompanyResponse remove(String id);
}
