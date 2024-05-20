package com.placement.Placement.service.impl;

import com.placement.Placement.constant.EStatus;
import com.placement.Placement.helper.convert.dto.Dto;
import com.placement.Placement.helper.convert.entity.Entity;
import com.placement.Placement.helper.response.Response;
import com.placement.Placement.model.entity.Batch;
import com.placement.Placement.model.request.BatchRequest;
import com.placement.Placement.model.response.BatchResponse;
import com.placement.Placement.repository.BatchRepository;
import com.placement.Placement.service.BatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BatchServiceImpl implements BatchService {
    private final BatchRepository batchRepository;

    @Override
    public ResponseEntity<Object> getAll() {
        List<BatchResponse> batchResponseList = batchRepository.findAll().stream()
                .map(Entity::convertToDto).toList();
        return Response.responseData(HttpStatus.OK, "Successfully get all batches", batchResponseList);
    }

    @Override
    public ResponseEntity<Object> getById(String id) {
        Batch batch = batchRepository.findById(id).orElse(null);
        if (batch != null) {
            return Response.responseData(HttpStatus.OK, "Successfully get batch", batch);
        }else{
            return Response.responseData(HttpStatus.NOT_FOUND, "Batch not found", null);
        }
    }

    @Override
    public BatchResponse findById(String id) {
        Batch batch = batchRepository.findById(id).orElse(null);
        if (batch != null){
            return Entity.convertToDto(batch);
        }

        return  null;
    }

    @Override
    public ResponseEntity<Object> create(BatchRequest batchRequest) {
        Batch batch = batchRepository.findByName(batchRequest.getName()).orElse(null);
        try {
            if (batch == null){
                batch = Dto.convertToEntity(batchRequest);
                batchRepository.save(batch);
                return Response.responseData(HttpStatus.OK, "Successfully create batch", batch);
            }else {
                return Response.responseData(HttpStatus.FOUND, "Batch already exist", null);
            }
        }catch (Exception e){
            return Response.responseData(HttpStatus.BAD_REQUEST, "Status Invalid", null);
        }
    }

    @Override
    public ResponseEntity<Object> update(BatchRequest batchRequest) {
        Batch batch = batchRepository.findById(batchRequest.getId()).orElse(null);
        if (batch != null) {
            batch.setName(batchRequest.getName());
            batch.setStatus(EStatus.valueOf(batchRequest.getStatus()));
            batchRepository.save(batch);
            return Response.responseData(HttpStatus.OK, "Successfully update batch", batch);
        }else{
            return Response.responseData(HttpStatus.NOT_FOUND, "Batch not found", null);
        }
    }

    @Override
    public ResponseEntity<Object> remove(String id) {
        Batch batch = batchRepository.findById(id).orElse(null);
        if (batch != null) {
            batch.setStatus(EStatus.NOT_ACTIVE);
            batchRepository.save(batch);
            return Response.responseData(HttpStatus.OK, "Successfully remove batch", batch);
        }else{
            return Response.responseData(HttpStatus.NOT_FOUND, "Batch not found", null);
        }
    }

    @Override
    public BatchResponse findByName(String name) {
        Batch batch = batchRepository.findByName(name).orElse(null);

        if (batch != null) {
            return Entity.convertToDto(batch);
        }

        return null;
    }
}
