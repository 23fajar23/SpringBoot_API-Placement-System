package com.placement.Placement.helper.convert.entity;

import com.placement.Placement.model.entity.*;
import com.placement.Placement.model.entity.auth.Admin;
import com.placement.Placement.model.entity.auth.Customer;
import com.placement.Placement.model.entity.auth.SuperAdmin;
import com.placement.Placement.model.response.*;

import java.util.List;

public class Entity {
    public static BatchResponse convertToDto(Batch batch) {
        return BatchResponse.builder()
                .id(batch.getId())
                .name(batch.getName())
                .region(batch.getRegion())
                .status(batch.getStatus())
                .build();
    }

    public static EducationResponse convertToDto(Education education) {
        return EducationResponse.builder()
                .id(education.getId())
                .name(education.getName())
                .value(education.getValue())
                .build();
    }

    public static CompanyResponse convertToDto(Company company) {
        return CompanyResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .address(company.getAddress())
                .mobilePhone(company.getPhoneNumber())
                .status(company.getStatus())
                .build();
    }

    public static AdminResponse convertToDto(Admin admin) {
        return AdminResponse.builder()
                .id(admin.getId())
                .name(admin.getName())
                .mobilePhone(admin.getPhoneNumber())
                .userCredential(admin.getUserCredential())
                .build();
    }

    public static CustomerResponse convertToDto(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .address(customer.getAddress())
                .mobilePhone(customer.getMobilePhone())
                .batch(customer.getBatch())
                .education(customer.getEducation())
                .userCredential(customer.getUserCredential())
                .applications(customer.getApplications())
                .build();
    }

    public static CustomerLoginResponse convertToDto(Customer customer, List<ApplicationTestResponse> applicationTestResponse) {
        return CustomerLoginResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .address(customer.getAddress())
                .mobilePhone(customer.getMobilePhone())
                .batch(customer.getBatch())
                .education(customer.getEducation())
                .userCredential(customer.getUserCredential())
                .applications(applicationTestResponse)
                .build();
    }

    public static MessageResponse convertToDto(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .customer_id(message.getRecipient().getId())
                .sender(message.getSender())
                .content(message.getContent())
                .read(message.getRead())
                .status(message.getStatus())
                .build();
    }

    public static StageResponse convertToDto(Stage stage) {
        return StageResponse.builder()
                .id(stage.getId())
                .nameStage(stage.getName())
                .typeStage(stage.getType())
                .dateTime(stage.getDateTime())
                .test(stage.getTest())
                .stageStatus(stage.getStageStatus())
                .build();
    }

    public static SuperAdminResponse convertToDto(SuperAdmin superAdmin) {
        return SuperAdminResponse.builder()
                .id(superAdmin.getId())
                .name(superAdmin.getName())
                .mobilePhone(superAdmin.getPhoneNumber())
                .userCredential(superAdmin.getUserCredential())
                .build();
    }
}
