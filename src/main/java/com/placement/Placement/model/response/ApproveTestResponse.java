package com.placement.Placement.model.response;

import com.placement.Placement.constant.EResultTest;
import com.placement.Placement.model.entity.Application;
import com.placement.Placement.model.entity.Stage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ApproveTestResponse {
    private Application application;
    private Stage stage;
}
