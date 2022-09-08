package com.example.consumervirtualtopic.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

@EnableJms
@Configuration
public class ListenerConfig {

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;

    @Value("${spring.activemq.user}")
    private String user;

    @Value("${spring.activemq.password}")
    private String password;

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory() {
        return new ActiveMQConnectionFactory(user, password, brokerUrl);
    }

    @Bean("queueListenerContainer")
    public DefaultJmsListenerContainerFactory queueListenerContainer(ActiveMQConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory listenerContainerFactory = new DefaultJmsListenerContainerFactory();
        //Listening is Queue
        listenerContainerFactory.setPubSubDomain(false);
        listenerContainerFactory.setConnectionFactory(connectionFactory);
        return listenerContainerFactory;
    }
}
