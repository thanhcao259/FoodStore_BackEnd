package com.example.store.controllers;

import com.example.store.dto.RegistrationDTO;
import com.example.store.exception.OTPNotFoundException;
import com.example.store.exception.UserNameExistedException;
import com.example.store.exception.UserNotFoundException;
import com.example.store.service.IRegisterService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {

    private final IRegisterService registrationService;

    public RegistrationController(IRegisterService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody RegistrationDTO registrationDTO) {
        try {
            registrationService.registration(registrationDTO);
            return new ResponseEntity<Void>(HttpStatus.CREATED);
        } catch (UserNameExistedException | DataIntegrityViolationException e) {
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed");
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> checkOtp(@RequestBody RegistrationDTO registrationDTO) {
        try {
            boolean isCheck = registrationService.verifyRegister(registrationDTO);
            return new ResponseEntity<>(isCheck, HttpStatus.OK);
        } catch (UserNotFoundException | OTPNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (UserNameExistedException e) {
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        } catch (Exception e) {
//            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
