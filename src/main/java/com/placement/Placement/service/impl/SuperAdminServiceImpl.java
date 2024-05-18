package com.placement.Placement.service.impl;

import com.placement.Placement.model.entity.auth.SuperAdmin;
import com.placement.Placement.repository.auth.SuperAdminRepository;
import com.placement.Placement.service.SuperAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SuperAdminServiceImpl implements SuperAdminService {

    private final SuperAdminRepository superAdminRepository;

    @Override
    public SuperAdmin save(SuperAdmin superAdmin) {
        return superAdminRepository.save(superAdmin);
    }
}
