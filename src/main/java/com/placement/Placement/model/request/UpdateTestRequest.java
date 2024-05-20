package com.placement.Placement.model.request;

import com.placement.Placement.constant.EQuota;
import com.placement.Placement.constant.EStatus;
import com.placement.Placement.constant.EType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UpdateTestRequest {
    private String id;
    private String placement;
    private String note;
    private String companyId;
    private String educationId;
    private EStatus statusTest;
    private String nameStage;
    private LocalDateTime dateTime;
    private EType typeStage;
    private int totalQuota;
    private int quotaAvailable;
    private EQuota typeQuota;
}
