package com.placement.Placement.service.impl;


import com.placement.Placement.constant.ERole;
import com.placement.Placement.model.entity.auth.*;
import com.placement.Placement.model.request.AuthRequest;
import com.placement.Placement.model.response.LoginResponse;
import com.placement.Placement.model.response.RegisterResponse;
import com.placement.Placement.repository.auth.UserCredentialRepository;
import com.placement.Placement.security.JwtUtil;
import com.placement.Placement.service.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserCredentialRepository userCredentialRepository;
    private final CustomerService customerService;
    private final AdminService adminService;
    private final SuperAdminService superAdminService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    private final RoleService roleService;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public RegisterResponse registerCustomer(AuthRequest request) {
        try {
            //TODO 1 : set Role
            Role role = roleService.getOrSave(ERole.ROLE_CUSTOMER);

            //TODO 2 : set credential
            UserCredential userCredential = UserCredential.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(role)
                    .build();
            userCredentialRepository.saveAndFlush(userCredential);

            //TODO 3 : set Customer
            Customer customer = Customer.builder()
                    .name(request.getName())
                    .address(request.getAddress())
                    .mobilePhone(request.getMobilePhone())
                    .userCredential(userCredential)
                    .build();
            customerService.save(customer);

            return RegisterResponse.builder()
                    .email(userCredential.getEmail())
                    .role(userCredential.getRole().getName().toString())
                    .build();

        }catch (DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user already exist");
        }
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public RegisterResponse registerAdmin(AuthRequest request) {
        try {
            //TODO 1 : set Role
            Role role = roleService.getOrSave(ERole.ROLE_ADMIN);

            //TODO 2 : set credential
            UserCredential userCredential = UserCredential.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(role)
                    .build();
            userCredentialRepository.saveAndFlush(userCredential);

            //TODO 3 : set Customer
            Admin admin = Admin.builder()
                    .name(request.getName())
                    .phoneNumber(request.getMobilePhone())
                    .build();

            adminService.save(admin);

            return RegisterResponse.builder()
                    .email(userCredential.getEmail())
                    .role(userCredential.getRole().getName().toString())
                    .build();

        }catch (DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user already exist");
        }
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public RegisterResponse registerSuperAdmin(AuthRequest request) {
        try {
            //TODO 1 : set Role
            Role role = roleService.getOrSave(ERole.ROLE_SUPER_ADMIN);

            //TODO 2 : set credential
            UserCredential userCredential = UserCredential.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(role)
                    .build();
            userCredentialRepository.saveAndFlush(userCredential);

            //TODO 3 : set Customer
            SuperAdmin superAdmin = SuperAdmin.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .address(request.getAddress())
                    .phoneNumber(request.getMobilePhone())
                    .build();

            superAdminService.save(superAdmin);

            return RegisterResponse.builder()
                    .email(userCredential.getEmail())
                    .role(userCredential.getRole().getName().toString())
                    .build();

        }catch (DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user already exist");
        }
    }

    @Override
    public LoginResponse login(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new
                UsernamePasswordAuthenticationToken(
                authRequest.getEmail().toLowerCase(),
                authRequest.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        AppUser appUser = (AppUser) authentication.getPrincipal();
        String token = jwtUtil.generateToken(appUser);

        return LoginResponse.builder()
                .email(authRequest.getEmail())
                .token(token)
                .role(appUser.getRole().name())
                .build();
    }
}
