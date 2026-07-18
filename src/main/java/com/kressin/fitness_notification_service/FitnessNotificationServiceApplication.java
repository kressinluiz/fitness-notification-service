package com.kressin.fitness_notification_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FitnessNotificationServiceApplication {
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(FitnessNotificationServiceApplication.class, args);
    }
}
