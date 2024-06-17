package com.example.store.service;

import com.example.store.dto.RegistrationDTO;
import com.example.store.dto.UserDTO;
import com.example.store.entity.Provider;
import com.example.store.entity.User;
import com.example.store.security.oauth2.CustomOAuth2User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    boolean changePassword(String username, String oldPassword, String newPassword);
    List<UserDTO> getAllUser ();
    boolean deleteUser (Long id);
    boolean resetPassword(Long id, String newPassword);
    UserDTO updateUser(Long id, RegistrationDTO registrationDTO);
    UserDTO updateUser(String username, UserDTO userDTO);
    UserDTO updateUser(Long userId, UserDTO userDTO);
    Optional<User> findUserByUsername(String username);
    UserDTO findUserByName(String username);
    String sendForgotPassword (String email);

    boolean updatePassword (String token,String username,String password);

    User findUserByEmail(String email);
    User loginWithFB(CustomOAuth2User user, Provider provider);

}
