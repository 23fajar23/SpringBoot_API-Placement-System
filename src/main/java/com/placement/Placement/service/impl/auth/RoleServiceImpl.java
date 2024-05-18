package com.placement.Placement.service.impl.auth;

import com.placement.Placement.constant.ERole;
import com.placement.Placement.model.entity.auth.Role;
import com.placement.Placement.repository.auth.RoleRepository;
import com.placement.Placement.service.auth.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;
    @Override
    public Role getOrSave(ERole role) {
        Optional<Role> optionalRole = repository.findByName(role);

        if (optionalRole.isPresent()){
            return optionalRole.get();
        }

        Role currentRole = Role.builder()
                .name(role)
                .build();

        return repository.saveAndFlush(currentRole);
    }
}
