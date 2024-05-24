package com.placement.Placement.model.response;

import com.placement.Placement.constant.EStage;
import com.placement.Placement.constant.EType;

import com.placement.Placement.model.entity.Test;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class StageResponse {
    private String id;
    private String nameStage;
    private LocalDate dateTime;
    private Test test;
    private EType typeStage;
    private EStage stageStatus;
}
