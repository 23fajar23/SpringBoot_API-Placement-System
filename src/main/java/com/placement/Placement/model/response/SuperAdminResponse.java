package com.placement.Placement.model.response;

import com.placement.Placement.constant.EStatus;
import com.placement.Placement.model.entity.auth.UserCredential;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SuperAdminResponse {
    private String id;
    private String name;
    private String mobilePhone;
    private UserCredential userCredential;
}
