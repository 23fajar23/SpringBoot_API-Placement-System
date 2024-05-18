package com.placement.Placement.helper.convert.entity;

import com.placement.Placement.model.entity.Batch;
import com.placement.Placement.model.entity.Education;
import com.placement.Placement.model.response.BatchResponse;
import com.placement.Placement.model.response.EducationResponse;

public class Entity {
    public static BatchResponse convertToDto(Batch batch) {
        return BatchResponse.builder()
                .id(batch.getId())
                .name(batch.getName())
                .status(batch.getStatus())
                .build();
    }

    public static EducationResponse convertToDto(Education education) {
        return EducationResponse.builder()
                .id(education.getId())
                .education(education.getEducation())
                .value(education.getValue())
                .build();
    }
}
