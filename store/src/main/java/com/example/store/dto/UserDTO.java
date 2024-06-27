package com.example.store.dto;

import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Getter @Setter
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
    private String identity;

    public UserDTO(String fullName, String email, String phone, LocalDate birthDate,String urlAvatar) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
        this.urlAvatar = urlAvatar;
    }

    public UserDTO(String username, String fullName, String email, String phone, String urlAvatar, LocalDate birthDate) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.urlAvatar = urlAvatar;
        this.birthDate = birthDate;
    }
}
