package com.example.store.service;

import com.example.store.dto.RegistrationDTO;

public interface IRegisterService {
    void registration(RegistrationDTO registrationDTO);
    boolean verifyRegister(RegistrationDTO registrationDTO);
}
