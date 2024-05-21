package com.placement.Placement.controller;

import com.placement.Placement.constant.AppPath;
import com.placement.Placement.model.request.CompanyRequest;
import com.placement.Placement.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = AppPath.API + AppPath.COMPANY)
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping
    public ResponseEntity<?> getAllCompanies() {
        return companyService.getAll();
    }

    @GetMapping(AppPath.BY_ID)
    public ResponseEntity<?> getCompanyById(@PathVariable String id) {
        return companyService.getById(id);
    }

    @PostMapping
    public ResponseEntity<?> createCompany(@RequestBody CompanyRequest companyRequest) {
        return companyService.create(companyRequest);
    }

    @PutMapping
    public ResponseEntity<?> updateCompany(@RequestBody CompanyRequest companyRequest) {
        return companyService.update(companyRequest);
    }

    @DeleteMapping(AppPath.BY_ID)
    public ResponseEntity<?> removeCompany(@PathVariable String id) {
        return companyService.remove(id);
    }
}
