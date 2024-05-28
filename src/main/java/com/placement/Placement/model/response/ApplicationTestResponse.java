package com.placement.Placement.model.response;

import com.placement.Placement.model.entity.Application;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ApplicationTestResponse {
    private Application application;
    private TestResponse test;
}
