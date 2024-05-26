package com.placement.Placement.controller;


import com.placement.Placement.constant.AppPath;
import com.placement.Placement.model.request.AuthRequest;
import com.placement.Placement.model.response.CommonResponse;
import com.placement.Placement.model.response.LoginResponse;
import com.placement.Placement.model.response.RegisterResponse;
import com.placement.Placement.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = AppPath.API + AppPath.AUTH)
public class AuthController {

    private final AuthService authService;

    @PostMapping(AppPath.REGISTER + AppPath.CUSTOMER)
    public ResponseEntity<?> registerCustomer(@RequestBody AuthRequest authRequest){
        RegisterResponse registerResponse = authService.registerCustomer(authRequest);

        CommonResponse<RegisterResponse> response = CommonResponse.<RegisterResponse>builder()
                .message("Successfully register new customer")
                .statusCode(HttpStatus.CREATED.value())
                .data(registerResponse)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping(AppPath.REGISTER + AppPath.ADMIN)
    public ResponseEntity<?> registerAdmin(@RequestBody AuthRequest authRequest){
        RegisterResponse registerResponse = authService.registerAdmin(authRequest);

        CommonResponse<RegisterResponse> response = CommonResponse.<RegisterResponse>builder()
                .message("Successfully register new Admim")
                .statusCode(HttpStatus.CREATED.value())
                .data(registerResponse)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping(AppPath.REGISTER + AppPath.SUPER_ADMIN)
    public ResponseEntity<?> registerSuperAdmin(@RequestBody AuthRequest authRequest){
        RegisterResponse registerResponse = authService.registerSuperAdmin(authRequest);

        CommonResponse<RegisterResponse> response = CommonResponse.<RegisterResponse>builder()
                .message("Successfully register new Super Admin")
                .statusCode(HttpStatus.CREATED.value())
                .data(registerResponse)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping(AppPath.LOGIN)
    public ResponseEntity<?> login(@RequestBody AuthRequest request){
        LoginResponse loginResponse = authService.login(request);

        HttpStatus httpStatus;
        CommonResponse<LoginResponse> response;
        if (loginResponse != null) {
            httpStatus = HttpStatus.OK;
            response = CommonResponse.<LoginResponse>builder()
                    .message("Success Login")
                    .statusCode(httpStatus.value())
                    .data(loginResponse)
                    .build();
        } else {
            httpStatus = HttpStatus.BAD_REQUEST;
            response = CommonResponse.<LoginResponse>builder()
                    .message("Failed Login")
                    .statusCode(httpStatus.value())
                    .data(null)
                    .build();
        }

        return ResponseEntity.status(httpStatus).body(response);
    }

    @PostMapping(AppPath.LOGIN + AppPath.MOBILE)
    public ResponseEntity<?> loginMobile(@RequestBody AuthRequest request){
        LoginResponse loginResponse = authService.loginMobile(request);

        HttpStatus httpStatus;
        CommonResponse<LoginResponse> response;
        if (loginResponse != null) {
            httpStatus = HttpStatus.OK;
            response = CommonResponse.<LoginResponse>builder()
                    .message("Success Login")
                    .statusCode(httpStatus.value())
                    .data(loginResponse)
                    .build();
        } else {
            httpStatus = HttpStatus.BAD_REQUEST;
            response = CommonResponse.<LoginResponse>builder()
                    .message("Failed Login")
                    .statusCode(httpStatus.value())
                    .data(null)
                    .build();
        }

        return ResponseEntity.status(httpStatus).body(response);
    }

}
