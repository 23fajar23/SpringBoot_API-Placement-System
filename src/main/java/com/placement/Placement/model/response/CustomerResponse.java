package com.placement.Placement.model.response;

import com.placement.Placement.model.entity.Batch;
import com.placement.Placement.model.entity.Education;
import com.placement.Placement.model.entity.auth.UserCredential;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CustomerResponse {
    private String id;
    private String name;
    private String address;
    private String phoneNumber;
    private Batch batch;
    private Education education;
    private UserCredential userCredential;
}
