package com.example.producer;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.Random;

@Component
public class Client implements MessageListener {

    private Session session;
    private Destination tempQueue;
    private MessageProducer producer;

    public Client(@Value("${spring.activemq.broker-url}") String brokerUrl,
                  @Value("${spring.activemq.queue}") String clientQueueName) {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
        try {
            Connection connection = connectionFactory.createConnection();
            connection.start();

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination adminQueue = session.createQueue(clientQueueName);

            //Setup a message producer to send message to the queue the server is consuming from
            producer = session.createProducer(adminQueue);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            //Create a temporary queue that this client will listen for responses on then create a consumer
            //that consumes message from this temporary queue...for a real application a client should reuse
            //the same temp queue for each message to the server...one temp queue per client
            tempQueue = session.createTemporaryQueue();
            MessageConsumer responseConsumer = session.createConsumer(tempQueue);

            //This class will handle the messages to the temp queue as well
            responseConsumer.setMessageListener(this);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                System.out.println("messageText = " + ((TextMessage) message).getText());
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void produceMessage(String message) {
        try {
            TextMessage textMessage = buildMessage(message);
            producer.send(textMessage);
            System.out.println("Message produced");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private TextMessage buildMessage(String message) throws JMSException {
        TextMessage textMessage = session.createTextMessage();
        textMessage.setText(message);

        //Set the reply to field to the temp queue that is created above, this is the queue the server
        //will respond to
        textMessage.setJMSReplyTo(tempQueue);

        //Set a correlation ID so when you get a response you know which sent message the response is for
        //If there is never more than one outstanding message to the server then the
        //same correlation ID can be used for all the messages...if there is more than one outstanding
        //message to the server you would presumably want to associate the correlation ID with this
        //message somehow...a Map works good
        String correlationId = createRandomString();
        textMessage.setJMSCorrelationID(correlationId);
        return textMessage;
    }

    private String createRandomString() {
        Random random = new Random(System.currentTimeMillis());
        return Long.toHexString(random.nextLong());
    }
}