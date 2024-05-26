package com.placement.Placement.controller;

import com.placement.Placement.constant.AppPath;
import com.placement.Placement.model.request.ApplicationRequest;
import com.placement.Placement.model.request.ApproveTestRequest;
import com.placement.Placement.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppPath.API + AppPath.USER_PLACEMENT)
public class ApplicationController {
    private final ApplicationService applicationService;
    @PostMapping(AppPath.JOIN)
    public ResponseEntity<Object> createApplication(@RequestBody ApplicationRequest applicationRequest) {
        return applicationService.create(applicationRequest);
    }

    @PostMapping(AppPath.APPROVE)
    public ResponseEntity<Object> approveApplication(@RequestBody ApproveTestRequest approveTestRequest) {
        return applicationService.approve(approveTestRequest);
    }

    @GetMapping(AppPath.ALL)
    public ResponseEntity<Object> getAllApplications() {
        return applicationService.getAll();
    }

    @GetMapping(AppPath.BY_ID)
    public ResponseEntity<Object> getApplicationById(@PathVariable String id) {
        return applicationService.getById(id);
    }
}
