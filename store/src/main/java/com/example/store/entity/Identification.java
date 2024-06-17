//package com.example.store.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import jakarta.validation.constraints.Email;
//import org.hibernate.annotations.ColumnDefault;
//
//import java.time.LocalDate;
//import java.time.ZonedDateTime;
//
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Table(name = "identification")
//public class Identification {
//    @Id
//    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "avatar")
//    private String urlAvatar;
//
//    @Column(name = "full_name", length = 100, nullable = false)
//    private String fullName;
//
//    @Column(name = "birthday")
//    private LocalDate birthDate;
//
//    @Email(message = "Invalid email format")
//    @Column(name = "email", unique = true)
//    private String email;
//
//    @Column(name = "phone")
//    private String phone;
//
//    @OneToOne(mappedBy = "identification", cascade = CascadeType.ALL)
//    private User user;
//
//}
