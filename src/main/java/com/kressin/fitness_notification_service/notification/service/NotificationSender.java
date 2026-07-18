package com.kressin.fitness_notification_service.notification.service;

import com.kressin.fitness_notification_service.notification.dto.WorkoutReminder;

public interface NotificationSender {
    void send(WorkoutReminder reminder);
}
