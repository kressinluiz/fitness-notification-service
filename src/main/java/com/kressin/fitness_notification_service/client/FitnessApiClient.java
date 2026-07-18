package com.kressin.fitness_notification_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.kressin.fitness_notification_service.client.dto.WorkoutPlanResponse;

@Component
public class FitnessApiClient {
    private final RestClient restClient;

    public FitnessApiClient(@Value("${fitness-api.base-url:http://localhost:8080}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    // implementar log do que foi fetched
    public WorkoutPlanResponse getWorkoutPlan(Long workoutPlanId) {
        return restClient.get()
                .uri("/planner/{id}", workoutPlanId)
                .retrieve()
                .body(WorkoutPlanResponse.class);
    }
}
