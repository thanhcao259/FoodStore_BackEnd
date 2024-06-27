//package com.example.store.service;
//
//import com.example.store.entity.*;
//import com.example.store.repository.IRoleRepository;
//import com.example.store.repository.IStatusOrderRepository;
//import com.example.store.repository.IUserRepository;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.time.ZonedDateTime;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//
//@Service
//public class DataService {
//    private final IRoleRepository roleRepository;
//    private final IStatusOrderRepository statusOrderRepository;
//    private final IUserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    public DataService(IRoleRepository roleRepository, IStatusOrderRepository statusOrderRepository
//            ,IUserRepository userRepository, PasswordEncoder passwordEncoder) {
//        this.roleRepository = roleRepository;
//        this.statusOrderRepository = statusOrderRepository;
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @PostConstruct
//    public void init() {
//        // Processing role of user
//        Optional<Role> existedRoleUser = roleRepository.findById(1L);
//        if (existedRoleUser.isEmpty()) {
//            Role roleUser = new Role();
//            roleUser.setName("USER");
//            roleUser.setDescription("This is the customer");
//            roleRepository.save(roleUser);
//        }
//        Optional<Role> existedRoleAdmin = roleRepository.findById(2L);
//        if (existedRoleAdmin.isEmpty()) {
//            Role roleAdmin = new Role();
//            roleAdmin.setName("ADMIN");
//            roleAdmin.setDescription("This is the admin");
//            roleRepository.save(roleAdmin);
//        }
//
//        // Processing status order
//        List<StatusOrder> list = statusOrderRepository.findAll();
//        if(list.isEmpty()){
//            StatusOrder statusOrder = new StatusOrder();
//            statusOrder.setName("Confirming");
//            statusOrderRepository.save(statusOrder);
//
//            StatusOrder statusOrder2 = new StatusOrder();
//            statusOrder2.setName("Packing");
//            statusOrderRepository.save(statusOrder2);
//
//            StatusOrder statusOrder3 = new StatusOrder();
//            statusOrder3.setName("Wait for Delivering");
//            statusOrderRepository.save(statusOrder3);
//
//            StatusOrder statusOrder4 = new StatusOrder();
//            statusOrder4.setName("Delivered");
//            statusOrderRepository.save(statusOrder4);
//
//            StatusOrder statusOrder5 = new StatusOrder();
//            statusOrder5.setName("Received");
//            statusOrderRepository.save(statusOrder5);
//
//            StatusOrder statusOrder6 = new StatusOrder();
//            statusOrder6.setName("Cancelled");
//            statusOrderRepository.save(statusOrder6);
//        }
//
//        // Initial a new admin
//        Optional<User> newUser = userRepository.findByUsername("admin");
//        if(newUser.isEmpty()){
//            Optional<Role> role = roleRepository.findByName("ADMIN");
//            Cart cart = new Cart();
//            cart.setCreatedDate(LocalDate.now());
////            Identification identification = new Identification();
////            identification.setFullName("Pham Thanh Cao");
////            identification.setEmail("thanhcao9501@gmail.com");
////            identification.setPhone("0838528503");
////            identification.setBirthDate(LocalDate.parse("2001-09-10"));
//
//
//            User user = new User();
//            user.setUsername("admin");
//            user.setPassword(passwordEncoder.encode("admin123"));
//            user.setFullName("Pham Thanh Cao");
//            user.setPhone("0838528503");
//            user.setEmail("thanhcao9501@gmail.com");
//            user.setBirthDate(LocalDate.parse("2001-09-10"));
////            user.setIdentification(identification);
//            user.setRoles(Collections.singleton(role.get()));
////            user.setRole(role.get());
//            user.setCart(cart);
//            user.setCreatedDate(ZonedDateTime.now());
//            cart.setUser(user);
//            userRepository.save(user);
//        }
//    }
//}
