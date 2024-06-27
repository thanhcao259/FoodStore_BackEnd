package com.example.store.mapper.impl;

import com.example.store.dto.UserDTO;
import com.example.store.entity.User;
import com.example.store.mapper.IUserMapper;
import com.example.store.util.FormattedDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapperImpl implements IUserMapper {
    @Autowired
    private FormattedDateUtils formattedDateUtils;
    @Override
    public User toEntity(UserDTO userDTO) {
        return null;
    }

    @Override
    public UserDTO toDTO(User user) {
        ZonedDateTime createdDate = user.getCreatedDate();
        ZonedDateTime updatedDate = user.getUpdatedDate();
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setIdentity(user.getIdentity());
        userDTO.setUsername(user.getUsername());
        userDTO.setFullName(user.getFullName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        userDTO.setCreatedDate(formattedDateUtils.convertToString(createdDate));
        userDTO.setUpdatedDate(formattedDateUtils.convertToString(updatedDate));
        userDTO.setUrlAvatar(user.getUrlAvatar());
        userDTO.setBirthDate(user.getBirthDate());
        return userDTO;
    }

    @Override
    public List<UserDTO> toDTOs(List<User> userList) {
        List<UserDTO> userDTOs = new ArrayList<>();
        for(User user: userList){
            userDTOs.add(toDTO(user));
        }
        return userDTOs;
    }
}
