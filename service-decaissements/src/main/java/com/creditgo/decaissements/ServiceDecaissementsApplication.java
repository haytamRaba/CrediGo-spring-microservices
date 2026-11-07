package com.creditgo.decaissements;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ServiceDecaissementsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceDecaissementsApplication.class, args);
    }

}
