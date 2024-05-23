package com.placement.Placement.model.request;

import com.placement.Placement.constant.EQuota;
import com.placement.Placement.constant.EStage;
import com.placement.Placement.constant.EType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class StageRequest {
    private String id;
    private String testId;
    private String nameStage;
    private LocalDate dateTime;
    private EType typeStage;
    private EStage stageStatus;
}
