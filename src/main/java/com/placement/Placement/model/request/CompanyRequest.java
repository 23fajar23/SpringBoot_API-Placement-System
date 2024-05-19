package com.placement.Placement.model.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CompanyRequest {
    private String id;
    private String name;
    private String address;
    private String phoneNumber;
}
