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
import org.springframework.stereotype.Service;

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
        return null;
    }

    @Override
    public StageResponse getById(String id) {
        return null;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public StageResponse create(StageRequest stageRequest) {
        return null;
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
