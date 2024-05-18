package com.placement.Placement.service.impl.auth;


import com.placement.Placement.constant.ERole;
import com.placement.Placement.constant.Status;
import com.placement.Placement.helper.convert.dto.Dto;
import com.placement.Placement.model.entity.Batch;
import com.placement.Placement.model.entity.Education;
import com.placement.Placement.model.entity.auth.*;
import com.placement.Placement.model.request.AuthRequest;
import com.placement.Placement.model.response.BatchResponse;
import com.placement.Placement.model.response.EducationResponse;
import com.placement.Placement.model.response.LoginResponse;
import com.placement.Placement.model.response.RegisterResponse;
import com.placement.Placement.repository.auth.UserCredentialRepository;
import com.placement.Placement.security.JwtUtil;
import com.placement.Placement.service.auth.*;
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
    private final BatchService batchService;
    private final EducationService educationService;
    private final RoleService roleService;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public RegisterResponse registerCustomer(AuthRequest request) {
        try {
            BatchResponse batchResponse = batchService.getById(request.getBatchId());
            EducationResponse educationResponse = educationService.getById(request.getEducationId());

            if (batchResponse == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Batch is not found");
            }

            if (educationResponse == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Education is not found");
            }

            if (batchResponse.getStatus() == Status.NOT_ACTIVE) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Batch is not active");
            }

            Batch batch = Dto.convertToEntity(batchResponse);
            Education education = Dto.convertToEntity(educationResponse);

            Role role = roleService.getOrSave(ERole.ROLE_CUSTOMER);

            UserCredential userCredential = UserCredential.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(role)
                    .status(Status.ACTIVE)
                    .build();
            userCredentialRepository.saveAndFlush(userCredential);

            Customer customer = Customer.builder()
                    .name(request.getName())
                    .address(request.getAddress())
                    .mobilePhone(request.getMobilePhone())
                    .userCredential(userCredential)
                    .batch(batch)
                    .education(education)
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
            Role role = roleService.getOrSave(ERole.ROLE_ADMIN);

            UserCredential userCredential = UserCredential.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(role)
                    .status(Status.ACTIVE)
                    .build();
            userCredentialRepository.saveAndFlush(userCredential);

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
                    .status(Status.ACTIVE)
                    .build();
            userCredentialRepository.saveAndFlush(userCredential);

            //TODO 3 : set Customer
            SuperAdmin superAdmin = SuperAdmin.builder()
                    .name(request.getName())
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

        var userCredential = userCredentialRepository.findByEmail(authRequest.getEmail()).orElse(null);

        if (userCredential != null) {
            if (userCredential.getStatus() == Status.ACTIVE) {
                return LoginResponse.builder()
                        .email(authRequest.getEmail())
                        .token(token)
                        .role(appUser.getRole().name())
                        .build();
            }
        }
        return null;
    }
}
