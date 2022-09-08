package com.example.subscriber.listener;

import com.example.common.domain.Model;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ModelListener {

    @JmsListener(destination = "${activemq.topic}", containerFactory = "jmsListenerContainerFactoryDurableSubscriber")
    public void receiveFromDurableSubscriber(Model model) {
        log.info("Message is received from durable: {}", model);
    }

    @JmsListener(destination = "${activemq.topic}", containerFactory = "jmsListenerContainerFactoryNonDurableSubscriber")
    public void receiveFromNonDurableSubscriber(Model model) {
        log.info("Message is received from non-durable: {}", model);
    }
}
