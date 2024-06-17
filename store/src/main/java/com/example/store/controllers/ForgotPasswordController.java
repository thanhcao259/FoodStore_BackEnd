package com.example.store.controllers;

import com.example.store.dto.ResetPasswordDTO;
import com.example.store.entity.ForgotPassword;
import com.example.store.exception.ForgotPasswordException;
import com.example.store.exception.UserNotFoundException;
import com.example.store.repository.ForgotPasswordRepository;
import com.example.store.repository.IUserRepository;
import com.example.store.service.EmailService;
import com.example.store.service.ForgotPasswordService;
import com.example.store.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class ForgotPasswordController {

    private static final Logger log = LoggerFactory.getLogger(ForgotPasswordController.class);
    private final IUserRepository userRepository;
    private final IUserService userService;
    private final EmailService emailService;
    private final ForgotPasswordRepository fpRepository;
    private final ForgotPasswordService fpService;

    public ForgotPasswordController(IUserRepository userRepository, IUserService userService, EmailService emailService, ForgotPasswordRepository forgotPasswordRepository, ForgotPasswordService fpService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.emailService = emailService;
        this.fpRepository = forgotPasswordRepository;
        this.fpService = fpService;
    }

    @PostMapping("/verifyMail")
    public ResponseEntity<?> verifyMail(@RequestBody ResetPasswordDTO dto) {
        try {
            String email = dto.getEmail();
            fpService.setOTPAndExpiration(email);
            return new ResponseEntity<Void>(HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/set-new-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDTO dto) {
        try {
            log.info("reset dto: {}, {}, {}", dto.getEmail(), dto.getPassword(), dto.getOtp());
            fpService.resetPassword(dto);
            return new ResponseEntity<Void>(HttpStatus.OK);
        } catch (UserNotFoundException | ForgotPasswordException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
