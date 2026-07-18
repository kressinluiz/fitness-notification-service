package com.kressin.fitness_notification_service.client.cache;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.kressin.fitness_notification_service.client.FitnessApiClient;
import com.kressin.fitness_notification_service.client.dto.WorkoutPlanResponse;

@Component
public class WorkoutPlanCache {

    private static final Duration TTL = Duration.ofMinutes(10);

    private final RedisTemplate<String, WorkoutPlanResponse> redisTemplate;
    private final FitnessApiClient fitnessApiClient;

    public WorkoutPlanCache(RedisTemplate<String, WorkoutPlanResponse> redisTemplate,
            FitnessApiClient fitnessApiClient) {
        this.redisTemplate = redisTemplate;
        this.fitnessApiClient = fitnessApiClient;
    }

    // implementar log de cache hit/cache miss
    public WorkoutPlanResponse getWorkoutPlan(Long workoutPlanId) {
        String key = "workoutplan:" + workoutPlanId;

        WorkoutPlanResponse cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return cached;
        }

        WorkoutPlanResponse fetched = fitnessApiClient.getWorkoutPlan(workoutPlanId);
        redisTemplate.opsForValue().set(key, fetched, TTL);
        return fetched;
    }
}
