package com.placement.Placement.model.request;

import com.placement.Placement.constant.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AdminRequest {
    private String id;
    private String name;
    private String mobilePhone;
    private EStatus status;
}
