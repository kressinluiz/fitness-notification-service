package com.kressin.fitness_notification_service.notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.kressin.fitness_notification_service.notification.dto.WorkoutReminder;

@Component
public class LogNotificationSender implements NotificationSender {
    private static final Logger log = LoggerFactory.getLogger(LogNotificationSender.class);

    @Override
    public void send(WorkoutReminder reminder) {

        log.info("""
                ==================================================
                Workout Reminder

                Workout:
                {}

                Scheduled for:
                {}

                ==================================================
                """,
                reminder.workoutName(),
                reminder.scheduledFor());
    }
}
