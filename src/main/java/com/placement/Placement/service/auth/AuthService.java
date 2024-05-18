package com.placement.Placement.service.auth;

import com.placement.Placement.model.request.AuthRequest;
import com.placement.Placement.model.response.LoginResponse;
import com.placement.Placement.model.response.RegisterResponse;

public interface AuthService {
    RegisterResponse registerCustomer(AuthRequest authRequest);
    RegisterResponse registerAdmin(AuthRequest authRequest);
    RegisterResponse registerSuperAdmin(AuthRequest authRequest);
    LoginResponse login(AuthRequest authRequest);
}