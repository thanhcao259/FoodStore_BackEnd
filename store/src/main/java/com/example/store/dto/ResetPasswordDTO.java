package com.example.store.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordDTO {
    @Email(message = "Invalid email pattern")
    private String email;
    private Integer otp;
    private String password;
//    private Date expiration;
}
