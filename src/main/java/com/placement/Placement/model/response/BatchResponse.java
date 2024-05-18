package com.placement.Placement.model.response;

import com.placement.Placement.constant.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class BatchResponse {
    private String id;
    private String name;
    private Status status;
}
