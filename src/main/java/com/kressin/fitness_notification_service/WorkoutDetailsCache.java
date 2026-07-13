package com.kressin.fitness_notification_service;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class WorkoutDetailsCache {

    private static final Duration TTL = Duration.ofMinutes(10);

    private final RedisTemplate<String, WorkoutDetails> redisTemplate;
    private final FitnessApiClient fitnessApiClient;

    public WorkoutDetailsCache(RedisTemplate<String, WorkoutDetails> redisTemplate,
            FitnessApiClient fitnessApiClient) {
        this.redisTemplate = redisTemplate;
        this.fitnessApiClient = fitnessApiClient;
    }

    public WorkoutDetails getWorkoutDetails(Long workoutId) {
        String key = "workout:" + workoutId;

        WorkoutDetails cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return cached;
        }

        WorkoutDetails fetched = fitnessApiClient.getWorkoutDetails(workoutId);
        redisTemplate.opsForValue().set(key, fetched, TTL);
        return fetched;
    }
}
