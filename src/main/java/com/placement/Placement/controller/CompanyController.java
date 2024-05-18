package com.placement.Placement.controller;

import com.placement.Placement.constant.AppPath;
import com.placement.Placement.model.request.CompanyRequest;
import com.placement.Placement.model.response.CommonResponse;
import com.placement.Placement.model.response.CompanyResponse;
import com.placement.Placement.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = AppPath.API + AppPath.COMPANY)
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping
    public ResponseEntity<?> getAllCompanies() {
        List<CompanyResponse> companies = companyService.getAll();

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully get all companies")
                        .data(companies)
                        .build());
    }

    @GetMapping(AppPath.BY_ID)
    public ResponseEntity<?> getCompanyById(@PathVariable String id) {
        CompanyResponse company = companyService.getById(id);

        CommonResponse<CompanyResponse> response;
        HttpStatus httpStatus;
        if (company != null) {
            httpStatus = HttpStatus.OK;
            response = CommonResponse.<CompanyResponse>builder()
                    .statusCode(httpStatus.value())
                    .message("Successfully get company")
                    .data(company)
                    .build();
        } else {
            httpStatus = HttpStatus.NOT_FOUND;
            response = CommonResponse.<CompanyResponse>builder()
                    .statusCode(httpStatus.value())
                    .message("Company is not found")
                    .data(null)
                    .build();
        }
        return ResponseEntity
                .status(httpStatus)
                .body(response);
    }

    @PostMapping(AppPath.CREATE)
    public ResponseEntity<?> createCompany(@RequestBody CompanyRequest companyRequest) {
        CompanyResponse company = companyService.create(companyRequest);

        CommonResponse<CompanyResponse> response = CommonResponse.<CompanyResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Successfully create new company")
                .data(company)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping(AppPath.UPDATE)
    public ResponseEntity<?> updateCompany(@RequestBody CompanyRequest companyRequest) {
        CompanyResponse company = companyService.update(companyRequest);

        CommonResponse<CompanyResponse> response;
        HttpStatus httpStatus;
        if (company != null) {
            httpStatus = HttpStatus.OK;
            response = CommonResponse.<CompanyResponse>builder()
                    .statusCode(httpStatus.value())
                    .message("Successfully update company")
                    .data(company)
                    .build();
        } else {
            httpStatus = HttpStatus.NOT_FOUND;
            response = CommonResponse.<CompanyResponse>builder()
                    .statusCode(httpStatus.value())
                    .message("Company is not found")
                    .data(null)
                    .build();
        }
        return ResponseEntity
                .status(httpStatus)
                .body(response);
    }

    @DeleteMapping(AppPath.BY_ID)
    public ResponseEntity<?> removeCompany(@PathVariable String id) {
        CompanyResponse company = companyService.remove(id);

        CommonResponse<CompanyResponse> response;
        HttpStatus httpStatus;
        if (company != null) {
            httpStatus = HttpStatus.OK;
            response = CommonResponse.<CompanyResponse>builder()
                    .statusCode(httpStatus.value())
                    .message("Successfully remove company")
                    .data(company)
                    .build();
        } else {
            httpStatus = HttpStatus.NOT_FOUND;
            response = CommonResponse.<CompanyResponse>builder()
                    .statusCode(httpStatus.value())
                    .message("Company is not found")
                    .data(null)
                    .build();
        }
        return ResponseEntity
                .status(httpStatus)
                .body(response);

    }
}
