package com.kressin.fitness_notification_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kressin.fitness_notification_service.client.dto.WorkoutPlanResponse;

@Configuration
public class RedisConfig {
    @Bean
    RedisTemplate<String, WorkoutPlanResponse> redisTemplate(RedisConnectionFactory factory) {
        ObjectMapper objectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();

        Jackson2JsonRedisSerializer<WorkoutPlanResponse> serializer = new Jackson2JsonRedisSerializer<>(objectMapper,
                WorkoutPlanResponse.class);

        RedisTemplate<String, WorkoutPlanResponse> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        return template;
    }
}
