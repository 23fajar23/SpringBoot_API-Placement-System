package com.placement.Placement.controller;

import com.placement.Placement.constant.AppPath;
import com.placement.Placement.model.request.TestRequest;
import com.placement.Placement.model.response.*;
import com.placement.Placement.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppPath.API + AppPath.PLACEMENT)
public class TestController {

    private final TestService testService;

    @GetMapping
    public ResponseEntity<?> getAllTests(){
        List<GetTestResponse> testResponses = testService.getAll();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully get all tests")
                        .data(testResponses)
                        .build());

    }

    @GetMapping(AppPath.BY_ID)
    public ResponseEntity<?> getTestById(@PathVariable String id){
        GetTestResponse testResponse = testService.getById(id);

        CommonResponse<GetTestResponse> response;
        HttpStatus httpStatus;
        if (testResponse != null) {
            httpStatus = HttpStatus.OK;
            response = CommonResponse.<GetTestResponse>builder()
                    .statusCode(httpStatus.value())
                    .message("Successfully get test")
                    .data(testResponse)
                    .build();
        } else {
            httpStatus = HttpStatus.NOT_FOUND;
            response = CommonResponse.<GetTestResponse>builder()
                    .statusCode(httpStatus.value())
                    .message("Failed get test")
                    .data(null)
                    .build();
        }

        return ResponseEntity.status(httpStatus)
                .body(response);
    }

    @PostMapping
    public ResponseEntity<?> createTest(@RequestBody TestRequest testRequest){
        TestResponse testResponse = testService.create(testRequest);

        CommonResponse<TestResponse> response = CommonResponse.<TestResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully create new test")
                .data(testResponse)
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);

    }

    @PutMapping
    public ResponseEntity<?> updateTest(@RequestBody TestRequest testRequest) {
        UpdateTestResponse test = testService.update(testRequest);

        HttpStatus httpStatus;
        CommonResponse<UpdateTestResponse> response;
        if (test != null) {
            httpStatus = HttpStatus.OK;
            response = CommonResponse.<UpdateTestResponse>builder()
                    .statusCode(httpStatus.value())
                    .message("Successfully update test")
                    .data(test)
                    .build();
        } else {
            httpStatus = HttpStatus.NOT_FOUND;
            response = CommonResponse.<UpdateTestResponse>builder()
                    .statusCode(httpStatus.value())
                    .message("Update is not found")
                    .data(null)
                    .build();
        }

        return ResponseEntity
                .status(httpStatus)
                .body(response);
    }

    @DeleteMapping(AppPath.BY_ID)
    public ResponseEntity<?> removeTest(@PathVariable String id) {
        TestRemoveResponse testRemoveResponse = testService.remove(id);

        CommonResponse<TestRemoveResponse> response;
        HttpStatus httpStatus;
        if (testRemoveResponse != null) {
            httpStatus = HttpStatus.OK;
            response = CommonResponse.<TestRemoveResponse>builder()
                    .statusCode(httpStatus.value())
                    .message("Successfully remove test")
                    .data(testRemoveResponse)
                    .build();
        } else {
            httpStatus = HttpStatus.NOT_FOUND;
            response = CommonResponse.<TestRemoveResponse>builder()
                    .statusCode(httpStatus.value())
                    .message("Failed remove test")
                    .data(null)
                    .build();
        }

        return ResponseEntity.status(httpStatus)
                .body(response);
    }
}
