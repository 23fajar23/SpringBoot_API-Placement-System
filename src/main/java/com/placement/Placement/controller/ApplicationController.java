package com.placement.Placement.controller;

import com.placement.Placement.constant.AppPath;
import com.placement.Placement.model.request.ApplicationRequest;
import com.placement.Placement.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppPath.API + AppPath.USER_PLACMENT + AppPath.JOIN)
public class ApplicationController {
    private final ApplicationService applicationService;
    @PostMapping
    public ResponseEntity<Object> createApplication(@RequestBody ApplicationRequest applicationRequest) {
        return applicationService.create(applicationRequest);
    }
}
