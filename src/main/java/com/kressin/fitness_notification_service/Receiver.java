package com.kressin.fitness_notification_service;

import java.util.concurrent.CountDownLatch;

import org.springframework.stereotype.Component;

/*
Em qualquer aplicação com mensageria, deve ser criado uma classe responsável
por responder as mensagens publicadas
*/

@Component
public class Receiver {
    private CountDownLatch latch = new CountDownLatch(1);

    public void receiveMessage(String message) {
        System.out.println("Received <" + message + ">");
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}
