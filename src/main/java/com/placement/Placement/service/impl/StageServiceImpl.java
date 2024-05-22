package com.placement.Placement.service.impl;

import com.placement.Placement.constant.EQuota;
import com.placement.Placement.constant.EStage;
import com.placement.Placement.constant.EStatus;
import com.placement.Placement.helper.convert.dto.Dto;
import com.placement.Placement.helper.response.Response;
import com.placement.Placement.model.entity.Quota;
import com.placement.Placement.model.entity.QuotaBatch;
import com.placement.Placement.model.entity.Stage;
import com.placement.Placement.model.entity.Test;
import com.placement.Placement.model.request.QuotaBatchRequest;
import com.placement.Placement.model.request.StageRequest;
import com.placement.Placement.model.response.*;
import com.placement.Placement.repository.QuotaBatchRepository;
import com.placement.Placement.repository.QuotaRepository;
import com.placement.Placement.repository.StageRepository;
import com.placement.Placement.repository.TestRepository;
import com.placement.Placement.service.BatchService;
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

@Service
@RequiredArgsConstructor
public class StageServiceImpl implements StageService {
    private final StageRepository stageRepository;
    private final TestRepository testRepository;
    private final QuotaRepository quotaRepository;
    private final BatchService batchService;
    private final QuotaBatchRepository quotaBatchRepository;

    @Override
    public List<StageResponse> getAll() {
        return null;
    }

    @Override
    public StageResponse getById(String id) {
        return null;
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
        if (!stages.isEmpty()) {
            for (int i = 0; i < test.getStages().size(); i++) {
                Stage stage = stages.get(i);
                if (stage.getStageStatus() == EStage.ONGOING || stage.getStageStatus() == EStage.COMING_SOON) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "There are stages that are still ongoing or coming soon");
                }
            }
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
                .total(stageRequest.getTotalQuota())
                .available(stageRequest.getQuotaAvailable())
                .type(stageRequest.getTypeQuota())
                .stage(stage)
                .build();

        quotaRepository.saveAndFlush(quota);

        List<QuotaBatch> quotaBatches = new ArrayList<>();

        if (quota.getType() == EQuota.BATCH) {
            List<QuotaBatchRequest> quotaBatchesRequest= stageRequest.getQuotaAvailableBatch();

            int totalQuotaAllBatch = 0;

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

                totalQuotaAllBatch += quotaBatchRequest.getQuotaAvailable();

                QuotaBatch quotaBatch = QuotaBatch.builder()
                        .batch(Dto.convertToEntity(batchResponse))
                        .quota(quota)
                        .available(quotaBatchRequest.getQuotaAvailable())
                        .build();

                batchIdOld = batchId;

                quotaBatches.add(quotaBatch);
            }

            if (totalQuotaAllBatch != quota.getTotal()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "The total quota input into the per batch does not match the total quota in the stage");
            }

            quotaBatchRepository.saveAllAndFlush(quotaBatches);
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
                .name(stage.getName())
                .type(stage.getType())
                .dateTime(stage.getDateTime())
                .totalQuota(quota.getTotal())
                .quotaAvailable(quota.getAvailable())
                .quotaBatchType(quota.getType())
                .quotas(quotas)
                .build();

        return Response.responseData(HttpStatus.OK, "Successfully create new stage", result);
    }

    @Override
    public ResponseEntity<Object> update(StageRequest stageRequest) {
        Stage stage = stageRepository.findById(stageRequest.getId()).orElse(null);

        if (stage != null) {

            if (stage.getStageStatus() == EStage.ONGOING) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "test is ongoing");
            }

            if (stage.getStageStatus() == EStage.FINISHED) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "test is finished");
            }

            if (stageRequest.getDateTime().isBefore(LocalDate.now())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "The time input must be today or in the future");
            }

            stage.setName(stageRequest.getNameStage());
            stage.setType(stageRequest.getTypeStage());
            stage.setStageStatus(stageRequest.getStageStatus());
            stage.setDateTime(stageRequest.getDateTime());

            Quota quota = quotaRepository.findById(stage.getQuotas().get(0).getId())
                    .orElse(null);

            if (quota == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quota with id "
                        + stage.getQuotas().get(0).getId()
                        + " is not found" );
            }

            if (quota.getTotal() != quota.getAvailable()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "quota has been used");
            }

            quota.setType(stageRequest.getTypeQuota());
            quota.setTotal(stageRequest.getTotalQuota());
            quota.setAvailable(stageRequest.getQuotaAvailable());

            List<QuotaBatch> quotaBatches = new ArrayList<>();

            if (quota.getType() == EQuota.BATCH) {
                List<QuotaBatchRequest> quotaBatchesRequest= stageRequest.getQuotaAvailableBatch();

                int totalQuotaAllBatch = 0;

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

                    totalQuotaAllBatch += quotaBatchRequest.getQuotaAvailable();

                    QuotaBatch quotaBatch = QuotaBatch.builder()
                            .batch(Dto.convertToEntity(batchResponse))
                            .quota(quota)
                            .available(quotaBatchRequest.getQuotaAvailable())
                            .build();

                    batchIdOld = batchId;

                    quotaBatches.add(quotaBatch);
                }

                if (totalQuotaAllBatch != quota.getTotal()) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "The total quota input into the per batch does not match the total quota in the stage");
                }

                quotaBatchRepository.saveAllAndFlush(quotaBatches);
            }

            quotaBatchRepository.deleteAll(quota.getQuotaBatches());

            List<Quota> quotas = new ArrayList<>();

            quotas.add(quota);

            quota.setQuotaBatches(quotaBatches);
            stage.setQuotas(quotas);

            quotaRepository.saveAndFlush(quota);
            stageRepository.saveAndFlush(stage);

            StageResponse result = StageResponse.builder()
                    .id(stage.getId())
                    .name(stage.getName())
                    .type(stage.getType())
                    .dateTime(stage.getDateTime())
                    .totalQuota(quota.getTotal())
                    .quotaAvailable(quota.getAvailable())
                    .quotaBatchType(quota.getType())
                    .quotas(quotas)
                    .build();

            return Response.responseData(HttpStatus.OK, "Successfully update stage", result);
        }

        return Response.responseData(HttpStatus.NOT_FOUND, "Stage is not found", null);
    }

    @Override
    public StageResponse remove(String id) {
        return null;
    }
}
