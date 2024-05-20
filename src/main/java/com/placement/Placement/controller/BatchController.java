package com.placement.Placement.controller;

import com.placement.Placement.constant.AppPath;
import com.placement.Placement.model.request.BatchRequest;
import com.placement.Placement.model.response.BatchResponse;
import com.placement.Placement.model.response.CommonResponse;
import com.placement.Placement.service.BatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = AppPath.API + AppPath.BATCH)
public class BatchController {

    private final BatchService batchService;

    @GetMapping
    public ResponseEntity<?> getAllBatches() {
        return batchService.getAll();
    }

    @GetMapping(AppPath.BY_ID)
    public ResponseEntity<?> getBatchById(@PathVariable String id) {
        return batchService.getById(id);
    }

    @PostMapping
    public ResponseEntity<?> createBatch(@RequestBody BatchRequest batchRequest) {
        return batchService.create(batchRequest);
    }

    @PutMapping
    public ResponseEntity<?> updateBatch(@RequestBody BatchRequest batchRequest) {
        return batchService.update(batchRequest);
    }

    @DeleteMapping(AppPath.BY_ID)
    public ResponseEntity<?> deleteBatch(@PathVariable String id) {
        return batchService.remove(id);
    }
}
