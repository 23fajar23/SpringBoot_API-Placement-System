package com.placement.Placement.model.request;

import com.placement.Placement.constant.EResultTest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ApplicationRequest {
    private String id;
    private String customerId;
    private String testId;
}
