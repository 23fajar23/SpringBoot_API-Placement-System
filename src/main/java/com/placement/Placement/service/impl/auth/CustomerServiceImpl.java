package com.placement.Placement.service.impl.auth;

import com.placement.Placement.constant.EStatus;
import com.placement.Placement.helper.convert.dto.Dto;
import com.placement.Placement.helper.convert.entity.Entity;
import com.placement.Placement.helper.response.Response;
import com.placement.Placement.model.entity.auth.Customer;
import com.placement.Placement.model.entity.auth.UserCredential;
import com.placement.Placement.model.request.CustomerRequest;
import com.placement.Placement.model.response.*;
import com.placement.Placement.repository.auth.CustomerRepository;
import com.placement.Placement.repository.auth.UserCredentialRepository;
import com.placement.Placement.service.BatchService;
import com.placement.Placement.service.EducationService;
import com.placement.Placement.service.auth.CustomerService;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final BatchService batchService;
    private final EducationService educationService;
    private final UserCredentialRepository userCredentialRepository;

    @Override
    public ResponseEntity<Object> getAll() {
        List<CustomerResponse> customers = customerRepository.findAll()
                .stream()
                .map(Entity::convertToDto)
                .toList();

        return Response.responseData(HttpStatus.OK, "Successfully get all customers", customers, null);
    }

    @Override
    public ResponseEntity<Object> getById(String id) {
        Customer customer = customerRepository.findById(id).orElse(null);

        if (customer != null) {
            return Response.responseData(HttpStatus.OK, "Successfully get customer", Entity.convertToDto(customer), null);
        }

        return Response.responseData(HttpStatus.NOT_FOUND, "Customer is not found", null, null);
    }

    @Override
    public CustomerResponse findById(String id) {
        Customer customer = customerRepository.findById(id).orElse(null);

        if (customer != null) {
            return Entity.convertToDto(customer);
        }

        return null;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<Object> update(CustomerRequest customerRequest) {
        Customer customer = customerRepository.findById(customerRequest.getId())
                .orElse(null);

        if (customer != null) {

            BatchResponse batchResponse = batchService.findById(customerRequest.getBatchId());
            EducationResponse educationResponse = educationService.findById(customerRequest.getEducationId());

            UserCredential customerUserCredential = customer.getUserCredential();

            if (customerUserCredential == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer user credential is null");
            }

            UserCredential userCredential = userCredentialRepository.findById(customerUserCredential.getId())
                    .orElse(null);

            if (userCredential == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer with id "
                        + customerUserCredential.getId() + " is not found");
            }

            if (batchResponse == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Batch with id "
                        + customerRequest.getBatchId() + " is not found");
            }

            if (educationResponse == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Education with id "
                        + customerRequest.getEducationId() + " is not found");
            }

            customer.setName(customerRequest.getName());
            customer.setAddress(customerRequest.getAddress());
            customer.setMobilePhone(customerRequest.getPhoneNumber());
            customer.setBatch(Dto.convertToEntity(batchResponse));
            customer.setEducation(Dto.convertToEntity(educationResponse));

            userCredential.setStatus(customerRequest.getStatus());

            userCredentialRepository.saveAndFlush(userCredential);
            customerRepository.save(customer);

            return Response.responseData(HttpStatus.OK, "Successfully update customer", customer, null);
        }

        return Response.responseData(HttpStatus.NOT_FOUND, "Customer is not found", null, null);
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public ResponseEntity<Object> remove(String id) {
        Customer customer = customerRepository.findById(id)
                .orElse(null);

        if (customer != null) {

            UserCredential customerUserCredential = customer.getUserCredential();

            if (customerUserCredential == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer user credential is null");
            }

            UserCredential userCredential = userCredentialRepository.findById(customerUserCredential.getId())
                    .orElse(null);

            if (userCredential == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User credential with id "
                        + customerUserCredential.getId()
                        + " is not found");
            }

            userCredential.setStatus(EStatus.NOT_ACTIVE);

            userCredentialRepository.save(userCredential);

            return Response.responseData(HttpStatus.OK, "Successfully remove customer", customer, null);
        }

        return Response.responseData(HttpStatus.NOT_FOUND, "Customer is not found", null, null);
    }

    public ResponseEntity<Object> getAllByName(String name, Integer page, Integer size) {
        Specification<Customer> specification = (root , query , criteriaBuilder) ->{
            List<Predicate> predicates = new ArrayList<>();
            if (name != null){
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(page,size);
        Page<Customer> customers = customerRepository.findAll(specification,pageable);

        List<CustomerResponse> customerResponses = new ArrayList<>();
        for (Customer customer : customers.getContent()){
            customerResponses.add(Entity.convertToDto(customer));
        }

        PageImpl<CustomerResponse> results = new PageImpl<>(customerResponses,pageable,customers.getTotalElements());

        PagingResponse pagingResponse = PagingResponse.builder()
                .currentPage(page)
                .totalPage(customers.getTotalPages())
                .size(size)
                .build();

        return Response.responseData(HttpStatus.OK, "Success get all customers by name", results, pagingResponse);
    }

    @Override
    public CustomerResponse findByUserCredentialId(String userCredential) {
        Customer customer = customerRepository.findByUserCredentialId(userCredential).orElse(null);

        if (customer != null) {
            return Entity.convertToDto(customer);
        }

        return null;
    }
}
