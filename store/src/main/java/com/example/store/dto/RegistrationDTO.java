package com.example.store.dto;

import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
public class RegistrationDTO {
    @Getter
    private String username;
    private String password;
    private String fullName;
    private String phone;

    @Email(message = "Invalid email format")
    private String email;

    public RegistrationDTO(String username, String password, String fullName, String phone, String email) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
    }

    public RegistrationDTO() {
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public @Email(message = "Invalid email format") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "Invalid email format") String email) {
        this.email = email;
    }
}
