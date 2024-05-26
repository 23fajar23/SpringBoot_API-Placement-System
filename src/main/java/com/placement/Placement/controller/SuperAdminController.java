package com.placement.Placement.controller;

import com.placement.Placement.constant.AppPath;
import com.placement.Placement.model.request.SuperAdminRequest;
import com.placement.Placement.service.auth.SuperAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppPath.API + AppPath.SUPER_ADMIN)
public class SuperAdminController {
    private final SuperAdminService superAdminService;

    @GetMapping
    public ResponseEntity<?> getAllSuperAdmins() {
        return superAdminService.getAll();
    }

    @GetMapping(AppPath.BY_ID)
    public ResponseEntity<?> getSuperAdminById(@PathVariable String id) {
        return superAdminService.getById(id);
    }

    @PutMapping
    public ResponseEntity<?> updateSuperAdmin(@RequestBody SuperAdminRequest superAdminRequest) {
        return superAdminService.update(superAdminRequest);
    }

    @DeleteMapping(AppPath.BY_ID)
    public ResponseEntity<?> removeSuperAdmin(@PathVariable String id) {
        return superAdminService.remove(id);
    }
}
