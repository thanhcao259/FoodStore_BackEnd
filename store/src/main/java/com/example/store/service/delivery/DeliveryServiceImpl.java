package com.example.store.service.delivery;

import com.example.store.dto.delivery.GHNProvinceResponse;
import com.example.store.entity.delivery.GHNProvince;
import com.example.store.util.DeliveryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeliveryServiceImpl implements DeliveryService {
    private Logger log = LoggerFactory.getLogger(DeliveryServiceImpl.class);
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DeliveryUtils deliveryUtils;

    @Override
    public List<GHNProvince> getProvinces() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", DeliveryUtils.TOKEN);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<GHNProvinceResponse> responseDto = restTemplate.exchange(DeliveryUtils.API_PROVINCE, HttpMethod.GET, entity, GHNProvinceResponse.class);

        if (responseDto.getStatusCode().is2xxSuccessful() && responseDto.getBody() != null) {
            List<GHNProvince> listData = responseDto.getBody().getData();
            log.info("have provinces: {}",listData.size());
            return listData;
//            log.info("getBody(): {}", responseDto.getBody());

//            for (GHNProvince item : listData){
//                log.info("Item {}, {}", item.getProvinceId(), item.getProvinceName());
//            }
        } else {
            return new ArrayList<>();
        }
    }

}
