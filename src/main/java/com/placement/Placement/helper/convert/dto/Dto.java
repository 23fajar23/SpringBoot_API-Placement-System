package com.placement.Placement.helper.convert.dto;

import com.placement.Placement.constant.EStatus;
import com.placement.Placement.model.entity.Batch;
import com.placement.Placement.model.entity.Company;
import com.placement.Placement.model.entity.Education;
import com.placement.Placement.model.entity.Test;
import com.placement.Placement.model.request.BatchRequest;
import com.placement.Placement.model.request.CompanyRequest;
import com.placement.Placement.model.request.EducationRequest;
import com.placement.Placement.model.request.TestRequest;
import com.placement.Placement.model.response.BatchResponse;
import com.placement.Placement.model.response.CompanyResponse;
import com.placement.Placement.model.response.EducationResponse;
import com.placement.Placement.model.response.TestResponse;

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
                .status(EStatus.ACTIVE)
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
                .status(EStatus.ACTIVE)
                .build();
    }

    public static Test convertToEntity(TestResponse testResponse) {
        return Test.builder()
                .id(testResponse.getId())
                .placement(testResponse.getPlacement())
                .note(testResponse.getNote())
                .company(testResponse.getCompany())
                .education(testResponse.getEducation())
                .status(testResponse.getStatus())
                .build();
    }

    public static Test convertToEntity(TestRequest testRequest) {
        return Test.builder()
                .id(testRequest.getId())
                .placement(testRequest.getPlacement())
                .note(testRequest.getNote())
                .company(null)
                .education(null)
                .status(EStatus.ACTIVE)
                .build();
    }
}
