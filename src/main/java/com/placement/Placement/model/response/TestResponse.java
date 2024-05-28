package com.placement.Placement.model.response;

import com.placement.Placement.constant.EQuota;
import com.placement.Placement.constant.EStatus;
import com.placement.Placement.constant.EType;
import com.placement.Placement.model.entity.Company;
import com.placement.Placement.model.entity.Education;
import com.placement.Placement.model.entity.Quota;
import com.placement.Placement.model.entity.Stage;
import com.placement.Placement.model.request.QuotaBatchRequest;
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
public class TestResponse {
    private String id;
    private String placement;
    private String note;
    private String rolePlacement;
    private EStatus statusTest;
    private List<StageResponse> stages;
    private Education education;
    private Company company;
}
