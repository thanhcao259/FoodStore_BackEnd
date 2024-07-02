package com.example.store.controllers.delivery;

import com.example.store.entity.delivery.GHNProvince;
import com.example.store.service.delivery.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @GetMapping("/provinces")
    public List<GHNProvince> getProvinces() {
        return deliveryService.getProvinces();
    }
//    public ResponseEntity<?> createDelivery(){
//        try{
//            List<GHNProvince> listData = deliveryService.getProvinces();
//            return new ResponseEntity<>(listData, HttpStatus.OK);
//        } catch (AuthenticationException e){
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
//        } catch (Exception e){
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
}
