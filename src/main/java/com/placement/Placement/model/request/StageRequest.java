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
public class StageRequest {
    private String id;
    private String testId;
    private String educationId;
    private String name;
    private EType type;
    private LocalDateTime dateTime;
    private EQuota quotaBatchType;
    private int quotaTotal;
    private EStatus status;
}
