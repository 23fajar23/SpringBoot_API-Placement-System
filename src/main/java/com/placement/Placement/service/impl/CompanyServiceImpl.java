package com.placement.Placement.service.impl;

import com.placement.Placement.constant.EStatus;
import com.placement.Placement.helper.convert.dto.Dto;
import com.placement.Placement.helper.convert.entity.Entity;
import com.placement.Placement.helper.response.Response;
import com.placement.Placement.model.entity.Company;
import com.placement.Placement.model.request.CompanyRequest;
import com.placement.Placement.model.response.CompanyResponse;
import com.placement.Placement.model.response.PagingResponse;
import com.placement.Placement.repository.CompanyRepository;
import com.placement.Placement.service.CompanyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        return Response.responseData(HttpStatus.OK, "Successfully get all companies", companyResponseList, null);
    }

    @Override
    public ResponseEntity<Object> getById(String id) {
        Company company = companyRepository.findById(id).orElse(null);
        if (company != null) {
            return Response.responseData(HttpStatus.OK, "Successfully get company", company, null);
        }

        return Response.responseData(HttpStatus.NOT_FOUND, "Company is not found", null, null);
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
            return Response.responseData(HttpStatus.OK, "Successfully create new company", company, null);
        }catch (Exception e){
            return Response.responseData(HttpStatus.BAD_REQUEST, "Status Invalid", null, null);
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
                    Entity.convertToDto(company),null);
        }

        return Response.responseData(HttpStatus.NOT_FOUND, "Company is not found", null, null);
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public ResponseEntity<Object> remove(String id) {
        Company company = companyRepository.findById(id).orElse(null);
        if (company != null) {
            company.setStatus(EStatus.NOT_ACTIVE);
            companyRepository.save(company);

            return Response.responseData(HttpStatus.OK, "Successfully remove company", Entity.convertToDto(company), null);
        }

        return Response.responseData(HttpStatus.NOT_FOUND, "Company is not found", null, null);
    }

    @Override
    public ResponseEntity<Object> getAllByName(String name, Integer page, Integer size) {
        Specification<Company> specification = (root , query , criteriaBuilder) ->{
            List<Predicate> predicates = new ArrayList<>();
            if (name != null){
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(page,size);
        Page<Company> companies = companyRepository.findAll(specification,pageable);

        List<CompanyResponse> companyResponses = new ArrayList<>();
        for (Company company : companies.getContent()){
            companyResponses.add(Entity.convertToDto(company));
        }

        PageImpl<CompanyResponse> results = new PageImpl<>(companyResponses,pageable,companies.getTotalElements());

        PagingResponse pagingResponse = PagingResponse.builder()
                .currentPage(page)
                .totalPage(companies.getTotalPages())
                .size(size)
                .build();

        return Response.responseData(HttpStatus.OK, "Success get all result by name", results, pagingResponse);
    }
}
