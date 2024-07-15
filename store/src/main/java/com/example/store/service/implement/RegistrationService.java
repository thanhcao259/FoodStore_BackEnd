package com.example.store.service.implement;

import com.example.store.dto.MailBody;
import com.example.store.dto.RegistrationDTO;
import com.example.store.entity.Provider;
import com.example.store.entity.Cart;
//import com.example.store.entity.Identification;
import com.example.store.entity.Role;
import com.example.store.entity.User;
import com.example.store.exception.ForgotPasswordException;
import com.example.store.exception.RoleNotFoundException;
import com.example.store.exception.UserNameExistedException;
import com.example.store.exception.UserNotFoundException;
import com.example.store.repository.IRoleRepository;
import com.example.store.repository.IUserRepository;
import com.example.store.service.EmailService;
import com.example.store.service.IRegisterService;
import com.example.store.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class RegistrationService implements IRegisterService {
    private static Logger log = LoggerFactory.getLogger(RegistrationService.class);
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;
    @Autowired
    private IUserService userService;

    public RegistrationService(IUserRepository userRepository, IRoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public void registration(RegistrationDTO registrationDTO) {
        Optional<User> optionalUser = userRepository.findByEmailAndUsername(registrationDTO.getEmail(), registrationDTO.getUsername());
        if (optionalUser.isPresent()) {
            throw new UserNameExistedException("Email or username already existed");
        }
        Optional<Role> optionalRole = roleRepository.findByName("CUSTOMER");
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

        /* Set identity for customer */
        List<Role> roles = new ArrayList<>(user.getRoles());
        String identity = generateIdentity(roles.get(0).getName(), registrationDTO.getUsername());
        user.setIdentity(identity);
        /*  End */

        user.setStatus(false);
        user.setCart(cart);
        cart.setUser(user);
        user.setCreatedDate(ZonedDateTime.now());

        /* Set exp and otp */
        int newOTP = otpGenerator();
        user.setOtp(newOTP);
        Date exp = new Date(System.currentTimeMillis() + 10 * 60 * 1000);
        user.setExpiration(exp);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                userRepository.delete(user);
                log.info("Both OTP and Expiration are set NULL");
            }
        }, exp);
        //setOTPAndExpiration(registrationDTO.getEmail());
        /* End */
        userRepository.save(user);
        sendMail(registrationDTO.getEmail(), newOTP);
    }

    @Transactional
    @Override
    public boolean verifyRegister(RegistrationDTO registrationDTO){
        Optional<User> optionalUser = userRepository.findByUsername(registrationDTO.getUsername());
        if(optionalUser.isEmpty()){
            throw new UserNotFoundException("Not found username");
        } User user = optionalUser.get();
        int otp = registrationDTO.getOtp();
        int savedOTP = user.getOtp();

        if(Objects.equals(otp, savedOTP)){
            log.info("Verify otp register success!");
            user.setOtp(null); user.setExpiration(null); user.setStatus(true);
            userRepository.save(user);
            return true;
        }
//        if (!checkExpiration(user.getExpiration())) {
//            log.info("Deleted because this account didn't activate within 1 hour");
//            userRepository.delete(user);
//            return false;
//        } if (!Objects.equals(otp, savedOTP)) {
//            log.info("Your OTP incorrect!");
//            return false;
//        }
        else {
            throw new ForgotPasswordException("Your OTP is incorrect or out of date");
        }
    }
    private String generateIdentity(String role, String username) {
        String strRand = String.valueOf(Math.round(Math.random() * 10000));
        String strRole = "";
        username = username.replaceAll(" ", "").toUpperCase();
        if(role.equals("CUSTOMER")){
            strRole ="CS";
        } else if (role.equals("ADMIN")) {
            strRole = "AD";
        } return strRole+"_"+username+"_"+strRand;
    }

    private boolean checkExpiration(Date expiration) {
        Date currTime = Date.from(Instant.now());
        if (currTime.after(expiration)) {
            log.info("Your OTP is out of date");
            return false;
        }
        return true;
    }
    private Integer otpGenerator() {
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }
    private void sendMail(String email, Integer otp) {
        MailBody mailBody = MailBody.builder()
                .to(email)
                .text("This is the OTP for your registration: " + otp)
                .subject("OTP for Registration").build();

        emailService.sendSimpleMessage(mailBody);
    }

}
