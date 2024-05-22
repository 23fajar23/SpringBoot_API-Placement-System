package com.placement.Placement.model.response;

import com.placement.Placement.constant.EQuota;
import com.placement.Placement.constant.EStatus;
import com.placement.Placement.constant.EType;
import com.placement.Placement.model.entity.Quota;
import com.placement.Placement.model.entity.Test;
import com.placement.Placement.model.request.QuotaBatchRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class StageResponse {
    private String id;
    private String name;
    private EType type;
    private LocalDate dateTime;
    private int totalQuota;
    private int quotaAvailable;
    private EQuota quotaBatchType;
    private List<Quota> quotas;
}
