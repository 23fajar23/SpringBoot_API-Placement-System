package com.placement.Placement.service.impl;

import com.placement.Placement.constant.EStatus;
import com.placement.Placement.helper.convert.dto.Dto;
import com.placement.Placement.helper.convert.entity.Entity;
import com.placement.Placement.model.entity.Company;
import com.placement.Placement.model.request.CompanyRequest;
import com.placement.Placement.model.response.CompanyResponse;
import com.placement.Placement.repository.CompanyRepository;
import com.placement.Placement.service.CompanyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    @Override
    public List<CompanyResponse> getAll() {
        return companyRepository.findAll()
                .stream()
                .map(Entity::convertToDto)
                .toList();
    }

    @Override
    public CompanyResponse getById(String id) {
        Company company = companyRepository.findById(id).orElse(null);
        if (company != null) {
            return Entity.convertToDto(company);
        }

        return null;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public CompanyResponse create(CompanyRequest request) {
        Company company = Company.builder()
                .name(request.getName())
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .status(EStatus.valueOf(request.getStatus()))
                .build();
        companyRepository.save(company);

        return Entity.convertToDto(company);
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public CompanyResponse update(CompanyRequest request) {
        Company company = companyRepository.findById(request.getId()).orElse(null);
        if (company != null) {
            company.setName(request.getName());
            company.setAddress(request.getAddress());
            company.setPhoneNumber(request.getPhoneNumber());
            company.setStatus(EStatus.valueOf(request.getStatus()));
            companyRepository.save(company);

            return Entity.convertToDto(company);
        }

        return null;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public CompanyResponse remove(String id) {
        Company company = companyRepository.findById(id).orElse(null);
        if (company != null) {
            company.setStatus(EStatus.NOT_ACTIVE);
            companyRepository.save(company);

            return Entity.convertToDto(company);
        }

        return null;
    }
}
