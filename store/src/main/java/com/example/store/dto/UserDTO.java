package com.example.store.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String fullName;

    @Email(message = "Invalid email format")
    private String email;

    private String phone;
    private String createdDate;
    private String updatedDate;
    private String urlAvatar;
    private LocalDate birthDate;

    public UserDTO(String fullName, String email, String phone, LocalDate birthDate,String urlAvatar) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
        this.urlAvatar = urlAvatar;
    }
}
