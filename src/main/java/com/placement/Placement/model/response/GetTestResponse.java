package com.placement.Placement.model.response;

import com.placement.Placement.constant.EStatus;
import com.placement.Placement.model.entity.Company;
import com.placement.Placement.model.entity.Education;
import com.placement.Placement.model.entity.Stage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class GetTestResponse {
    private String id;
    private String placement;
    private String note;
    private EStatus statusTest;
    private List<Stage> stages;
    private Education education;
    private Company company;
}
