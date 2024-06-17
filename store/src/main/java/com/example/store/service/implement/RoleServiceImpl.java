package com.example.store.service.implement;

import com.example.store.dto.RoleUserDTO;
import com.example.store.entity.Role;
import com.example.store.entity.User;
import com.example.store.exception.RoleNotFoundException;
import com.example.store.exception.UserNotFoundException;
import com.example.store.repository.IRoleRepository;
import com.example.store.repository.IUserRepository;
import com.example.store.service.IRoleUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
public class RoleServiceImpl implements IRoleUserService {
    private final IRoleRepository roleRepository;
    private final IUserRepository userRepository;

    public RoleServiceImpl(IRoleRepository roleRepository, IUserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public boolean setRole(RoleUserDTO roleUserDTO) throws RoleNotFoundException {
        Optional<Role> existedRole = roleRepository.findById(roleUserDTO.getRoleId());
        if(existedRole.isEmpty()){
            throw new RoleNotFoundException("Role not found");
        } Optional<User> existedUser = userRepository.findById(roleUserDTO.getUserId());
        if(existedUser.isEmpty()){
            throw new UserNotFoundException("User not found");
        } Role role = existedRole.get();
        User user = existedUser.get();
//        Set<Role> roleSet = user.getRoles();
//        roleSet.clear();
//        roleSet.add(role);
        return true;
    }
}
