package com.kressin.fitness_notification_service.client.dto;

import java.util.List;

public record WorkoutDateResponse(
        Long id,
        ScheduleType scheduleType,
        List<ScheduleEntryResponse> scheduleEntries) {
}
