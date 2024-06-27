package com.example.store.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", length = 100, nullable = false, unique = true)
    private String username;

    @Column(name = "full_name", length = 100, nullable = false)
    private String fullName;

    @Email(message = "Invalid email format")
    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "createdDate")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private ZonedDateTime createdDate;

    @Column(name = "updatedDate")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private ZonedDateTime updatedDate;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    @ColumnDefault("false")
    private Boolean status;

    @Column(name = "token_reset_pwd")
    private String tokenResetPwd;

    @Column(name = "avatar")
    private String urlAvatar;

    @Column(name = "birthday")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDate birthDate;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "role_user", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    private String identity;

//
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "identification_id", referencedColumnName = "id")
//    private Identification identification;

//    @ManyToOne
//    @JoinColumn(name = "role", referencedColumnName = "id")
//    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JoinColumn(name = "cart", referencedColumnName = "id")
    private Cart cart;

//    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
//    @JoinColumn(name = "forgot_password", referencedColumnName = "id")
//    private ForgotPassword forgotPassword;

    @OneToMany(mappedBy = "userCreated", cascade = CascadeType.ALL)
    private List<Product> createdProducts;
    @OneToMany(mappedBy = "userUpdated", cascade = CascadeType.ALL)
    private List<Product> updatedProducts;

    @OneToMany(mappedBy = "userCreated", cascade = CascadeType.ALL)
    private List<Category> createdCategory;
    @OneToMany(mappedBy = "userUpdated", cascade = CascadeType.ALL)
    private List<Category> updatedCategory;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Address> addresses;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Favorite> favorites;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Reviews> reviews;

    @Column(name = "otp")
    private Integer otp;

    @Column(name = "expiration")
    private Date expiration;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private Provider provider;
}
