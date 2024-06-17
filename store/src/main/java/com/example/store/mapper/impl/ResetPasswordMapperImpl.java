package com.example.store.mapper.impl;

import com.example.store.dto.ResetPasswordDTO;
import com.example.store.entity.User;
import com.example.store.mapper.IResetPasswordMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ResetPasswordMapperImpl implements IResetPasswordMapper {

    @Override
    public ResetPasswordDTO toDto(User user) {
        ResetPasswordDTO dto = new ResetPasswordDTO();
        dto.setEmail(user.getEmail());

        return dto;
    }

    @Override
    public User toEntity(ResetPasswordDTO dto) {

        return null;
    }

    @Override
    public List<User> toEntities(List<ResetPasswordDTO> dtos) {
        return List.of();
    }
}
