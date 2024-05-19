package com.placement.Placement.service.impl;

import com.placement.Placement.constant.EStatus;
import com.placement.Placement.helper.convert.dto.Dto;
import com.placement.Placement.helper.convert.entity.Entity;
import com.placement.Placement.model.entity.Company;
import com.placement.Placement.model.entity.Education;
import com.placement.Placement.model.entity.Test;
import com.placement.Placement.model.request.TestRequest;
import com.placement.Placement.model.response.CompanyResponse;
import com.placement.Placement.model.response.EducationResponse;
import com.placement.Placement.model.response.TestResponse;
import com.placement.Placement.repository.TestRepository;
import com.placement.Placement.service.CompanyService;
import com.placement.Placement.service.EducationService;
import com.placement.Placement.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private final TestRepository testRepository;
    private final EducationService educationService;
    private final CompanyService companyService;

    @Override
    public List<TestResponse> getAll() {
        return testRepository.findAll()
                .stream()
                .map(Entity::convertToDto)
                .toList();
    }

    @Override
    public TestResponse getById(String id) {
        Test test = testRepository.findById(id).orElse(null);
        if (test != null) {
            return Entity.convertToDto(test);
        }

        return null;
    }

    @Override
    public TestResponse create(TestRequest testRequest) {
        EducationResponse educationResponse = educationService.getById(testRequest.getEducationId());
        CompanyResponse companyResponse = companyService.getById(testRequest.getCompanyId());

        if (educationResponse == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Education with id "
                    + testRequest.getEducationId() + " is not found");
        }

        if (companyResponse == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company with id "
                    + testRequest.getCompanyId() + " is not found");
        }

        Education education = Dto.convertToEntity(educationResponse);
        Company company = Dto.convertToEntity(companyResponse);

        Test test = Dto.convertToEntity(testRequest);
        test.setEducation(education);
        test.setCompany(company);

        testRepository.save(test);

        return Entity.convertToDto(test);
    }

    @Override
    public TestResponse update(TestRequest testRequest) {
        EducationResponse educationResponse = educationService.getById(testRequest.getEducationId());
        CompanyResponse companyResponse = companyService.getById(testRequest.getCompanyId());
        Test test = testRepository.findById(testRequest.getId()).orElse(null);

        if (test != null) {

            if (educationResponse == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Education with id "
                        + testRequest.getEducationId() + " is not found");
            }

            if (companyResponse == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company with id "
                        + testRequest.getCompanyId() + " is not found");
            }

            Education education = Dto.convertToEntity(educationResponse);
            Company company = Dto.convertToEntity(companyResponse);

            test.setNote(testRequest.getNote());
            test.setPlacement(testRequest.getPlacement());
            test.setCompany(company);
            test.setEducation(education);
            testRepository.save(test);

            return Entity.convertToDto(test);
        }

        return null;
    }

    @Override
    public TestResponse remove(TestRequest testRequest) {
        Test test = testRepository.findById(testRequest.getId()).orElse(null);
        if (test != null) {
            test.setStatus(EStatus.NOT_ACTIVE);
            testRepository.save(test);

            return Entity.convertToDto(test);
        }

        return null;
    }
}
