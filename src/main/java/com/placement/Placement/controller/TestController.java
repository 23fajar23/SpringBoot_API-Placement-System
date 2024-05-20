package com.placement.Placement.controller;

import com.placement.Placement.constant.AppPath;
import com.placement.Placement.model.request.TestRequest;
import com.placement.Placement.model.response.CommonResponse;
import com.placement.Placement.model.response.TestResponse;
import com.placement.Placement.service.TestService;
import com.placement.Placement.service.impl.TestServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppPath.API + AppPath.PLACEMENT)
public class TestController {

    private final TestService testService;

    @GetMapping("/hello/customer")
    public String hello(){
        return "nice customer";
    }

    @GetMapping("/hello/admin")
    public String helloAdmin(){
        return "nice admin";
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
}
