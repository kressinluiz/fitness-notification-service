package com.kressin.fitness_notification_service.notification.entity;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.kressin.fitness_notification_service.notification.enums.NotificationStatus;
import com.kressin.fitness_notification_service.notification.enums.NotificationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private Long ownerId;

    @Column(nullable = false, unique = true)
    private UUID messageId;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    private ZonedDateTime scheduledAt;

    @OneToMany(mappedBy = "notification")
    private List<NotificationHistory> notificationHistory = new ArrayList<>();

    protected Notification() {
    }

    public Notification(Long ownerId, UUID messageId, NotificationType type,
            NotificationStatus status, ZonedDateTime scheduledAt) {
        this.ownerId = ownerId;
        this.messageId = messageId;
        this.type = type;
        this.status = status;
        this.scheduledAt = scheduledAt;
    }

    public UUID getId() {
        return id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public NotificationType getType() {
        return type;
    }

    public ZonedDateTime getScheduledAt() {
        return scheduledAt;
    }
}
