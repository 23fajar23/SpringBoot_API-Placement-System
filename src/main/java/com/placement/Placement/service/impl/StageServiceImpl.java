package com.placement.Placement.service.impl;

import com.placement.Placement.constant.EStatus;
import com.placement.Placement.model.entity.*;
import com.placement.Placement.model.request.StageRequest;
import com.placement.Placement.model.response.*;
import com.placement.Placement.repository.QuotaBatchRepository;
import com.placement.Placement.repository.QuotaRepository;
import com.placement.Placement.repository.StageRepository;
import com.placement.Placement.service.BatchService;
import com.placement.Placement.service.EducationService;
import com.placement.Placement.service.StageService;
import com.placement.Placement.service.TestService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StageServiceImpl implements StageService {
    private final StageRepository stageRepository;
    private final QuotaBatchRepository quotaBatchRepository;
    private final QuotaRepository quotaRepository;
    private final TestService testService;
    private final EducationService educationService;
    private final BatchService batchService;

    @Override
    public List<StageResponse> getAll() {
        return stageRepository.findAll().stream()
                .map(stage -> {
                    Quota quota = quotaRepository.findByStageId(stage.getId())
                            .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.BAD_REQUEST, "Quota is not found"));

                    List<QuotaBatch> quotaBatches = quotaBatchRepository.findAll();

                    List<QuotaBatch> qb = quotaBatches.stream()
                            .filter(quotaBatch -> quotaBatch.getQuota().getId().equals(quota.getId()))
                            .toList();

                    List<QuotaBatchResponse> quotaBatchResponseList = qb.stream().map(quotaBatch -> QuotaBatchResponse.builder()
                            .batchId(quotaBatch.getBatch().getId())
                            .quotaAvailable(quotaBatch.getAvailable())
                            .build()).toList();


                    return StageResponse.builder()
                            .id(stage.getId())
                            .name(stage.getName())
                            .dateTime(stage.getDateTime())
                            .quotaAvailable(quota.getAvailable())
                            .totalQuota(quota.getTotal())
                            .type(stage.getType())
                            .test(stage.getTest())
                            .status(stage.getStatus())
                            .dateTime(stage.getDateTime())
                            .status(stage.getStatus())
                            .quotaBatchResponses(quotaBatchResponseList)
                            .build();
                }).toList();
    }

    @Override
    public StageResponse getById(String id) {
        Stage stage = stageRepository.findById(id).orElse(null);
        if(stage !=  null) {

            Quota quota = quotaRepository.findByStageId(stage.getId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Quota is not found"));

            List<QuotaBatch> quotaBatches = quotaBatchRepository.findAll();

            List<QuotaBatch> qb = quotaBatches.stream()
                    .filter(quotaBatch -> quotaBatch.getQuota().getId().equals(quota.getId()))
                    .toList();

            List<QuotaBatchResponse> quotaBatchResponseList = qb.stream().map(quotaBatch -> QuotaBatchResponse.builder()
                    .batchId(quotaBatch.getBatch().getId())
                    .quotaAvailable(quotaBatch.getAvailable())
                    .build()).toList();

            return StageResponse.builder()
                    .id(stage.getId())
                    .name(stage.getName())
                    .type(stage.getType())
                    .dateTime(stage.getDateTime())
                    .test(stage.getTest())
                    .status(stage.getStatus())
                    .quotaBatchResponses(quotaBatchResponseList)
                    .build();
        }
        return null;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public StageResponse create(StageRequest stageRequest) {

        TestResponse testResponse = testService.getById(stageRequest.getTestId());
        EducationResponse educationResponse = educationService.findById(stageRequest.getEducationId());

        if (testResponse == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Test is not found");
        }

        Test test = Test.builder()
                .id(testResponse.getId())
                .placement(testResponse.getPlacement())
                .note(testResponse.getNote())
                .company(testResponse.getCompany())
                .education(testResponse.getEducation())
                .status(testResponse.getStatus())
                .stages(testResponse.getStages())
                .build();

        Education education = Education.builder()
                .id(educationResponse.getId())
                .education(educationResponse.getEducation())
                .value(educationResponse.getValue())
                .build();

        Stage stage = Stage.builder()
                .name(stageRequest.getName())
                .type(stageRequest.getType())
                .test(test)
                .dateTime(stageRequest.getDateTime())
                .education(education)
                .status(EStatus.ACTIVE)
                .build();

        stageRepository.saveAndFlush(stage);

        Quota quota = Quota.builder()
                .type(stageRequest.getQuotaBatch())
                .available(stageRequest.getQuotaAvailable())
                .total(stageRequest.getTotalQuota())
                .stage(stage)
                .build();

        quotaRepository.saveAndFlush(quota);

        List<QuotaBatch> quotaBatches = new ArrayList<>();


        for (int i = 0; i < stageRequest.getQuotaBatchRequestList().size(); i++) {
            BatchResponse batchResponse = batchService.findById(stageRequest.getQuotaBatchRequestList().get(i).getBatchId());
            Batch batch = Batch.builder()
                    .id(batchResponse.getId())
                    .name(batchResponse.getName())
                    .status(batchResponse.getStatus())
                    .build();
            quotaBatches.add(QuotaBatch.builder()
                    .batch(batch)
                    .available(stageRequest.getQuotaAvailable())
                    .quota(quota)
                    .build());
        }

        quotaBatchRepository.saveAll(quotaBatches);

        return StageResponse.builder()
                .id(stage.getId())
                .type(stage.getType())
                .test(stage.getTest())
                .name(stage.getName())
                .totalQuota(quota.getTotal())
                .dateTime(stage.getDateTime())
                .quotaAvailable(quota.getAvailable())
                .status(stage.getStatus())
                .quotaBatchResponses(quotaBatches.stream().map(quotaBatch -> QuotaBatchResponse.builder()
                                .batchId(quotaBatch.getBatch().getId())
                                .quotaAvailable(quotaBatch.getAvailable())
                                .build())
                        .toList())
                .build();
    }

    @Override
    public StageResponse update(StageRequest stageRequest) {
        return null;
    }

    @Override
    public StageResponse remove(String id) {
        return null;
    }
}
