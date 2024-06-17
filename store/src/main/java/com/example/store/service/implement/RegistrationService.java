package com.example.store.service.implement;

import com.example.store.dto.RegistrationDTO;
import com.example.store.entity.Provider;
import com.example.store.entity.Cart;
//import com.example.store.entity.Identification;
import com.example.store.entity.Role;
import com.example.store.entity.User;
import com.example.store.exception.RoleNotFoundException;
import com.example.store.exception.UserNameExistedException;
import com.example.store.repository.IRoleRepository;
import com.example.store.repository.IUserRepository;
import com.example.store.service.IRegisterService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Optional;

@Service
public class RegistrationService implements IRegisterService {
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(IUserRepository userRepository, IRoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public void registration(RegistrationDTO registrationDTO) {
        Optional<User> optionalUser = userRepository.findByUsername(registrationDTO.getUsername());
        if (optionalUser.isPresent()) {
            throw new UserNameExistedException("Username already existed");
        }
        Optional<Role> optionalRole = roleRepository.findByName("USER");
        if(optionalRole.isEmpty()){
            throw new RoleNotFoundException("Role not found");
        }
        Cart cart = new Cart();
        cart.setCreatedDate(LocalDate.now());
//        Identification identification = new Identification();
//        identification.setFullName(registrationDTO.getFullName());
//        identification.setEmail(registrationDTO.getEmail());
//        identification.setPhone(registrationDTO.getPhone());

        User user = new User();
//        user.setIdentification(identification);
//        user.setRole(optionalRole.get());
        user.setUsername(registrationDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setFullName(registrationDTO.getFullName());
        user.setPhone(registrationDTO.getPhone());
        user.setEmail(registrationDTO.getEmail());
        user.setRoles(Collections.singleton(optionalRole.get()));
        user.setCart(cart);
        cart.setUser(user);
        user.setCreatedDate(ZonedDateTime.now());
        user.setProvider(Provider.LOCAL);

        userRepository.save(user);
    }
}
