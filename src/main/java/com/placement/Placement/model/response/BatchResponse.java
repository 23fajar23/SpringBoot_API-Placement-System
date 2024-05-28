package com.placement.Placement.model.response;

import com.placement.Placement.constant.ERegion;
import com.placement.Placement.constant.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class BatchResponse {
    private String id;
    private String name;
    private ERegion region;
    private EStatus status;
}
