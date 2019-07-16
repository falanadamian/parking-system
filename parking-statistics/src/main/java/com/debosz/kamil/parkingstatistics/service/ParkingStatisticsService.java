package com.debosz.kamil.parkingstatistics.service;

import com.debosz.kamil.dto.ResponseDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ParkingStatisticsService {

    private RestTemplate restTemplate;

    public ParkingStatisticsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    ResponseDTO<Integer> getAllParkedNow() {
        return restTemplate.exchange("http://localhost:8080/statistics/parked/all", HttpMethod.GET, null, new ParameterizedTypeReference<ResponseDTO<Integer>>() {
        }).getBody();
    }

    ResponseDTO<Integer> getTicketParkedNow() {
        return restTemplate.exchange("http://localhost:8080/statistics/parked/ticket", HttpMethod.GET, null, new ParameterizedTypeReference<ResponseDTO<Integer>>() {
        }).getBody();
    }

    ResponseDTO<Integer> getSubscriptionParkedNow() {
        return restTemplate.exchange("http://localhost:8080/statistics/parked/subscription", HttpMethod.GET, null, new ParameterizedTypeReference<ResponseDTO<Integer>>() {
        }).getBody();
    }

    String getDayDateFormat() {
        return restTemplate.getForObject("http://localhost:8080/statistics/dateformat/day", String.class);
    }

    String getMonthDateFormat() {
        return restTemplate.getForObject("http://localhost:8080/statistics/dateformat/month", String.class);
    }

    String getYearDateFormat() {
        return restTemplate.getForObject("http://localhost:8080/statistics/dateformat/year", String.class);
    }

    ResponseDTO<Integer> getAllEntriesOnDate(String date) {
        HttpEntity<String> request = new HttpEntity<>(date);
        return restTemplate.exchange("http://localhost:8080/statistics/entries/all", HttpMethod.POST, request, new ParameterizedTypeReference<ResponseDTO<Integer>>() {
        }).getBody();
    }

    ResponseDTO<Integer> getTicketEntriesOnDate(String date) {
        HttpEntity<String> request = new HttpEntity<>(date);
        return restTemplate.exchange("http://localhost:8080/statistics/entries/ticket", HttpMethod.POST, request, new ParameterizedTypeReference<ResponseDTO<Integer>>() {
        }).getBody();
    }

    ResponseDTO<Integer> getSubscriptionEntriesOnDate(String date) {
        HttpEntity<String> request = new HttpEntity<>(date);
        return restTemplate.exchange("http://localhost:8080/statistics/entries/subscription", HttpMethod.POST, request, new ParameterizedTypeReference<ResponseDTO<Integer>>() {
        }).getBody();
    }

    ResponseDTO<Integer> getAllDeparturesOnDate(String date) {
        HttpEntity<String> request = new HttpEntity<>(date);
        return restTemplate.exchange("http://localhost:8080/statistics/departures/all", HttpMethod.POST, request, new ParameterizedTypeReference<ResponseDTO<Integer>>() {
        }).getBody();
    }

    ResponseDTO<Integer> getTicketDeparturesOnDate(String date) {
        HttpEntity<String> request = new HttpEntity<>(date);
        return restTemplate.exchange("http://localhost:8080/statistics/departures/ticket", HttpMethod.POST, request, new ParameterizedTypeReference<ResponseDTO<Integer>>() {
        }).getBody();
    }

    ResponseDTO<Integer> getSubscriptionDeparturesOnDate(String date) {
        HttpEntity<String> request = new HttpEntity<>(date);
        return restTemplate.exchange("http://localhost:8080/statistics/departures/subscription", HttpMethod.POST, request, new ParameterizedTypeReference<ResponseDTO<Integer>>() {
        }).getBody();
    }


}
