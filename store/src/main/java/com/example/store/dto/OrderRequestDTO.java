package com.example.store.dto;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {
    private Long addressId;
    private String name;
    private String phone;

    @Override
    public String toString() {
        return "OrderRequestDTO{" +
                "addressId=" + addressId +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
