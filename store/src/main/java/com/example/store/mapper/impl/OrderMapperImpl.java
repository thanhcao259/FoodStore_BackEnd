package com.example.store.mapper.impl;

import com.example.store.dto.CartItemResponseDTO;
import com.example.store.dto.OrderResponseDTO;
import com.example.store.entity.CartItem;
import com.example.store.entity.Order;
import com.example.store.mapper.AddressMapper;
import com.example.store.mapper.ICartItemMapper;
import com.example.store.mapper.IOrderMapper;
import com.example.store.mapper.VnPayMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMapperImpl implements IOrderMapper {

    private final AddressMapper addressMapper;
    private final ICartItemMapper cartItemMapper;
    private final VnPayMapper vnPayMapper;

    public OrderMapperImpl(AddressMapper addressMapper, ICartItemMapper cartItemMapper, VnPayMapper vnPayMapper) {
        this.addressMapper = addressMapper;
        this.cartItemMapper = cartItemMapper;
        this.vnPayMapper = vnPayMapper;
    }
    @Override
    public OrderResponseDTO toResponseDTO(Order order) {
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setId(order.getId());
        orderResponseDTO.setName(order.getName());
        orderResponseDTO.setPhone(order.getPhone());
        orderResponseDTO.setAddress(addressMapper.toDTO(order.getAddress()));
        orderResponseDTO.setTotalPrice(order.getTotalPrice());
        orderResponseDTO.setDeliveryTime(order.getDeliveryTime());
        orderResponseDTO.setStatusOrder(order.getStatusOrder().getName());
        orderResponseDTO.setIdentity(order.getIdentity());

        List<CartItemResponseDTO> cartItemResponseDTOs = cartItemMapper.toResponseDTOs(order.getCartItem());
        for(CartItem item : order.getCartItem()) {
            cartItemResponseDTOs.add(cartItemMapper.toResponseDTO(item));
        } orderResponseDTO.setCartItemResponseDTOs(cartItemResponseDTOs);

        if(order.getVnPayInfo()!= null){
            orderResponseDTO.setVnPayResponseDTO(vnPayMapper.toDTO(order.getVnPayInfo()));
        }
        return orderResponseDTO;
    }

    @Override
    public List<OrderResponseDTO> toResponseDTOs(List<Order> orders) {
        List<OrderResponseDTO> orderResponseDTOS = new ArrayList<>();
        for(Order item : orders){
            orderResponseDTOS.add(toResponseDTO(item));
        }
        return orderResponseDTOS;
    }
}
