package com.placement.Placement.controller;

import com.placement.Placement.constant.AppPath;
import com.placement.Placement.model.request.EducationRequest;
import com.placement.Placement.model.response.CommonResponse;
import com.placement.Placement.model.response.EducationResponse;
import com.placement.Placement.service.EducationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppPath.API + AppPath.EDUCATION)
public class EducationController {
    private final EducationService educationService;

    @GetMapping
    public ResponseEntity<?> getAllEducations() {
        return educationService.getAll();
    }

    @GetMapping(AppPath.BY_ID)
    public ResponseEntity<?> getEducationById(@PathVariable String id) {
        return educationService.getById(id);
    }

    @PostMapping
    public ResponseEntity<?> createEducation(@RequestBody EducationRequest educationRequest) {
        return educationService.create(educationRequest);
    }

    @PutMapping
    public ResponseEntity<?> updateEducation(@RequestBody EducationRequest educationRequest) {
        return educationService.update(educationRequest);
    }

    @DeleteMapping(AppPath.BY_ID)
    public ResponseEntity<?> deleteEducation(@PathVariable String id) {
        return educationService.remove(id);

    }
}
