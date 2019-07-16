package com.debosz.kamil.ticketmachine.service;

import com.debosz.kamil.domain.carstop.CarStop;
import com.debosz.kamil.dto.ResponseDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TicketMachineService {

    private RestTemplate restTemplate;

    public TicketMachineService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    ResponseDTO<CarStop> enterParking() {
        return restTemplate.exchange("http://localhost:8080/parking/entry/regular", HttpMethod.GET, null, new ParameterizedTypeReference<ResponseDTO<CarStop>>() {
        }).getBody();
    }

    String enterParkingWithSubscription(String cardCode) {
        return restTemplate.postForObject("http://localhost:8080/parking/entry/subscription", cardCode, String.class);
    }

    ResponseDTO<CarStop> departure(String ticketCode) {
        HttpEntity<String> request = new HttpEntity<>(ticketCode);
        return restTemplate.exchange("http://localhost:8080/parking/departure/regular", HttpMethod.POST, request, new ParameterizedTypeReference<ResponseDTO<CarStop>>() {
        }).getBody();
    }

    String departureWithSubscription(String ticketCode) {
        return restTemplate.postForObject("http://localhost:8080/parking/departure/subscription", ticketCode, String.class);
    }

}
