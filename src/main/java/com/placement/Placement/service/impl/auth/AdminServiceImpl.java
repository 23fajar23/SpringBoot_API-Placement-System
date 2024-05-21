package com.placement.Placement.service.impl.auth;

import com.placement.Placement.constant.EStatus;
import com.placement.Placement.helper.convert.dto.Dto;
import com.placement.Placement.helper.convert.entity.Entity;
import com.placement.Placement.helper.response.Response;
import com.placement.Placement.model.entity.auth.Admin;
import com.placement.Placement.model.entity.auth.UserCredential;
import com.placement.Placement.model.request.AdminRequest;
import com.placement.Placement.model.response.AdminResponse;
import com.placement.Placement.repository.auth.AdminRepository;
import com.placement.Placement.repository.auth.UserCredentialRepository;
import com.placement.Placement.service.auth.AdminService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final UserCredentialRepository userCredentialRepository;

    @Override
    public ResponseEntity<Object> getAll() {
        List<AdminResponse> adminList = adminRepository.findAll()
                .stream()
                .map(Entity::convertToDto)
                .toList();

        return Response.responseData(
                HttpStatus.OK,
                "Successfully get all admin",
                adminList);

    }

    @Override
    public ResponseEntity<Object> getById(String id) {
        Admin admin = adminRepository.findById(id).orElse(null);
        if (admin != null) {
            return Response.responseData(
                    HttpStatus.OK,
                    "Successfully get admin",
                    admin);
        }

        return Response.responseData(
                HttpStatus.NOT_FOUND,
                "Admin is not found",
                null);
    }

    @Override
    public AdminResponse findById(String id) {
        Admin admin = adminRepository.findById(id).orElse(null);

        if (admin != null) {
            return Entity.convertToDto(admin);
        }

        return null;
    }

    @Override
    public AdminResponse save(AdminResponse adminResponse) {
        Admin admin = Dto.convertToEntity(adminResponse);
        adminRepository.save(admin);
        return Entity.convertToDto(admin);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<Object> update(AdminRequest adminRequest) {
        Admin admin = adminRepository.findById(adminRequest.getId()).orElse(null);

        if (admin != null) {

            UserCredential adminUserCredential = admin.getUserCredential();

            if (adminUserCredential == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin user credential is null");
            }

            UserCredential userCredential = userCredentialRepository.findById(
                    adminUserCredential.getId()).orElse(null);

            if (userCredential == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User credential with id "
                        + adminUserCredential.getId()
                        + " is not found");
            }

            admin.setName(adminRequest.getName());
            admin.setPhoneNumber(adminRequest.getPhoneNumber());
            userCredential.setStatus(adminRequest.getStatus());

            userCredentialRepository.saveAndFlush(userCredential);
            adminRepository.save(admin);

            return Response.responseData(HttpStatus.OK,
                    "Successfully update admin",
                    admin);
        }

        return Response.responseData(
                HttpStatus.NOT_FOUND,
                "Admin is not found",
                null);
    }

    @Override
    public ResponseEntity<Object> remove(String id) {
        Admin admin = adminRepository.findById(id).orElse(null);

        if (admin != null) {
            UserCredential userCredential = userCredentialRepository.findById(
                    admin.getUserCredential().getId())
                    .orElse(null);

            if (userCredential == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User credential with id "
                        + admin.getUserCredential().getId() + " is not found");
            }

            userCredential.setStatus(EStatus.NOT_ACTIVE);
            userCredentialRepository.save(userCredential);

            return Response.responseData(HttpStatus.OK, "Successfully remove admin", admin);
        }

        return Response.responseData(HttpStatus.NOT_FOUND, "Admin is not found", null);
    }
}
