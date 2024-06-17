package com.example.store.service;

import com.example.store.dto.RoleUserDTO;
import com.example.store.exception.RoleNotFoundException;

public interface IRoleUserService {
    boolean setRole(RoleUserDTO roleUserDTO) throws RoleNotFoundException;
}
