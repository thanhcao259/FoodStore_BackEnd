package com.example.store.service.implement;

import com.example.store.dto.MailBody;
import com.example.store.dto.ResetPasswordDTO;
import com.example.store.entity.User;
import com.example.store.exception.ForgotPasswordException;
import com.example.store.exception.UserNotFoundException;
import com.example.store.mapper.IUserMapper;
import com.example.store.repository.IUserRepository;
import com.example.store.service.EmailService;
import com.example.store.service.ForgotPasswordService;
import com.example.store.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
public class ForgotPasswordServiceImp implements ForgotPasswordService {

    private static final Logger log = LoggerFactory.getLogger(ForgotPasswordServiceImp.class);
    @Autowired
    private IUserService userService;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Transactional
    @Override
    public void setOTPAndExpiration(String email) {
        User user = userService.findUserByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("Email is not exists: " + email);
        }
        Integer otp = otpGenerator();
        Date exp = new Date(System.currentTimeMillis() + 5 * 60 * 1000);
        user.setOtp(otp);
        user.setExpiration(exp);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                user.setOtp(null);
                user.setExpiration(null);
                userRepository.save(user);
                log.info("Both OTP and out of date Expiration are set NULL");
            }
        }, exp);

        userRepository.save(user);
        sendMail(email, otp);
    }

    @Transactional
    @Override
    public void resetPassword(ResetPasswordDTO dto) {
        String email = dto.getEmail();
        Integer otp = dto.getOtp();
        String password = dto.getPassword();
        User user = userService.findUserByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("Email is not exists: " + email);
        }
        Date savedExp = user.getExpiration();
        Integer savedOtp = user.getOtp();
        boolean checkExp = checkExpiration(savedExp);
        if (Objects.equals(otp, savedOtp) && checkExp) {
            log.info("Correct OTP and Expiration");
            user.setOtp(null);
            user.setExpiration(null);
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        } else {
            throw new ForgotPasswordException("Your OTP is not existed or out of date");
        }
    }

    private void sendMail(String email, Integer otp) {
        MailBody mailBody = MailBody.builder()
                .to(email)
                .text("This is the OTP for your Forgot password: " + otp)
                .subject("OTP for Forgot Password").build();

        emailService.sendSimpleMessage(mailBody);
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

}
