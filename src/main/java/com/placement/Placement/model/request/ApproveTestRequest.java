package com.placement.Placement.model.request;

import com.placement.Placement.constant.EResultTest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ApproveTestRequest {
    private String id;
    private String applicationId;
    private String stageId;
    private EResultTest resultTest;
}
