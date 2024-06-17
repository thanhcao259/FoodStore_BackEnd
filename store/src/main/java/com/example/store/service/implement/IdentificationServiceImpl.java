//package com.example.store.service.implement;
//
//import com.example.store.dto.IdentificationDTO;
//import com.example.store.entity.Identification;
//import com.example.store.entity.User;
//import com.example.store.exception.UserNotFoundException;
//import com.example.store.mapper.IdentificationMapper;
//import com.example.store.repository.IUserRepository;
//import com.example.store.service.IdentificationService;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Optional;
//
//@Service
//public class IdentificationServiceImpl implements IdentificationService {
//    private final IUserRepository userRepository;
//    private final IdentificationMapper identificationMapper;
//
//    public IdentificationServiceImpl(IUserRepository userRepository, IdentificationMapper identificationMapper) {
//        this.userRepository = userRepository;
//        this.identificationMapper = identificationMapper;
//    }
//
//    @Transactional
//    @Override
//    public IdentificationDTO getIdentification(String username) {
//        Optional<User> existedUser = userRepository.findByUsername(username);
//        if (existedUser.isEmpty()) {
//            throw new UserNotFoundException(username);
//        }
//        User savedUser = existedUser.get();
//        Identification identification = savedUser.getIdentification();
//        IdentificationDTO identificationDTO = identificationMapper.toDTO(identification);
//        return identificationDTO;
//    }
//
//    @Transactional
//    @Override
//    public IdentificationDTO updateIdentification(String username, IdentificationDTO identificationDTO) {
//        Optional<User> existedUser = userRepository.findByUsername(username);
//        if (existedUser.isEmpty()) {
//            throw new UserNotFoundException("Not found " + username);
//        }
//        User savedUser = existedUser.get();
//        Identification identification = getIdentification(identificationDTO, savedUser);
//        savedUser.setIdentification(identification);
//        userRepository.save(savedUser);
//        return identificationDTO;
//    }
//
//    private Identification getIdentification(IdentificationDTO identificationDTO, User savedUser) {
//        Identification identification = savedUser.getIdentification();
//        identification.setPhone(identificationDTO.getPhone());
//        identification.setEmail(identificationDTO.getEmail());
//        identification.setFullName(identificationDTO.getFullName());
//        identification.setBirthDate(identificationDTO.getBirthDate());
//        if (!identificationDTO.getUrlAvatar().isEmpty()) {
//            identification.setUrlAvatar(identificationDTO.getUrlAvatar());
//        } else {
//            identification.setUrlAvatar(identification.getUrlAvatar());
//        }
//        return identification;
//    }
//}
