package com.kressin.fitness_notification_service.notification.service;

import org.springframework.stereotype.Component;

import com.kressin.fitness_notification_service.notification.dto.WorkoutReminder;

@Component
public class EmailNotificationSender
        implements NotificationSender {

    @Override
    public void send(WorkoutReminder reminder) {
        throw new UnsupportedOperationException("Email sender not implemented yet");
    }

}
