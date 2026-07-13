package com.kressin.fitness_notification_service;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class RabbitTopologyConfig {
    @Bean
    DirectExchange fitnessEventsExchange() {
        return new DirectExchange(RabbitConfig.EXCHANGE, true, false);
    }

    @Bean
    Queue workoutScheduledQueue() {
        return new Queue(RabbitConfig.QUEUE_WORKOUT_SCHEDULED, true);
    }

    @Bean
    Binding workoutScheduledBinding() {
        return BindingBuilder
                .bind(workoutScheduledQueue())
                .to(fitnessEventsExchange())
                .with(RabbitConfig.ROUTING_KEY_WORKOUT_SCHEDULED);
    }

    @Bean
    MessageConverter jsonMessageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }
}
