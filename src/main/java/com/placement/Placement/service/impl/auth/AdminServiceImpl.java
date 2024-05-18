package com.placement.Placement.service.impl.auth;

import com.placement.Placement.model.entity.auth.Admin;
import com.placement.Placement.repository.auth.AdminRepository;
import com.placement.Placement.service.auth.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    @Override
    public Admin save(Admin admin) {
        return adminRepository.save(admin);
    }
}
