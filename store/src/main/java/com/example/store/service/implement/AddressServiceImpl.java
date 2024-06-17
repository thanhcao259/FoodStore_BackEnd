package com.example.store.service.implement;

import com.example.store.dto.AddressDTO;
import com.example.store.entity.Address;
import com.example.store.entity.User;
import com.example.store.exception.AddressNotFoundException;
import com.example.store.exception.UserNotFoundException;
import com.example.store.mapper.AddressMapper;
import com.example.store.repository.IAddressRepository;
import com.example.store.repository.IUserRepository;
import com.example.store.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements IAddressService {
    private final IUserRepository userRepository;
    private final AddressMapper addressMapper;
    private final IAddressRepository addressRepository;

    public AddressServiceImpl(IUserRepository userRepository, AddressMapper a, IAddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.addressMapper = a;
        this.addressRepository = addressRepository;
    }


    @Transactional
    @Override
    public List<AddressDTO> getAllAddress(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty()){
            throw new UserNotFoundException("Not Found "+username);
        } List<Address> list = user.get().getAddresses();
        return addressMapper.toDTOs(list);
    }

    @Transactional
    @Override
    public AddressDTO createAddress(String username, AddressDTO addressDTO) {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty()){
            throw new UserNotFoundException("Not Found "+username);
        } User userEntity = user.get();
        Address address = new Address();
        address.setAddress(addressDTO.getAddress());
        address.setUser(userEntity);
        userEntity.getAddresses().add(address);// add new address to List address of each user
        userRepository.save(userEntity);
        return addressMapper.toDTO(address);
    }

    @Transactional
    @Override
    public AddressDTO updateAddressById(String username, Long id, AddressDTO addressDTO) {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty()){
            throw new UserNotFoundException("Not Found "+username);
        }
        Long userId = user.get().getId();
        Optional<Address> existedAddress = addressRepository.findByIdAndUserId(id, userId);
        if(existedAddress.isEmpty()){
            throw new AddressNotFoundException("Not Found "+existedAddress);
        } Address address = existedAddress.get();
        address.setAddress(addressDTO.getAddress());
        addressRepository.save(address);

        return addressMapper.toDTO(address);
    }

    @Transactional
    @Override
    public boolean deleteAddressById(String username, Long id) {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty()){
            throw new UserNotFoundException("Not Found "+username);
        }
        Long userId = user.get().getId();
        Optional<Address> existedAddress = addressRepository.findByIdAndUserId(id, userId);
        if(existedAddress.isEmpty()){
            throw new AddressNotFoundException("Not Found "+existedAddress);
        }
        addressRepository.deleteById(id);
        return false;
    }


}
