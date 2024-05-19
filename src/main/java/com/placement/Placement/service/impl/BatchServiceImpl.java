package com.placement.Placement.service.impl;

import com.placement.Placement.constant.EStatus;
import com.placement.Placement.helper.convert.dto.Dto;
import com.placement.Placement.helper.convert.entity.Entity;
import com.placement.Placement.model.entity.Batch;
import com.placement.Placement.model.request.BatchRequest;
import com.placement.Placement.model.response.BatchResponse;
import com.placement.Placement.repository.BatchRepository;
import com.placement.Placement.service.BatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BatchServiceImpl implements BatchService {
    private final BatchRepository batchRepository;

    @Override
    public List<BatchResponse> getAll() {
        return batchRepository.findAll().stream()
                .map(Entity::convertToDto).toList();
    }

    @Override
    public BatchResponse getById(String id) {
        Batch batch = batchRepository.findById(id).orElse(null);
        if (batch != null) {
            return Entity.convertToDto(batch);
        }

        return null;
    }

    @Override
    public BatchResponse create(BatchRequest batchRequest) {
        Batch batch = Batch.builder()
                .name(batchRequest.getName())
                .status(EStatus.valueOf(batchRequest.getStatus()))
                .build();
        batchRepository.save(batch);

        return Entity.convertToDto(batch);
    }

    @Override
    public BatchResponse update(BatchRequest batchRequest) {
        Batch batch = batchRepository.findById(batchRequest.getId()).orElse(null);
        if (batch != null) {
            batch.setName(batchRequest.getName());
            batch.setStatus(EStatus.valueOf(batchRequest.getStatus()));
            batchRepository.save(batch);

            return Entity.convertToDto(batch);
        }

        return null;
    }

    @Override
    public BatchResponse remove(String id) {
        Batch batch = batchRepository.findById(id).orElse(null);
        if (batch != null) {
            batch.setStatus(EStatus.NOT_ACTIVE);
            batchRepository.save(batch);
            return Entity.convertToDto(batch);
        }

        return null;
    }

    @Override
    public BatchResponse getByName(String name) {
        Batch batch = batchRepository.findByName(name).orElse(null);

        if (batch != null) {
            return Entity.convertToDto(batch);
        }

        return null;
    }
}
