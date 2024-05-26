package com.placement.Placement.service.impl.auth;

import com.placement.Placement.constant.EStatus;
import com.placement.Placement.helper.convert.entity.Entity;
import com.placement.Placement.helper.response.Response;
import com.placement.Placement.model.entity.auth.SuperAdmin;
import com.placement.Placement.model.entity.auth.UserCredential;
import com.placement.Placement.model.request.SuperAdminRequest;
import com.placement.Placement.model.response.SuperAdminResponse;
import com.placement.Placement.repository.auth.SuperAdminRepository;
import com.placement.Placement.repository.auth.UserCredentialRepository;
import com.placement.Placement.service.auth.SuperAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SuperAdminServiceImpl implements SuperAdminService {

    private final SuperAdminRepository superAdminRepository;
    private final UserCredentialRepository userCredentialRepository;

    @Override
    public ResponseEntity<Object> getAll() {
        List<SuperAdminResponse> superAdminResponseList = superAdminRepository.findAll().stream()
                .map(Entity::convertToDto)
                .toList();

        return Response.responseData(HttpStatus.OK, "Successfully get all super admins", superAdminResponseList, null);
    }

    @Override
    public ResponseEntity<Object> getById(String id) {
        SuperAdmin superAdmin = superAdminRepository.findById(id).orElse(null);
        if (superAdmin != null) {
            SuperAdminResponse superAdminResponse = Entity.convertToDto(superAdmin);

            return Response.responseData(HttpStatus.OK, "Successfully get super admin", superAdminResponse, null);
        }

        return Response.responseData(HttpStatus.NOT_FOUND, "Super admin is not found", null, null);
    }


    @Override
    public ResponseEntity<Object> update(SuperAdminRequest superAdminRequest) {
        SuperAdmin superAdmin = superAdminRepository.findById(superAdminRequest.getId()).orElse(null);

        if (superAdmin != null) {

            UserCredential superAdminUserCredential = superAdmin.getUserCredential();

            if (superAdminUserCredential == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Super admin user credential is null");
            }

            UserCredential userCredential = userCredentialRepository.findById(
                    superAdminUserCredential.getId()).orElse(null);

            if (userCredential == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User credential with id "
                        + superAdminUserCredential.getId()
                        + " is not found");
            }

            superAdmin.setName(superAdminRequest.getName());
            superAdmin.setPhoneNumber(superAdmin.getPhoneNumber());

            userCredential.setStatus(superAdminRequest.getStatus());

            userCredentialRepository.saveAndFlush(userCredential);
            superAdminRepository.save(superAdmin);

            return Response.responseData(HttpStatus.OK, "Successfully update super admin", Entity.convertToDto(superAdmin), null);
        }

        return Response.responseData(HttpStatus.BAD_REQUEST, "Super admin is not found", null, null);
    }

    @Override
    public ResponseEntity<Object> remove(String id) {
        SuperAdmin superAdmin = superAdminRepository.findById(id).orElse(null);
        if (superAdmin != null) {
            UserCredential userCredential = userCredentialRepository.findById(
                            superAdmin.getUserCredential().getId())
                    .orElse(null);

            if (userCredential == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User credential with id "
                        + superAdmin.getUserCredential().getId() + " is not found");
            }

            userCredential.setStatus(EStatus.NOT_ACTIVE);
            userCredentialRepository.save(userCredential);

            return Response.responseData(HttpStatus.OK, "Successfully remove super admin", Entity.convertToDto(superAdmin), null);
        }

        return Response.responseData(HttpStatus.NOT_FOUND, "Super admin is not found", null, null);
    }

    @Override
    public SuperAdmin save(SuperAdmin superAdmin) {
        return superAdminRepository.save(superAdmin);
    }
}
