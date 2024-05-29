package com.placement.Placement.service.impl;

import com.placement.Placement.constant.EQuota;
import com.placement.Placement.constant.EResultTest;
import com.placement.Placement.constant.EStage;
import com.placement.Placement.constant.EStatus;
import com.placement.Placement.helper.convert.entity.Entity;
import com.placement.Placement.helper.response.Response;
import com.placement.Placement.model.entity.*;
import com.placement.Placement.model.request.StageRequest;
import com.placement.Placement.model.response.*;
import com.placement.Placement.repository.*;
import com.placement.Placement.service.StageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StageServiceImpl implements StageService {
    private final StageRepository stageRepository;
    private final TestRepository testRepository;
    private final QuotaRepository quotaRepository;
    private final QuotaBatchRepository quotaBatchRepository;
    private final TestStageResultRepository testStageResultRepository;
    private final ApplicationRepository applicationRepository;

    @Override
    public ResponseEntity<Object> getById(String id) {
        Stage stage = stageRepository.findById(id).orElse(null);

        if (stage != null) {
            return Response.responseData(HttpStatus.OK,"Successfully get stage", Entity.convertToDto(stage), null);
        }

        return Response.responseData(HttpStatus.BAD_REQUEST, "Stage is not found", null, null);
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public ResponseEntity<Object> create(StageRequest stageRequest) {
        Test test = testRepository.findById(stageRequest.getTestId()).orElse(null);

        if (test == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Test with id "
                    + stageRequest.getTestId()
                    + " is not found");
        }

        if (test.getStatus() == EStatus.NOT_ACTIVE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must use a test that is still active");
        }

        List<Stage> stages = test.getStages();

        if (stages.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Test not have a stage");
        }

        Stage lastStage = stages.get(stages.size() - 1);

        if (lastStage.getStageStatus() != EStage.FINISHED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The previous stage has not yet been completed");
        }

        List<Optional<TestStageResult>> participants = testStageResultRepository.findByStage(lastStage.getId());

        List<TestStageResult> customerPassedList = getPassedParticipants(participants);

        if (customerPassedList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There were no participants there");
        }

        if (stageRequest.getDateTime().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The time input must be today or in the future");
        }

        if (stageRequest.getStageStatus() == EStage.FINISHED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Must select ongoing or coming soon when creating new test");
        }

        Stage stage = Stage.builder()
                .name(stageRequest.getNameStage())
                .dateTime(stageRequest.getDateTime())
                .type(stageRequest.getTypeStage())
                .stageStatus(stageRequest.getStageStatus())
                .test(test)
                .build();

        stageRepository.saveAndFlush(stage);

        Quota quota = Quota.builder()
                .total(customerPassedList.size())
                .available(0)
                .type(lastStage.getQuotas().get(0).getType())
                .stage(stage)
                .build();

        quotaRepository.saveAndFlush(quota);

        List<QuotaBatch> quotaBatches = new ArrayList<>();

        if (quota.getType() == EQuota.BATCH) {

            List<QuotaBatch> quotaBatchList = lastStage.getQuotas().get(0).getQuotaBatches();

            for (QuotaBatch batch : quotaBatchList) {
                QuotaBatch quotaBatch = QuotaBatch.builder()
                        .batch(batch.getBatch())
                        .available(0)
                        .quota(quota)
                        .build();

                quotaBatches.add(quotaBatch);
            }

            quotaBatchRepository.saveAllAndFlush(quotaBatches);
        }

        for (TestStageResult passedCustomer : customerPassedList) {

            Application application = applicationRepository.findById(passedCustomer.getApplication().getId()).orElse(null);

            if (application == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Application with id " + passedCustomer.getApplication().getId() + " is not found");
            }

            TestStageResult testStageResult = TestStageResult.builder()
                    .stage(stage)
                    .application(application)
                    .build();

            List<TestStageResult> testStageResultList = application.getTestStageResultList();
            testStageResultList.add(testStageResult);

            testStageResultRepository.saveAndFlush(testStageResult);

            application.setTestStageResultList(testStageResultList);
            applicationRepository.saveAndFlush(application);
        }

        List<Quota> quotas = new ArrayList<>();

        stages.add(stage);
        quotas.add(quota);

        quota.setQuotaBatches(quotaBatches);
        stage.setQuotas(quotas);
        test.setStages(stages);

        quotaRepository.saveAndFlush(quota);
        stageRepository.saveAndFlush(stage);
        testRepository.save(test);

        StageResponse result = StageResponse.builder()
                .id(test.getId())
                .nameStage(stage.getName())
                .typeStage(stage.getType())
                .dateTime(stage.getDateTime())
                .test(stage.getTest())
                .build();

        return Response.responseData(HttpStatus.OK, "Successfully create new stage", result, null);
    }

    @Override
    public ResponseEntity<Object> update(StageRequest stageRequest) {

        Stage stage = stageRepository.findById(stageRequest.getId()).orElse(null);

        if (stage != null) {

            stage.setName(stageRequest.getNameStage());

            if (stageRequest.getDateTime().isBefore(LocalDate.now())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "The time input must be today or in the future");
            }

            stage.setDateTime(stageRequest.getDateTime());
            stage.setStageStatus(stageRequest.getStageStatus());
            stage.setType(stageRequest.getTypeStage());

            stageRepository.saveAndFlush(stage);

            return Response.responseData(HttpStatus.OK, "Successfully update stage", Entity.convertToDto(stage), null);
        }

        return Response.responseData(HttpStatus.NOT_FOUND, "Stage is not found", null, null);
    }

    private static List<TestStageResult> getPassedParticipants(List<Optional<TestStageResult>> participants) {
        List<TestStageResult> customerPassedList = new ArrayList<>();

        for (Optional<TestStageResult> participant : participants) {
            TestStageResult testStageResult = participant.orElse(null);
            if (testStageResult == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Test stage result is not found");
            }

            if (testStageResult.getResult() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There are participants who have not been approved");
            }

            if (testStageResult.getResult() == EResultTest.PASSED) {
                customerPassedList.add(testStageResult);
            }

        }

        if (customerPassedList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer passed list is empty");
        }
        return customerPassedList;
    }
}
