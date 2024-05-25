package com.placement.Placement.service.impl;

import com.placement.Placement.constant.EQuota;
import com.placement.Placement.constant.EStage;
import com.placement.Placement.constant.EStatus;
import com.placement.Placement.helper.convert.dto.Dto;
import com.placement.Placement.helper.response.Response;
import com.placement.Placement.model.entity.*;
import com.placement.Placement.model.request.QuotaBatchRequest;
import com.placement.Placement.model.request.TestRequest;
import com.placement.Placement.model.response.*;
import com.placement.Placement.repository.*;
import com.placement.Placement.service.BatchService;
import com.placement.Placement.service.EducationService;
import com.placement.Placement.service.TestService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDate;
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
    public ResponseEntity<Object> getAllByPlacementAndRole(String placement, String rolePlacement, Integer page, Integer size) {
        Specification<Test> specification = (root , query , criteriaBuilder) ->{
            List<Predicate> predicates = new ArrayList<>();
            if (placement != null){
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("placement")), "%" + placement.toLowerCase() + "%"));
            }
            if (rolePlacement != null){
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("rolePlacement")), "%" + rolePlacement.toLowerCase() + "%"));
            }
            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(page,size);
        Page<Test> tests = testRepository.findAll(specification,pageable);

        List<GetTestResponse> testResponses = new ArrayList<>();
        for (Test test : tests.getContent()){
            testResponses.add(GetTestResponse.builder()
                    .id(test.getId())
                    .placement(test.getPlacement())
                    .note(test.getNote())
                    .rolePlacement(test.getRolePlacement())
                    .statusTest(test.getStatus())
                    .company(test.getCompany())
                    .education(test.getEducation())
                    .stages(test.getStages())
                    .build());
        }

        PageImpl<GetTestResponse> results = new PageImpl<>(testResponses,pageable,tests.getTotalElements());

        PagingResponse pagingResponse = PagingResponse.builder()
                .currentPage(page)
                .totalPage(tests.getTotalPages())
                .size(size)
                .build();

        return Response.responseData(HttpStatus.OK, "Successfully get all test by placement and role placement", results, pagingResponse);
    }

    @Override
    public ResponseEntity<Object> getAll() {
        var results = testRepository.findAll().stream()
                .map(test -> GetTestResponse.builder()
                        .id(test.getId())
                        .placement(test.getPlacement())
                        .note(test.getNote())
                        .rolePlacement(test.getRolePlacement())
                        .statusTest(test.getStatus())
                        .company(test.getCompany())
                        .education(test.getEducation())
                        .stages(test.getStages())
                        .build()
                ).toList();
        return Response.responseData(HttpStatus.OK, "Success get all tests", results, null);
    }

    @Override
    public ResponseEntity<Object> getById(String id) {
        Test test = testRepository.findById(id).orElse(null);
        if (test != null) {
            var result = GetTestResponse.builder()
                    .id(test.getId())
                    .placement(test.getPlacement())
                    .note(test.getNote())
                    .rolePlacement(test.getRolePlacement())
                    .statusTest(test.getStatus())
                    .stages(test.getStages())
                    .education(test.getEducation())
                    .company(test.getCompany())
                    .build();

            return Response.responseData(HttpStatus.OK, "Successfully get test", result, null);
        }

        return Response.responseData(HttpStatus.BAD_REQUEST, "Test is not found", null, null);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<Object> create(TestRequest testRequest) {
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
                .rolePlacement(testRequest.getRolePlacement())
                .status(EStatus.ACTIVE)
                .build();

        testRepository.saveAndFlush(test);

        if (testRequest.getDateTime().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The time input must be today or in the future");
        }

        if (testRequest.getStageStatus() == EStage.FINISHED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Must select ongoing or coming soon when creating new test");
        }

        Stage stage = Stage.builder()
                .name(testRequest.getNameStage())
                .dateTime(testRequest.getDateTime())
                .type(testRequest.getTypeStage())
                .test(test)
                .stageStatus(testRequest.getStageStatus())
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

        var result = TestResponse.builder()
                .id(test.getId())
                .placement(test.getPlacement())
                .note(test.getNote())
                .statusTest(test.getStatus())
                .stage(stage)
                .education(Dto.convertToEntity(educationResponse))
                .company(company)
                .build();

        return Response.responseData(HttpStatus.OK, "Successfully create new test", result, null);
    }

    @Override
    public ResponseEntity<Object> update(TestRequest testRequest) {
        Test test = testRepository.findById(testRequest.getId()).orElse(null);

        if (test != null) {

            EducationResponse educationResponse = educationService.findById(testRequest.getEducationId());

            if (educationResponse == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Education with id "
                        + testRequest.getEducationId()
                        + " is not found");
            }

            test.setPlacement(testRequest.getPlacement());
            test.setNote(testRequest.getNote());
            test.setEducation(Dto.convertToEntity(educationResponse));
            test.setStatus(testRequest.getStatusTest());
            test.setRolePlacement(testRequest.getRolePlacement());

            testRepository.save(test);
            var result = UpdateTestResponse.builder()
                    .id(test.getId())
                    .placement(test.getPlacement())
                    .note(test.getNote())
                    .statusTest(test.getStatus())
                    .education(Dto.convertToEntity(educationResponse))
                    .build();

            return Response.responseData(HttpStatus.OK, "Successfully update test", result, null);
        }

        return Response.responseData(HttpStatus.NOT_FOUND, "Test is not found", null, null);
    }

    @Override
    public ResponseEntity<Object> remove(String id) {
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

            var result = TestRemoveResponse.builder()
                    .id(test.getId())
                    .placement(test.getPlacement())
                    .note(test.getNote())
                    .statusTest(test.getStatus())
                    .company(company)
                    .build();

            return Response.responseData(HttpStatus.OK, "Successfully remove test", result, null);
        }
        return Response.responseData(HttpStatus.NOT_FOUND, "Test is not found", null,null);
    }
}
