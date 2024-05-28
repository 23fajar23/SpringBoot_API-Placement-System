package com.placement.Placement.model.response;

import com.placement.Placement.model.entity.Batch;
import com.placement.Placement.model.entity.Education;
import com.placement.Placement.model.entity.auth.UserCredential;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CustomerLoginResponse {
    private String id;
    private String name;
    private String address;
    private String phoneNumber;
    private Batch batch;
    private Education education;
    private UserCredential userCredential;
    //    private List<Application> applications;
    private List<ApplicationTestResponse> applications;
}


