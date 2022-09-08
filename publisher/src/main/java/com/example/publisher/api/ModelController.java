package com.example.publisher.api;

import com.example.common.domain.Model;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("v1/model")
public class ModelController {

    @Value("${activemq.topic}")
    private String topic;

    private final JmsTemplate jmsTemplate;

    public ModelController(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @PostMapping
    public void publishModel(@RequestParam(name = "name") String name) {
        Model message = new Model(name);
        jmsTemplate.convertAndSend(topic, message);
        log.info("Message : {} published to topic: {} successfully.", message, topic);
    }
}
