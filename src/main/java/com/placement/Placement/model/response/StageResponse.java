package com.placement.Placement.model.response;

import com.placement.Placement.constant.EStatus;
import com.placement.Placement.constant.EType;
import com.placement.Placement.model.entity.Test;
import com.placement.Placement.model.request.QuotaBatchRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class StageResponse {
    private String id;
    private String name;
    private LocalDateTime dateTime;
    private EType type;
    private Test test;
    private int totalQuota;
    private int quotaAvailable;
    private EStatus status;
    private List<QuotaBatchResponse> quotaBatchResponses;
}
