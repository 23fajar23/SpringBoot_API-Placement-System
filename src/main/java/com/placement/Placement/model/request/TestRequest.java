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
public class TestRequest {

    // This Request for Stage 1
    private String id;
    private String placement;
    private String note;
    private String companyId;
    private String educationId;
    private EStatus statusTest;

    // Stage
    private String nameStage;
    private LocalDateTime dateTime;
    private EType typeStage;

    // Quota
    private int totalQuota;
    private int quotaAvailable;
    private EType typeQuota;

    // Quota Batch
    private List<QuotaBatchRequest> quotaAvailableBatch;

}
