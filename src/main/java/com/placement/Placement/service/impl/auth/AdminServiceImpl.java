package com.placement.Placement.service.impl.auth;

import com.placement.Placement.constant.EStatus;
import com.placement.Placement.helper.convert.dto.Dto;
import com.placement.Placement.helper.convert.entity.Entity;
import com.placement.Placement.helper.response.Response;
import com.placement.Placement.model.entity.auth.Admin;
import com.placement.Placement.model.entity.auth.UserCredential;
import com.placement.Placement.model.request.AdminRequest;
import com.placement.Placement.model.response.AdminResponse;
import com.placement.Placement.model.response.PagingResponse;
import com.placement.Placement.repository.auth.AdminRepository;
import com.placement.Placement.repository.auth.UserCredentialRepository;
import com.placement.Placement.service.auth.AdminService;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
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
                adminList, null);
    }

    @Override
    public ResponseEntity<Object> getById(String id) {
        Admin admin = adminRepository.findById(id).orElse(null);
        if (admin != null) {
            return Response.responseData(
                    HttpStatus.OK,
                    "Successfully get admin",
                    admin, null);
        }

        return Response.responseData(
                HttpStatus.NOT_FOUND,
                "Admin is not found",
                null, null);
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
            admin.setPhoneNumber(adminRequest.getMobilePhone());
            userCredential.setStatus(adminRequest.getStatus());

            userCredentialRepository.saveAndFlush(userCredential);
            adminRepository.save(admin);

            return Response.responseData(HttpStatus.OK,
                    "Successfully update admin",
                    admin, null);
        }

        return Response.responseData(
                HttpStatus.NOT_FOUND,
                "Admin is not found",
                null, null);
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

            return Response.responseData(HttpStatus.OK, "Successfully remove admin", admin, null);
        }

        return Response.responseData(HttpStatus.NOT_FOUND, "Admin is not found", null, null);
    }

    public ResponseEntity<Object> getAllByName(String name, Integer page, Integer size) {
        Specification<Admin> specification = (root , query , criteriaBuilder) ->{
            List<Predicate> predicates = new ArrayList<>();
            if (name != null){
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(page,size);
        Page<Admin> admins = adminRepository.findAll(specification,pageable);

        List<AdminResponse> adminResponses = new ArrayList<>();
        for (Admin admin : admins.getContent()){
            adminResponses.add(Entity.convertToDto(admin));
        }

        PageImpl<AdminResponse> results = new PageImpl<>(adminResponses,pageable,admins.getTotalElements());

        PagingResponse pagingResponse = PagingResponse.builder()
                .currentPage(page)
                .totalPage(admins.getTotalPages())
                .size(size)
                .build();

        return Response.responseData(HttpStatus.OK, "Success get all admin by name", results, pagingResponse);
    }

    @Override
    public AdminResponse findByUserCredentialId(String userCredential) {
        Admin admin = adminRepository.findByUserCredentialId(userCredential).orElse(null);
        if (admin != null) {
            return Entity.convertToDto(admin);
        }

        return null;
    }
}
