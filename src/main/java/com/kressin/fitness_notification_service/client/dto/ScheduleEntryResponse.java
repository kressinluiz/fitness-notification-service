package com.kressin.fitness_notification_service.client.dto;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;

public record ScheduleEntryResponse(
        Long id,
        DayOfWeek weekDay,
        ZonedDateTime dateTime) {
}
