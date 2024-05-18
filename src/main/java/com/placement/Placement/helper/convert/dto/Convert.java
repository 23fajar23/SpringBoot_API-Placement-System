package com.placement.Placement.helper.convert.dto;

import com.placement.Placement.model.entity.Batch;
import com.placement.Placement.model.response.BatchResponse;

public class Convert {
    public static BatchResponse convertToDto(Batch batch) {
        return BatchResponse.builder()
                .id(batch.getId())
                .name(batch.getName())
                .status(batch.isStatus())
                .build();
    }
}
