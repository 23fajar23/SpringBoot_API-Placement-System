package com.placement.Placement.model.response;

import com.placement.Placement.constant.EStatus;
import com.placement.Placement.model.entity.Education;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UpdateTestResponse {
    private String id;
    private String placement;
    private String note;
    private Education education;
    private EStatus statusTest;
}
