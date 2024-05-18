package com.placement.Placement.service.impl.auth;

import com.placement.Placement.model.entity.auth.AppUser;
import com.placement.Placement.model.entity.auth.UserCredential;
import com.placement.Placement.repository.auth.UserCredentialRepository;
import com.placement.Placement.service.auth.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserCredentialRepository userCredentialRepository;
    @Override
    public AppUser loadUserByUserId(String id) {
        UserCredential userCredential = userCredentialRepository.findById(id).orElseThrow();
        return AppUser.builder()
                .id(userCredential.getId())
                .username(userCredential.getEmail())
                .password(userCredential.getPassword())
                .role(userCredential.getRole().getName())
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserCredential userCredential = userCredentialRepository.findByEmail(username).orElseThrow();
        return AppUser.builder()
                .id(userCredential.getId())
                .username(userCredential.getEmail())
                .password(userCredential.getPassword())
                .role(userCredential.getRole().getName())
                .build();
    }
}
