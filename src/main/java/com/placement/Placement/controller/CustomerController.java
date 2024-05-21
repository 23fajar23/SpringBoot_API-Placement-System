package com.placement.Placement.controller;

import com.placement.Placement.constant.AppPath;
import com.placement.Placement.model.request.CustomerRequest;
import com.placement.Placement.service.auth.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppPath.API + AppPath.CUSTOMER)
public class CustomerController {
    private final CustomerService customerService;
    @GetMapping
    public ResponseEntity<?> getAllCustomers() {
        return customerService.getAll();
    }

    @GetMapping(AppPath.BY_ID)
    public ResponseEntity<?> getCustomerById(@PathVariable String id) {
        return customerService.getById(id);
    }

    @PutMapping
    public ResponseEntity<?> updateCustomer(@RequestBody CustomerRequest customerRequest) {
        return customerService.update(customerRequest);
    }

    @DeleteMapping(AppPath.BY_ID)
    public ResponseEntity<?> removeCustomer(@PathVariable String id) {
        return customerService.remove(id);
    }
}
