package com.placement.Placement.model.request;


import com.placement.Placement.constant.ERegion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class BatchRequest {
    private String id;
    private String name;
    private ERegion region;
    private String status;
}
