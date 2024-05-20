package com.placement.Placement.service.impl;

import com.placement.Placement.constant.EQuota;
import com.placement.Placement.constant.EStatus;
import com.placement.Placement.helper.convert.dto.Dto;
import com.placement.Placement.model.entity.*;
import com.placement.Placement.model.request.QuotaBatchRequest;
import com.placement.Placement.model.request.TestRequest;
import com.placement.Placement.model.request.UpdateTestRequest;
import com.placement.Placement.model.response.*;
import com.placement.Placement.repository.*;
import com.placement.Placement.service.BatchService;
import com.placement.Placement.service.EducationService;
import com.placement.Placement.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private final TestRepository testRepository;
    private final StageRepository stageRepository;
    private final QuotaRepository quotaRepository;
    private final QuotaBatchRepository quotaBatchRepository;
    private final CompanyRepository companyRepository;
    private final BatchService batchService;
    private final EducationService educationService;

    @Override
    public List<GetTestResponse> getAll() {
        return testRepository.findAll().stream()
                .map(test -> GetTestResponse.builder()
                            .id(test.getId())
                            .placement(test.getPlacement())
                            .note(test.getNote())
                            .statusTest(test.getStatus())
                            .company(test.getCompany())
                            .education(test.getEducation())
                            .stages(test.getStages())
                            .build()
                ).toList();
    }

    @Override
    public GetTestResponse getById(String id) {
        Test test = testRepository.findById(id).orElse(null);
        if (test != null) {
            return GetTestResponse.builder()
                    .id(test.getId())
                    .placement(test.getPlacement())
                    .note(test.getNote())
                    .statusTest(test.getStatus())
                    .stages(test.getStages())
                    .education(test.getEducation())
                    .company(test.getCompany())
                    .build();
        }

        return null;
    }

    @Override
    public TestResponse create(TestRequest testRequest) {
        Company company = companyRepository.findById(
                testRequest.getCompanyId()).orElse(null);

        EducationResponse educationResponse = educationService.findById(
                testRequest.getEducationId());

        if (company == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Company with id " + testRequest.getCompanyId()
                    + " is not found");
        }

        if (company.getStatus() == EStatus.NOT_ACTIVE) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Must use a company that is still active");
        }

        if (educationResponse == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Education with id "
                    + testRequest.getEducationId() + " is not found");
        }

        Test test = Test.builder()
                .placement(testRequest.getPlacement())
                .note(testRequest.getNote())
                .company(company)
                .education(Dto.convertToEntity(educationResponse))
                .status(EStatus.ACTIVE)
                .build();

        testRepository.saveAndFlush(test);

        Stage stage = Stage.builder()
                .name(testRequest.getNameStage())
                .dateTime(testRequest.getDateTime())
                .type(testRequest.getTypeStage())
                .test(test)
                .build();

        stageRepository.saveAndFlush(stage);

        Quota quota = Quota.builder()
                .total(testRequest.getTotalQuota())
                .available(testRequest.getQuotaAvailable())
                .type(testRequest.getTypeQuota())
                .stage(stage)
                .build();

        quotaRepository.saveAndFlush(quota);

        List<QuotaBatch> quotaBatches = new ArrayList<>();

        if (quota.getType() == EQuota.BATCH) {
            List<QuotaBatchRequest> quotaBatchesRequest= testRequest.getQuotaAvailableBatch();

            int totalQuoataAllBatch = 0;

            String batchIdOld = "";

            for (QuotaBatchRequest quotaBatchRequest : quotaBatchesRequest) {
                String batchId = quotaBatchRequest.getBatchId();
                BatchResponse batchResponse = batchService.findById(batchId);

                if (batchIdOld.equals(batchId)) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "The batch has been registered");
                }

                if (batchResponse == null) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Batch with id " + batchId + " is not found");
                }

                if (batchResponse.getStatus() == EStatus.NOT_ACTIVE) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Must use a batch that is still active");
                }

                totalQuoataAllBatch += quotaBatchRequest.getQuotaAvailable();

                QuotaBatch quotaBatch = QuotaBatch.builder()
                        .batch(Dto.convertToEntity(batchResponse))
                        .quota(quota)
                        .available(quotaBatchRequest.getQuotaAvailable())
                        .build();

                batchIdOld = batchId;

                quotaBatches.add(quotaBatch);
            }

            if (totalQuoataAllBatch != quota.getTotal()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "The total quota input into the per batch does not match the total quota in the stage");
            }

            quotaBatchRepository.saveAllAndFlush(quotaBatches);
        }

        List<Test> companyListTest = company.getTests();

        List<Quota> quotas = new ArrayList<>();
        List<Stage> stages = new ArrayList<>();

        quotas.add(quota);
        stages.add(stage);

        quota.setQuotaBatches(quotaBatches);
        stage.setQuotas(quotas);
        test.setStages(stages);


        quotaRepository.saveAndFlush(quota);
        stageRepository.saveAndFlush(stage);
        testRepository.saveAndFlush(test);

        companyListTest.add(test);
        company.setTests(companyListTest);
        companyRepository.save(company);

        return TestResponse.builder()
                .id(test.getId())
                .placement(test.getPlacement())
                .note(test.getNote())
                .statusTest(test.getStatus())
                .stage(stage)
                .education(Dto.convertToEntity(educationResponse))
                .company(company)
                .build();
    }

    @Override
    public TestResponse update(UpdateTestRequest updateTestRequest) {
        Test test = testRepository.findById(updateTestRequest.getId()).orElse(null);

        if (test != null) {
            Company company = companyRepository.findById(updateTestRequest.getCompanyId()).orElse(null);
            EducationResponse educationResponse = educationService.findById(updateTestRequest.getEducationId());

            if (company == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Company with id "
                        + updateTestRequest.getCompanyId() + " is not found");
            }

            if (company.getStatus() == EStatus.NOT_ACTIVE) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Must use a company that is still active");
            }

            if (educationResponse == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Education with id "
                        + updateTestRequest.getEducationId() + " is not found");
            }

            test.setPlacement(updateTestRequest.getPlacement());
            test.setNote(updateTestRequest.getNote());
            test.setCompany(company);
            test.setEducation(Dto.convertToEntity(educationResponse));
            test.setStatus(updateTestRequest.getStatusTest());



        }

        return null;
    }

    @Override
    public TestRemoveResponse remove(String id) {
        Test test = testRepository.findById(id).orElse(null);

        if (test != null) {
            Company company = companyRepository.findById(
                    test.getCompany().getId()).orElse(null);

            if (company == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Company with id "
                        + test.getCompany().getId() + " is not found");
            }

            test.setStatus(EStatus.NOT_ACTIVE);
            testRepository.save(test);

            return TestRemoveResponse.builder()
                    .id(test.getId())
                    .placement(test.getPlacement())
                    .note(test.getNote())
                    .statusTest(test.getStatus())
                    .company(company)
                    .build();
        }
        return null;
    }
}
