package com.placement.Placement.controller;

import com.placement.Placement.constant.AppPath;
import com.placement.Placement.model.request.StageRequest;
import com.placement.Placement.service.StageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppPath.API + AppPath.STAGE)
public class StageController {

    private final StageService stageService;
    @PostMapping
    public ResponseEntity<?> createStage(@RequestBody StageRequest stageRequest) {
        return stageService.create(stageRequest);
    }

    @PutMapping
    public ResponseEntity<?> updateStage(@RequestBody StageRequest stageRequest) {
       return stageService.update(stageRequest);
    }
}
