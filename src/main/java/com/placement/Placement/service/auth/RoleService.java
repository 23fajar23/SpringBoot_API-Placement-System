package com.placement.Placement.service.auth;

import com.placement.Placement.constant.ERole;
import com.placement.Placement.model.entity.auth.Role;

public interface RoleService {
    Role getOrSave(ERole role);
}

