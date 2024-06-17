package com.example.store.mapper;

import com.example.store.dto.AddressDTO;
import com.example.store.entity.Address;
import org.springframework.context.annotation.Bean;

import java.util.List;


public interface AddressMapper {
    AddressDTO toDTO (Address address);

    List<AddressDTO> toDTOs (List<Address> addresses);
    Address toEntity(AddressDTO addressDTO);
    List<Address> toEntities (List<AddressDTO> addressDTOS);

}
