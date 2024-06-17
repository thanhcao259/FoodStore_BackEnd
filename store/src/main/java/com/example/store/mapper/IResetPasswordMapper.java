package com.example.store.mapper;

import com.example.store.dto.ResetPasswordDTO;
import com.example.store.entity.User;

import java.util.List;

public interface IResetPasswordMapper {
    ResetPasswordDTO toDto(User user);
    User toEntity(ResetPasswordDTO dto);
    List<User> toEntities(List<ResetPasswordDTO> dtos);
}
