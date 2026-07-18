package com.kressin.fitness_notification_service.notification.service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.kressin.fitness_notification_service.client.cache.WorkoutPlanCache;
import com.kressin.fitness_notification_service.client.dto.ScheduleEntryResponse;
import com.kressin.fitness_notification_service.client.dto.ScheduleType;
import com.kressin.fitness_notification_service.client.dto.WorkoutDateResponse;
import com.kressin.fitness_notification_service.client.dto.WorkoutPlanResponse;
import com.kressin.fitness_notification_service.messaging.mapper.WorkoutReminderMapper;
import com.kressin.fitness_notification_service.notification.dto.WorkoutReminder;
import com.kressin.fitness_notification_service.notification.entity.Notification;
import com.kressin.fitness_notification_service.notification.entity.NotificationHistory;
import com.kressin.fitness_notification_service.notification.enums.NotificationHistoryStatus;
import com.kressin.fitness_notification_service.notification.enums.NotificationStatus;
import com.kressin.fitness_notification_service.notification.enums.NotificationType;
import com.kressin.fitness_notification_service.notification.repository.NotificationHistoryRepository;
import com.kressin.fitness_notification_service.notification.repository.NotificationRepository;

@Service
public class NotificationSchedulerService {

    private final NotificationRepository notificationRepo;
    private final NotificationHistoryRepository notificationHistoryRepo;
    private final WorkoutPlanCache workoutPlanCache;
    private final LogNotificationSender notificationSender;
    private static final Logger log = LoggerFactory.getLogger(NotificationSchedulerService.class);

    public NotificationSchedulerService(
            NotificationRepository notificationRepo,
            NotificationHistoryRepository notificationHistoryRepo,
            WorkoutPlanCache workoutPlanCache,
            LogNotificationSender notificationSender) {
        this.notificationRepo = notificationRepo;
        this.notificationHistoryRepo = notificationHistoryRepo;
        this.workoutPlanCache = workoutPlanCache;
        this.notificationSender = notificationSender;
    }

    public void processPendingNotifications() {
        List<Notification> notifications = notificationRepo.findByStatus(NotificationStatus.ACTIVE);
        // e se a API estiver salvando em uma zone diferente da do serviço?
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MINUTES);
        notifications.forEach(notification -> processNotification(notification, now));
    }

    private void processNotification(Notification notification, ZonedDateTime now) {
        try {
            WorkoutPlanResponse workoutPlan = workoutPlanCache.getWorkoutPlan(notification.getOwnerId());
            WorkoutDateResponse workoutDate = workoutPlan.workoutDate();
            ScheduleType scheduleType = workoutDate.scheduleType();

            for (ScheduleEntryResponse entry : workoutDate.scheduleEntries()) {
                if (shouldTriggerNotification(entry, scheduleType, now)) {
                    ZonedDateTime scheduledFor = scheduledForNow(entry, scheduleType, now);

                    if (!notificationHistoryRepo.existsByNotificationIdAndScheduledFor(notification.getId(),
                            scheduledFor)) {
                        // send notify
                        WorkoutReminder reminder = WorkoutReminderMapper.toReminder(workoutPlan, scheduledFor);
                        notificationSender.send(reminder);
                        createNotificationHistory(notification, scheduledFor);
                    }
                }
            }
        } catch (Exception e) {
            log.error(
                    "Failed to process notification. notificationId={}, workoutPlanId={}",
                    notification.getId(),
                    notification.getOwnerId(),
                    e);
        }
    }

    // this function needs to be timezone safe
    private boolean shouldTriggerNotification(ScheduleEntryResponse entry, ScheduleType type, ZonedDateTime now) {
        ZonedDateTime scheduled = entry.dateTime();
        if (type == ScheduleType.RECURRING) {
            return entry.weekDay() == now.getDayOfWeek() &&
                    scheduled.getHour() == now.getHour() &&
                    scheduled.getMinute() == now.getMinute();
        }

        return scheduled.truncatedTo(ChronoUnit.MINUTES).equals(now);
    }

    private ZonedDateTime scheduledForNow(ScheduleEntryResponse entry, ScheduleType type, ZonedDateTime now) {
        if (type == ScheduleType.RECURRING) {
            return now.truncatedTo(ChronoUnit.MINUTES);
        }

        return entry.dateTime().truncatedTo(ChronoUnit.MINUTES);
    }

    private void createNotificationHistory(Notification notification, ZonedDateTime scheduledFor) {
        NotificationHistory history = new NotificationHistory(notification, scheduledFor,
                NotificationType.WORKOUT_REMINDER, NotificationHistoryStatus.SUCCESS);
        notificationHistoryRepo.saveAndFlush(history);
    }
}
