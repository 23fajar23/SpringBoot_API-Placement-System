package com.placement.Placement.controller;

import com.placement.Placement.constant.AppPath;
import com.placement.Placement.model.request.BatchRequest;
import com.placement.Placement.model.response.BatchResponse;
import com.placement.Placement.model.response.CommonResponse;
import com.placement.Placement.model.response.EducationResponse;
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
        List<BatchResponse> batches = batchService.getAll();

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully get all batches")
                        .data(batches)
                        .build());

    }

    @GetMapping(AppPath.BY_ID)
    public ResponseEntity<?> getBatchById(@PathVariable String id) {
        BatchResponse batch = batchService.getById(id);

        CommonResponse<BatchResponse> response;
        HttpStatus httpStatus;
        if (batch !=  null) {
            httpStatus = HttpStatus.OK;
            response = CommonResponse.<BatchResponse>builder()
                    .statusCode(httpStatus.value())
                    .message("Successfully get batch")
                    .data(batch)
                    .build();
        } else {
            httpStatus = HttpStatus.NOT_FOUND;
            response = CommonResponse.<BatchResponse>builder()
                    .statusCode(httpStatus.value())
                    .message("Batch not found")
                    .data(null)
                    .build();
        }

        return ResponseEntity
                .status(httpStatus)
                .body(response);

    }

    @PostMapping(AppPath.CREATE)
    public ResponseEntity<?> createBatch(@RequestBody BatchRequest batchRequest) {
        BatchResponse batch = batchService.create(batchRequest);

        CommonResponse<BatchResponse> response = CommonResponse.<BatchResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Successfully create batch")
                .data(batch)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping(AppPath.UPDATE)
    public ResponseEntity<?> updateBatch(@RequestBody BatchRequest batchRequest) {
        BatchResponse batch = batchService.update(batchRequest);

        CommonResponse<BatchResponse> response;
        HttpStatus httpStatus;
        if (batch !=  null) {
            httpStatus = HttpStatus.OK;
            response = CommonResponse.<BatchResponse>builder()
                    .statusCode(httpStatus.value())
                    .message("Successfully update batch")
                    .data(batch)
                    .build();
        } else {
            httpStatus = HttpStatus.NOT_FOUND;
            response = CommonResponse.<BatchResponse>builder()
                    .statusCode(httpStatus.value())
                    .message("Batch not found")
                    .data(null)
                    .build();
        }

        return ResponseEntity
                .status(httpStatus)
                .body(response);

    }

    @DeleteMapping(AppPath.REMOVE + AppPath.BY_ID)
    public ResponseEntity<?> deleteBatch(@PathVariable String id) {
        BatchResponse batch = batchService.remove(id);

        CommonResponse<BatchResponse> response;
        HttpStatus httpStatus;
        if (batch !=  null) {
            httpStatus = HttpStatus.OK;
            response = CommonResponse.<BatchResponse>builder()
                    .statusCode(httpStatus.value())
                    .message("Successfully remove batch")
                    .data(batch)
                    .build();
        } else {
            httpStatus = HttpStatus.NOT_FOUND;
            response = CommonResponse.<BatchResponse>builder()
                    .statusCode(httpStatus.value())
                    .message("Batch not found")
                    .data(null)
                    .build();
        }

        return ResponseEntity
                .status(httpStatus)
                .body(response);
    }
}
