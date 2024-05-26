package com.placement.Placement.service.auth;

import com.placement.Placement.helper.response.Response;
import com.placement.Placement.model.entity.auth.Customer;
import com.placement.Placement.model.request.CustomerRequest;
import com.placement.Placement.model.response.AdminResponse;
import com.placement.Placement.model.response.CustomerResponse;
import org.springframework.http.ResponseEntity;

public interface CustomerService {
    ResponseEntity<Object> getAll();
    ResponseEntity<Object> getById(String id);
    CustomerResponse findById(String id);
    ResponseEntity<Object> update(CustomerRequest customerRequest);
    Customer save(Customer customer);
    ResponseEntity<Object> remove(String id);
    CustomerResponse findByUserCredentialId(String userCredential);
    ResponseEntity<Object> getAllByName(String name, Integer page, Integer size);
}
