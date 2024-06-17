package com.example.store.service;

import com.example.store.dto.AddressDTO;

import java.util.List;

public interface IAddressService {
    List<AddressDTO> getAllAddress(String username);
    AddressDTO createAddress(String username, AddressDTO addressDTO);
    AddressDTO updateAddressById (String username, Long id, AddressDTO addressDTO);
    boolean deleteAddressById (String username,Long id);
}
