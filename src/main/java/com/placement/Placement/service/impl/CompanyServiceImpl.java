package com.placement.Placement.service.impl;

import com.placement.Placement.constant.EStatus;
import com.placement.Placement.helper.convert.dto.Dto;
import com.placement.Placement.helper.convert.entity.Entity;
import com.placement.Placement.helper.response.Response;
import com.placement.Placement.model.entity.Company;
import com.placement.Placement.model.request.CompanyRequest;
import com.placement.Placement.model.response.CompanyResponse;
import com.placement.Placement.repository.CompanyRepository;
import com.placement.Placement.service.CompanyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    @Override
    public ResponseEntity<Object> getAll() {
        List<CompanyResponse> companyResponseList = companyRepository.findAll()
                .stream()
                .map(Entity::convertToDto)
                .toList();
        return Response.responseData(HttpStatus.OK, "Successfully get all companies", companyResponseList);
    }

    @Override
    public ResponseEntity<Object> getById(String id) {
        Company company = companyRepository.findById(id).orElse(null);
        if (company != null) {
            return Response.responseData(HttpStatus.OK, "Successfully get company", company);
        }

        return Response.responseData(HttpStatus.NOT_FOUND, "Company is not found", null);
    }

    @Override
    public CompanyResponse findById(String id) {
        Company company = companyRepository.findById(id).orElse(null);
        if (company != null) {
            return Entity.convertToDto(company);
        }

        return null;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public ResponseEntity<Object> create(CompanyRequest request) {
        try {
            Company company = Dto.convertToEntity(request);
            companyRepository.save(company);
            return Response.responseData(HttpStatus.OK, "Successfully create new company", company);
        }catch (Exception e){
            return Response.responseData(HttpStatus.BAD_REQUEST, "Status Invalid", null);
        }
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public ResponseEntity<Object> update(CompanyRequest request) {
        Company company = companyRepository.findById(request.getId()).orElse(null);
        if (company != null) {
            company.setName(request.getName());
            company.setAddress(request.getAddress());
            company.setPhoneNumber(request.getPhoneNumber());
            company.setStatus(EStatus.valueOf(request.getStatus()));
            companyRepository.save(company);

            return Response.responseData(HttpStatus.OK, "Successfully update company",
                    Entity.convertToDto(company));
        }

        return Response.responseData(HttpStatus.NOT_FOUND, "Company is not found", null);
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public ResponseEntity<Object> remove(String id) {
        Company company = companyRepository.findById(id).orElse(null);
        if (company != null) {
            company.setStatus(EStatus.NOT_ACTIVE);
            companyRepository.save(company);

            return Response.responseData(HttpStatus.OK, "Successfully remove company", Entity.convertToDto(company));
        }

        return Response.responseData(HttpStatus.NOT_FOUND, "Company is not found", null);
    }
}
