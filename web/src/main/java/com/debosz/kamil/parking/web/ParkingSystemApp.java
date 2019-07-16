package com.debosz.kamil.parking.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.debosz.kamil.service", "com.debosz.kamil.repository"})
@EntityScan(basePackages = {"com.debosz.kamil.domain"})
@ComponentScan(basePackages = {"com.debosz.kamil.parking.web.rest", "com.debosz.kamil.service", "com.debosz.kamil.repository", "com.debosz.kamil.domain"})
public class ParkingSystemApp {

    public static void main(String[] args) {
        SpringApplication.run(ParkingSystemApp.class, args);
    }

}
