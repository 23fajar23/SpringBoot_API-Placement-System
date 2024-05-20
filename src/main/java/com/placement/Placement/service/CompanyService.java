package com.placement.Placement.service;

import com.placement.Placement.model.request.CompanyRequest;
import com.placement.Placement.model.response.CompanyResponse;
import org.springframework.http.ResponseEntity;

public interface CompanyService {
    ResponseEntity<Object> getAll();
    ResponseEntity<Object> getById(String id);
    CompanyResponse findById(String id);
    ResponseEntity<Object> create(CompanyRequest request);
    CompanyResponse update(CompanyRequest request);
    CompanyResponse remove(String id);
}
