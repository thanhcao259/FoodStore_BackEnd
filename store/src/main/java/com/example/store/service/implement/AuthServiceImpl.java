package com.example.store.service.implement;

import com.example.store.entity.Role;
import com.example.store.entity.User;
import com.example.store.repository.IUserRepository;
import com.example.store.service.IAuthService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthServiceImpl implements IAuthService {
    private final IUserRepository userRepository;

    public AuthServiceImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public String getRoleUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty()) {
            throw new UsernameNotFoundException("Not found "+username);
        }
        List<Role> roles = new ArrayList<>(user.get().getRoles());
//        Role role = user.get().getRole();
        return roles.get(0).getName();
    }
}
