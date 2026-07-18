package com.kressin.fitness_notification_service.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kressin.fitness_notification_service.notification.service.NotificationSchedulerService;

@Component
public class NotificationScheduler {

    private final NotificationSchedulerService schedulerService;

    public NotificationScheduler(NotificationSchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @Scheduled(fixedDelay = 50_000)
    public void processNotifications() {
        System.out.println("processNotifications: ...");
        schedulerService.processPendingNotifications();
    }
}
