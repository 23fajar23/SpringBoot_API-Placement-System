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
      List<EducationResponse> educations = educationService.getAll();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully get all educations")
                        .data(educations)
                        .build());
    }

    @GetMapping(AppPath.BY_ID)
    public ResponseEntity<?> getEducationById(@PathVariable String id) {
        EducationResponse education = educationService.getById(id);

        CommonResponse<EducationResponse> response;
        HttpStatus httpStatus;
        if (education !=  null) {
            httpStatus = HttpStatus.OK;
            response = CommonResponse.<EducationResponse>builder()
                    .statusCode(httpStatus.value())
                    .message("Successfully get education")
                    .data(education)
                    .build();
        } else {
            httpStatus = HttpStatus.NOT_FOUND;
            response = CommonResponse.<EducationResponse>builder()
                    .statusCode(httpStatus.value())
                    .message("Education not found")
                    .data(null)
                    .build();
        }

        return ResponseEntity
                .status(httpStatus)
                .body(response);
    }

    @PostMapping(AppPath.CREATE)
    public ResponseEntity<?> createEducation(@RequestBody EducationRequest educationRequest) {
        EducationResponse education = educationService.create(educationRequest);
        CommonResponse<EducationResponse> response = CommonResponse.<EducationResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Successfully create education")
                .data(education)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping(AppPath.UPDATE)
    public ResponseEntity<?> updateEducation(@RequestBody EducationRequest educationRequest) {
        EducationResponse education = educationService.update(educationRequest);
        CommonResponse<EducationResponse> response;
        HttpStatus httpStatus;
        if (education !=  null) {
            httpStatus = HttpStatus.OK;
            response = CommonResponse.<EducationResponse>builder()
                    .statusCode(httpStatus.value())
                    .message("Successfully update education")
                    .data(education)
                    .build();
        } else {
            httpStatus = HttpStatus.NOT_FOUND;
            response = CommonResponse.<EducationResponse>builder()
                    .statusCode(httpStatus.value())
                    .message("Education not found")
                    .data(null)
                    .build();
        }

        return ResponseEntity
                .status(httpStatus)
                .body(response);
    }

    @DeleteMapping(AppPath.REMOVE + AppPath.BY_ID)
    public ResponseEntity<?> deleteEducation(@PathVariable String id) {
        EducationResponse education = educationService.remove(id);

        CommonResponse<EducationResponse> response;
        HttpStatus httpStatus;
        if (education !=  null) {
            httpStatus = HttpStatus.OK;
            response = CommonResponse.<EducationResponse>builder()
                    .statusCode(httpStatus.value())
                    .message("Successfully remove education")
                    .data(education)
                    .build();
        } else {
            httpStatus = HttpStatus.NOT_FOUND;
            response = CommonResponse.<EducationResponse>builder()
                    .statusCode(httpStatus.value())
                    .message("Education not found")
                    .data(null)
                    .build();
        }

        return ResponseEntity
                .status(httpStatus)
                .body(response);
    }
}
