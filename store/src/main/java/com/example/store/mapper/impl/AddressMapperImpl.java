package com.example.store.mapper.impl;

import com.example.store.dto.AddressDTO;
import com.example.store.entity.Address;
import com.example.store.mapper.AddressMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AddressMapperImpl implements AddressMapper {
    @Override
    public AddressDTO toDTO(Address address) {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setId(address.getId());
        addressDTO.setAddress(address.getAddress());
        return addressDTO;
    }

    @Override
    public List<AddressDTO> toDTOs(List<Address> addresses) {
        List<AddressDTO> addressDTOS = new ArrayList<>();
        for (Address address : addresses) {
            addressDTOS.add(toDTO(address));
        }
        return addressDTOS;
    }

    @Override
    public Address toEntity(AddressDTO addressDTO) {
        Address address = new Address();
        address.setAddress(addressDTO.getAddress());
        return address;
    }

    @Override
    public List<Address> toEntities(List<AddressDTO> addressDTOS) {
        List<Address> addresses = new ArrayList<>();
        for (AddressDTO addressDTO : addressDTOS) {
            addresses.add(toEntity(addressDTO));
        }
        return addresses;
    }
}
