package com.placement.Placement.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class QuotaBatchResponse {
    private String id;
    private BatchResponse batch;
    private int quotaAvailable;
}
