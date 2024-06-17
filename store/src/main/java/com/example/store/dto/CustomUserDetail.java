package com.example.store.dto;

import com.example.store.entity.Role;
import com.example.store.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomUserDetail implements UserDetails {
    private final String username;
    private final String password;
    private final Set<GrantedAuthority> authorities;

    public CustomUserDetail(String username, String password, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.authorities = roles.stream()
        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toSet());
    }

//    public CustomUserDetail(User user) {
//        this.username = user.getUsername();
//        this.password = user.getPassword();
//        this.authorities = Arrays.stream(user.getRoles().spliterator(","))
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
