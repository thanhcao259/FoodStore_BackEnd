//package com.example.store.mapper.impl;
//
//import com.example.store.dto.IdentificationDTO;
//import com.example.store.entity.Identification;
//import com.example.store.mapper.IdentificationMapper;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//public class IdentificationMapperImpl implements IdentificationMapper {
//    @Override
//    public Identification toEntity(IdentificationDTO identificationDTO) {
//        Identification identification = new Identification();
//        identification.setFullName(identificationDTO.getFullName());
//        identification.setEmail(identificationDTO.getEmail());
//        identification.setPhone(identificationDTO.getPhone());
//        identification.setBirthDate(identificationDTO.getBirthDate());
//        identification.setUrlAvatar(identificationDTO.getUrlAvatar());
//        return identification;
//    }
//
//    @Override
//    public IdentificationDTO toDTO(Identification identification) {
//        IdentificationDTO identificationDTO = new IdentificationDTO();
//        identificationDTO.setFullName(identification.getFullName());
//        identificationDTO.setEmail(identification.getEmail());
//        identificationDTO.setPhone(identification.getPhone());
//        identificationDTO.setBirthDate(identification.getBirthDate());
//        identificationDTO.setUrlAvatar(identification.getUrlAvatar());
//
//        return identificationDTO;
//    }
//
//    @Override
//    public List<IdentificationDTO> toDTOs(List<Identification> identifications) {
//        List<IdentificationDTO> list = new ArrayList<>();
//        for (Identification identification : identifications) {
//            list.add(toDTO(identification));
//        }
//        return list;
//    }
//}
