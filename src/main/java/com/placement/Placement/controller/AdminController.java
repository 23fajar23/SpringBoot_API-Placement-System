package com.placement.Placement.controller;

import com.placement.Placement.constant.AppPath;
import com.placement.Placement.model.request.AdminRequest;
import com.placement.Placement.service.auth.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppPath.API + AppPath.ADMIN)
public class AdminController {

    private final AdminService adminService;
    @GetMapping
    public ResponseEntity<?> getAllAdmins() {
        return adminService.getAll();
    }

    @GetMapping(AppPath.BY_ID)
    public ResponseEntity<?> getAdminById(@PathVariable String id) {
        return adminService.getById(id);
    }

    @PutMapping
    public ResponseEntity<?> updateAdmin(@RequestBody AdminRequest adminRequest) {
        return adminService.update(adminRequest);
    }

    @DeleteMapping(AppPath.BY_ID)
    public ResponseEntity<?> removeAdmin(@PathVariable String id) {
       return adminService.remove(id);
    }
}
