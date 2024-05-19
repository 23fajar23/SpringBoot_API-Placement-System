package com.placement.Placement.model.request;

import com.placement.Placement.constant.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TestRequest {
    private String id;
    private String placement;
    private String note;
    private String companyId;
    private String educationId;
    private EStatus status;
}
