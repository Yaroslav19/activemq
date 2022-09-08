package com.example.consumervirtualtopic;

import com.example.common.domain.Model;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Listener {

    @JmsListener(destination = "Consumer.myConsumer1.VirtualTopic.MY-SUPER-TOPIC", containerFactory = "queueListenerContainer")
    public void consumer1Listener(Model model) {
        System.out.println("myConsumer1 received message: " + model);
    }

    @JmsListener(destination = "Consumer.myConsumer2.VirtualTopic.MY-SUPER-TOPIC", containerFactory = "queueListenerContainer")
    public void consumer2Listener(Model model) {
        System.out.println("myConsumer2 received message: " + model);
    }
}
