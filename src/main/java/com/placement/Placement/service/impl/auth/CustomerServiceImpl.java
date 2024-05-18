package com.placement.Placement.service.impl.auth;

import com.placement.Placement.model.entity.auth.Customer;
import com.placement.Placement.repository.auth.CustomerRepository;
import com.placement.Placement.service.auth.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }
}
