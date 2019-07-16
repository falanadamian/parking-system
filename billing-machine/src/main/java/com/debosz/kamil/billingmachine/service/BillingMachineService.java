package com.debosz.kamil.billingmachine.service;

import com.debosz.kamil.domain.carstop.CarStop;
import com.debosz.kamil.domain.driver.Driver;
import com.debosz.kamil.domain.driver.PersonalInformation;
import com.debosz.kamil.dto.ResponseDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BillingMachineService {

    private RestTemplate restTemplate;

    public BillingMachineService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    ResponseDTO<CarStop> payForTicket(String ticketCode) {
        HttpEntity<String> request = new HttpEntity<>(ticketCode);
        return restTemplate.exchange("http://localhost:8080/parking/payment", HttpMethod.POST, request, new ParameterizedTypeReference<ResponseDTO<CarStop>>() {
        }).getBody();
    }

    ResponseDTO<Driver> issueSubscriptionCard(PersonalInformation personalInformation) {
        HttpEntity<PersonalInformation> request = new HttpEntity<>(personalInformation);
        return restTemplate.exchange("http://localhost:8080/parking/subscription/issue", HttpMethod.POST, request, new ParameterizedTypeReference<ResponseDTO<Driver>>() {
        }).getBody();
    }

}
