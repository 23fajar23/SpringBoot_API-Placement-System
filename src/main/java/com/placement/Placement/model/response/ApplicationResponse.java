package com.placement.Placement.model.response;

import com.placement.Placement.model.entity.Test;
import com.placement.Placement.model.entity.auth.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ApplicationResponse {
    private String id;
    private Customer customer;
    private TestResponse test;
}
