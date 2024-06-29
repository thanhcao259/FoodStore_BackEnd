package com.example.store.service;

import com.example.store.dto.CartItemResponseDTO;
import com.example.store.dto.OrderPaymentDTO;
import com.example.store.dto.OrderRequestDTO;
import com.example.store.dto.OrderResponseDTO;
import com.example.store.entity.CartItem;

import java.util.List;

public interface IOrderService {
    OrderResponseDTO order(String username, OrderRequestDTO orderRequest);
    OrderResponseDTO orderPayment (String username, OrderPaymentDTO orderPaymentDTO);
    List<OrderResponseDTO> getAllOrder(int pageNo, int pageSize, String sortBy, String sortDirection);
    OrderResponseDTO setStatusOrder(Long orderId, Long statusId);
    List<CartItemResponseDTO> getDetailOrder(String username, Long orderId);
    List<CartItemResponseDTO> getDetailOrderAdmin(Long orderId);
    List<OrderResponseDTO> getOrderByUser(String username);

    boolean cancelOrder(Long idOrder, String username);
    boolean receivedProduct (Long idOrder, String username);

    OrderResponseDTO updateInfoOrder(Long orderId, String username);

}
