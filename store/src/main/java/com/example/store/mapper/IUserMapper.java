package com.example.store.mapper;

import com.example.store.dto.UserDTO;
import com.example.store.entity.User;

import java.util.List;

public interface IUserMapper {
    User toEntity (UserDTO userDTO);
    UserDTO toDTO (User user);
    List<UserDTO> toDTOs (List<User> users);


}
