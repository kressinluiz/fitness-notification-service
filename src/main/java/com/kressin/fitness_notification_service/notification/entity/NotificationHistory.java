package com.kressin.fitness_notification_service.notification.entity;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.kressin.fitness_notification_service.notification.enums.NotificationHistoryStatus;
import com.kressin.fitness_notification_service.notification.enums.NotificationType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class NotificationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "notification_id")
    private Notification notification;

    private ZonedDateTime scheduledFor;

    private NotificationHistoryStatus status;

    private String errorMessage;

    public NotificationHistory(Notification notification, ZonedDateTime scheduledFor, NotificationType type,
            NotificationHistoryStatus status) {
        this.notification = notification;
        this.scheduledFor = scheduledFor;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public Notification getNotification() {
        return notification;
    }

    public NotificationHistoryStatus getStatus() {
        return status;
    }

    public ZonedDateTime getScheduledFor() {
        return scheduledFor;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
