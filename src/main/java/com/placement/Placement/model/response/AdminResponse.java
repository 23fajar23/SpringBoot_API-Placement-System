package com.placement.Placement.model.response;

import com.placement.Placement.model.entity.auth.UserCredential;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AdminResponse {
    private String id;
    private String name;
    private String phoneNumber;
    private UserCredential userCredential;
}
