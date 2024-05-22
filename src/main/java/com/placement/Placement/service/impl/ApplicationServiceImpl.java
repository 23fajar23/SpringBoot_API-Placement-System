package com.placement.Placement.service.impl;

import com.placement.Placement.constant.EQuota;
import com.placement.Placement.helper.response.Response;
import com.placement.Placement.model.entity.*;
import com.placement.Placement.model.entity.auth.Customer;
import com.placement.Placement.model.request.ApplicationRequest;
import com.placement.Placement.model.response.ApplicationResponse;
import com.placement.Placement.repository.*;
import com.placement.Placement.repository.auth.CustomerRepository;
import com.placement.Placement.service.ApplicationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Override
    public ResponseEntity<Object> getAll() {
        return null;
    }

    @Override
    public ResponseEntity<Object> getById(String id) {
        return null;
    }

    @Override
    public ResponseEntity<Object> update(ApplicationRequest applicationRequest) {
        return null;
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

        if (test.getEducation() != customer.getEducation()) {
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
