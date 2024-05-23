package com.placement.Placement.service.impl;

import com.placement.Placement.constant.EQuota;
import com.placement.Placement.constant.EResultTest;
import com.placement.Placement.helper.response.Response;
import com.placement.Placement.model.entity.*;
import com.placement.Placement.model.entity.auth.Customer;
import com.placement.Placement.model.request.ApplicationRequest;
import com.placement.Placement.model.request.ApproveTestRequest;
import com.placement.Placement.model.response.ApplicationResponse;
import com.placement.Placement.model.response.ApproveTestResponse;
import com.placement.Placement.repository.*;
import com.placement.Placement.repository.auth.CustomerRepository;
import com.placement.Placement.service.ApplicationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final TestRepository testRepository;
    private final CustomerRepository customerRepository;
    private final QuotaRepository quotaRepository;
    private final TestStageResultRepository testStageResultRepository;
    private final QuotaBatchRepository quotaBatchRepository;
    private final StageRepository stageRepository;

    @Override
    public ResponseEntity<Object> getAll() {
        List<ApplicationResponse> applicationResponses = applicationRepository.findAll()
                .stream()
                .map(application -> {
                    Test test = testRepository.findById(application.getTest().getId()).orElse(null);
                    Customer customer = customerRepository.findById(application.getCustomer().getId()).orElse(null);

                    if (test == null) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Test is not found");
                    }

                    if (customer == null) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer not found");
                    }

                    return ApplicationResponse.builder()
                            .id(application.getId())
                            .test(application.getTest())
                            .customer(customer)
                            .build();
                }).toList();

        return Response.responseData(HttpStatus.OK, "Successfully get all application", applicationResponses);
    }

    @Override
    public ResponseEntity<Object> getById(String id) {
        Application application = applicationRepository.findById(id).orElse(null);

        if (application != null) {

            Test test = testRepository.findById(application.getTest().getId()).orElse(null);

            Customer customer = customerRepository.findById(application.getCustomer().getId()).orElse(null);

            if (test == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Test with id " + application.getTest().getId() + " is not found");
            }

            if (customer == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer with id " + application.getCustomer().getId() + " is not found");
            }

            ApplicationResponse applicationResponse = ApplicationResponse.builder()
                    .id(application.getId())
                    .test(test)
                    .customer(customer)
                    .build();

            return Response.responseData(HttpStatus.OK, "Successfully get application", applicationResponse);
        }

        return Response.responseData(HttpStatus.NOT_FOUND, "Application is not found", null);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<Object> approve(ApproveTestRequest approveTestRequest) {
        Stage stage = stageRepository.findById(approveTestRequest.getStageId()).orElse(null);
        Application application = applicationRepository.findById(approveTestRequest.getApplicationId()).orElse(null);

        if (stage == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stage with id " + approveTestRequest.getStageId() + " is not found");
        }

        if (application == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Application with id " + approveTestRequest.getApplicationId() + " is not found");
        }

        TestStageResult testStageResult = testStageResultRepository.findByCustomerTestAndStage(
                application.getId(), stage.getId()
        ).orElse(null);

        if (testStageResult == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Test stage result is not found");
        }

        if (approveTestRequest.getResultTest() == EResultTest.FAILED) {
            testStageResult.setResult(approveTestRequest.getResultTest());
            application.setFinalResult(approveTestRequest.getResultTest());
            testStageResultRepository.saveAndFlush(testStageResult);
            applicationRepository.save(application);

        } else {
            testStageResult.setResult(approveTestRequest.getResultTest());
            testStageResultRepository.save(testStageResult);
        }

        ApproveTestResponse approveTestResponse = ApproveTestResponse.builder()
                .application(application)
                .stage(stage)
                .build();

        return Response.responseData(HttpStatus.OK, "Successfully approve application test customer", approveTestResponse);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<Object> create(ApplicationRequest applicationRequest) {
        Test test = testRepository.findById(applicationRequest.getTestId()).orElse(null);
        Customer customer = customerRepository.findById(applicationRequest.getCustomerId()).orElse(null);

        if (test == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Test with id "
                    + applicationRequest.getTestId()
                    + " is not found");
        }

        if (customer == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer with id "
                    + applicationRequest.getCustomerId()
                    + " is not found");
        }

        Application application = applicationRepository.findByTestAndCustomer(test.getId(), customer.getId())
                .orElse(null);

        if (application != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer already registered in test");
        }

        Quota quota = quotaRepository.findById(test
                        .getStages().get(0)
                        .getQuotas().get(0).getId())
                .orElse(null);

        if (quota == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quota with id "
                    + test.getStages().get(0).getQuotas().get(0).getId()
                    + " is not found");
        }

        if (quota.getAvailable() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The test quota is full");
        }

        if (customer.getEducation().getValue() < test.getEducation().getValue()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Customer education does not meet the requirements");
        }

        boolean isBatchFound = false;

        if (quota.getType() == EQuota.BATCH) {
            for (int i = 0; i < quota.getQuotaBatches().size(); i++) {
                if (quota.getQuotaBatches().get(i).getBatch() == customer.getBatch()) {
                    isBatchFound = true;
                    break;
                }
            }

            if (!isBatchFound) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Batch customers are not registered to take this test");
            }

            QuotaBatch quotaBatch = quotaBatchRepository.findByBatchId(customer.getBatch().getId(), test.getStages().get(0)
                    .getQuotas().get(0).getId())
                    .orElse(null);

            if (quotaBatch == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quota batch with id " + test
                        .getStages().get(0)
                        .getQuotas().get(0)
                        .getId() + " is not found ");
            }

            quotaBatch.setAvailable(quotaBatch.getAvailable() - 1);
            quota.setAvailable(quota.getAvailable() - 1);

            quotaBatchRepository.saveAndFlush(quotaBatch);
            quotaRepository.saveAndFlush(quota);

            application = Application.builder()
                    .customer(customer)
                    .test(test)
                    .dateTime(LocalDate.now())
                    .build();

            applicationRepository.saveAndFlush(application);

            List<Application> applications = customer.getApplications();
            applications.add(application);

            customer.setApplications(applications);

            customerRepository.saveAndFlush(customer);

            TestStageResult testStageResult = TestStageResult.builder()
                    .application(application)
                    .stage(test.getStages().get(0))
                    .build();

            testStageResultRepository.saveAndFlush(testStageResult);

            List<TestStageResult> testStageResultList = application.getTestStageResultList() == null ? new ArrayList<>()
                    : application.getTestStageResultList();

            testStageResultList.add(testStageResult);

            application.setTestStageResultList(testStageResultList);

            applicationRepository.save(application);
        } else {

            application = Application.builder()
                    .customer(customer)
                    .test(test)
                    .dateTime(LocalDate.now())
                    .build();

            applicationRepository.saveAndFlush(application);

            List<Application> applications = customer.getApplications();
            applications.add(application);

            customer.setApplications(applications);

            customerRepository.saveAndFlush(customer);

            TestStageResult testStageResult = TestStageResult.builder()
                    .application(application)
                    .stage(test.getStages().get(0))
                    .build();

            testStageResultRepository.saveAndFlush(testStageResult);

            List<TestStageResult> testStageResultList = application.getTestStageResultList();
            testStageResultList.add(testStageResult);

            applicationRepository.save(application);
        }

        ApplicationResponse applicationResponse = ApplicationResponse.builder()
                .id(application.getId())
                .customer(customer)
                .test(test)
                .build();

        return Response.responseData(HttpStatus.OK, "Successfully create application", applicationResponse);
    }



}
