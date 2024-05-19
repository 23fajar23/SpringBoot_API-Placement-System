package com.placement.Placement.model.request;

import com.placement.Placement.constant.EQuota;
import com.placement.Placement.constant.EStatus;
import com.placement.Placement.constant.EType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class StageRequest {
    private String id;
    private String name;
    private LocalDateTime dateTime;
    private EType type;
    private String testId;
    private String educationId;
    private int totalQuota;
    private int quotaAvailable;
    private EQuota quotaBatch;
    private EStatus status;
    private List<QuotaBatchRequest> quotaBatchRequestList;
}
