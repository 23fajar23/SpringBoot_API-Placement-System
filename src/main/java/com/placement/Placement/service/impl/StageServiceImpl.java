package com.placement.Placement.service.impl;

import com.placement.Placement.constant.EStatus;
import com.placement.Placement.helper.convert.dto.Dto;
import com.placement.Placement.model.entity.*;
import com.placement.Placement.model.request.StageRequest;
import com.placement.Placement.model.response.*;
import com.placement.Placement.repository.QuotaRepository;
import com.placement.Placement.repository.StageRepository;
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
    private final QuotaRepository quotaRepository;
    private final TestService testService;
    private final EducationService educationService;

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
    public StageResponse create(StageRequest stageRequest) {
        TestResponse testResponse = testService.getById(stageRequest.getTestId());
        EducationResponse educationResponse = educationService.getById(stageRequest.getEducationId());

        if (testResponse == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Test is not found with id " + stageRequest.getTestId());
        }

        if (educationResponse == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Education is not found with id " + stageRequest.getEducationId());
        }

        Stage stage = Stage.builder()
                .name(stageRequest.getName())
                .type(stageRequest.getType())
                .dateTime(stageRequest.getDateTime())
                .education(Dto.convertToEntity(educationResponse))
                .quotas(null)
                .type(stageRequest.getType())
                .test(Dto.convertToEntity(testResponse))
                .status(EStatus.ACTIVE)
                .quotas(null)
                .build();

        stageRepository.saveAndFlush(stage);

        Quota quota = Quota.builder()
                .total(stageRequest.getQuotaTotal())
                .available(stageRequest.getQuotaTotal())
                .type(stageRequest.getQuotaBatchType())
                .quotaBatches(null)
                .stage(stage)
                .build();

        quotaRepository.saveAndFlush(quota);

        List<Quota> quotas = new ArrayList<>();
        quotas.add(quota);
        stage.setQuotas(quotas);

        stageRepository.save(stage);

        return StageResponse.builder()
                .id(stage.getId())
                .name(stage.getName())
                .type(stage.getType())
                .dateTime(stage.getDateTime())
                .totalQuota(quota.getTotal())
                .quotaAvailable(quota.getAvailable())
                .quotaBatchType(quota.getType())
                .status(stage.getStatus())
                .test(stage.getTest())
                .quotas(stage.getQuotas())
                .build();
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public StageResponse update(StageRequest stageRequest) {

        Stage stage = stageRepository.findById(stageRequest.getId()).orElse(null);
        TestResponse testResponse = testService.getById(stageRequest.getTestId());
        EducationResponse educationResponse = educationService.getById(stageRequest.getEducationId());

        if (stage != null) {
            if (testResponse == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Test is not found with id " + stageRequest.getTestId());
            }

            if (educationResponse == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Education is not found with id " + stageRequest.getEducationId());
            }

            stage.setName(stage.getName());
            stage.setTest(Dto.convertToEntity(testResponse));
            stage.setType(stageRequest.getType());
            stage.setEducation(Dto.convertToEntity(educationResponse));
            stage.setDateTime(stageRequest.getDateTime());
            stage.setStatus(stageRequest.getStatus());

            Quota quota = quotaRepository.findByStageId(stage.getId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Quota with stage id " + stage.getId() + " is not found"));

            if (quota.getAvailable() == quota.getTotal()) {
                quota.setType(stageRequest.getQuotaBatchType());
                quota.setStage(stage);
                quota.setTotal(stageRequest.getQuotaTotal());
                quota.setAvailable(stageRequest.getQuotaTotal());
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quota has been used");
            }

            quotaRepository.saveAndFlush(quota);
            stageRepository.save(stage);

            return StageResponse.builder()
                    .id(stage.getId())
                    .name(stage.getName())
                    .type(stage.getType())
                    .dateTime(stage.getDateTime())
                    .totalQuota(quota.getTotal())
                    .quotaAvailable(quota.getAvailable())
                    .quotaBatchType(quota.getType())
                    .status(stage.getStatus())
                    .test(stage.getTest())
                    .quotas(stage.getQuotas())
                    .build();

        }

        return null;
    }

    @Override
    public StageResponse remove(String id) {
        return null;
    }
}
