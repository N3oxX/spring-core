package com.template.spring.common.rabbit.configuration;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "alertQueue";
    public static final String EXCHANGE_NAME = "alertExchange";


    private static final int MAX_PRIORITY = 10;

    @Bean
    public Queue helloQueue() {
        return QueueBuilder.durable(QUEUE_NAME)
                .maxPriority(MAX_PRIORITY)
                .build();
    }

    @Bean
    public TopicExchange helloExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue helloQueue, TopicExchange helloExchange) {
        return BindingBuilder.bind(helloQueue).to(helloExchange).with("alert.#");
    }
}