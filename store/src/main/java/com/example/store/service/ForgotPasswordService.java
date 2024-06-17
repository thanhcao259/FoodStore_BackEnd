package com.example.store.service;

import com.example.store.dto.ResetPasswordDTO;

public interface ForgotPasswordService {

    void setOTPAndExpiration(String email);
    void resetPassword(ResetPasswordDTO dto);
}
