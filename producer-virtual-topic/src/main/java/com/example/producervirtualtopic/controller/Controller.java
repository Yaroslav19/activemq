package com.example.producervirtualtopic.controller;

import com.example.common.domain.Model;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/message")
public class Controller {

    private final JmsTemplate jmsTemplate;
    private final ActiveMQTopic activeMQTopic;

    public Controller(JmsTemplate jmsTemplate, ActiveMQTopic activeMQTopic) {
        this.jmsTemplate = jmsTemplate;
        this.activeMQTopic = activeMQTopic;
    }

    @PostMapping
    public void produceMessage(@RequestParam String message) {
        jmsTemplate.convertAndSend(activeMQTopic, new Model(message));
        System.out.println("Message is produced: " + message);
    }
}
