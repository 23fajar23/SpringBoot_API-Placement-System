package com.placement.Placement.helper.convert.dto;

import com.placement.Placement.constant.Status;
import com.placement.Placement.model.entity.Batch;
import com.placement.Placement.model.entity.Company;
import com.placement.Placement.model.entity.Education;
import com.placement.Placement.model.request.BatchRequest;
import com.placement.Placement.model.request.CompanyRequest;
import com.placement.Placement.model.request.EducationRequest;
import com.placement.Placement.model.response.BatchResponse;
import com.placement.Placement.model.response.CompanyResponse;
import com.placement.Placement.model.response.EducationResponse;

public class Dto {
    public static Batch convertToEntity(BatchResponse batchResponse) {
        return Batch.builder()
                .id(batchResponse.getId())
                .name(batchResponse.getName())
                .status(batchResponse.getStatus())
                .build();
    }

    public static Batch convertToEntity(BatchRequest batchRequest) {
        return Batch.builder()
                .id(batchRequest.getId())
                .name(batchRequest.getName())
                .status(Status.ACTIVE)
                .build();
    }

    public static Education convertToEntity(EducationRequest educationRequest) {
        return Education.builder()
                .id(educationRequest.getId())
                .education(educationRequest.getEducation())
                .value(educationRequest.getValue())
                .build();
    }

    public static Education convertToEntity(EducationResponse educationResponse) {
        return Education.builder()
                .id(educationResponse.getId())
                .education(educationResponse.getEducation())
                .value(educationResponse.getValue())
                .build();
    }

    public static Company convertToEntity(CompanyResponse companyResponse) {
        return Company.builder()
                .id(companyResponse.getId())
                .name(companyResponse.getName())
                .phoneNumber(companyResponse.getPhoneNumber())
                .address(companyResponse.getAddress())
                .status(companyResponse.getStatus())
                .build();
    }

    public static Company convertToEntity(CompanyRequest companyRequest) {
        return Company.builder()
                .id(companyRequest.getId())
                .name(companyRequest.getName())
                .address(companyRequest.getAddress())
                .phoneNumber(companyRequest.getPhoneNumber())
                .status(Status.ACTIVE)
                .build();
    }
}
