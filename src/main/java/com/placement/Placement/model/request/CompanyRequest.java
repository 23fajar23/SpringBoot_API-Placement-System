package com.placement.Placement.model.request;

import com.placement.Placement.constant.Status;
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
