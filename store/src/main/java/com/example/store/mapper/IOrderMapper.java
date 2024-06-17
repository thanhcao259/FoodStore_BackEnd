package com.example.store.mapper;

import com.example.store.dto.OrderResponseDTO;
import com.example.store.entity.Order;

import java.util.List;

public interface IOrderMapper {
    OrderResponseDTO toResponseDTO (Order order);
    List<OrderResponseDTO> toResponseDTOs (List<Order> orders);

}
