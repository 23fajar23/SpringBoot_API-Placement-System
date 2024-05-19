package com.placement.Placement.controller;

import com.placement.Placement.constant.AppPath;
import com.placement.Placement.model.request.StageRequest;
import com.placement.Placement.model.response.CommonResponse;
import com.placement.Placement.model.response.StageResponse;
import com.placement.Placement.service.StageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppPath.API + AppPath.STAGE)
public class StageController {

    private final StageService stageService;
    @PostMapping(AppPath.CREATE)
    public ResponseEntity<?> createStage(@RequestBody StageRequest stageRequest) {
        StageResponse stageResponse = stageService.create(stageRequest);

        CommonResponse<StageResponse> response = CommonResponse.<StageResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Successfully create new stage")
                .data(stageResponse)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
