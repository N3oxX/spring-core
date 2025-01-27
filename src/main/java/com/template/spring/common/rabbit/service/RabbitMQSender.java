package com.template.spring.common.rabbit.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public RabbitMQSender(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendMessageWithPriority(Object message, int priority) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            rabbitTemplate.convertAndSend("alertExchange", "alert.abinitio.run", jsonMessage, m -> {
                m.getMessageProperties().setPriority(priority);
                m.getMessageProperties().setContentType("application/json");
                return m;
            });
            System.out.println("Sent message with priority " + priority + ": " + jsonMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}