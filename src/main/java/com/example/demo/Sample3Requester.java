package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.jms.*;
import java.util.Date;

@SpringBootApplication
@EnableJms
@EnableTransactionManagement
public class Sample3Requester {
    // Create a transaction manager object that will be used to control commit/rollback of operations in the listener.
    static JmsTransactionManager tm = new JmsTransactionManager();
//    public static final String qName = "DEV.QUEUE.1"; // A queue from the default MQ Developer container config

    static String correlID = null;
    static TextMessage message;

    // Construct a Transaction Manager that will control local transactions.
    @Bean
    public JmsTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        JmsTransactionManager transactionManager = new JmsTransactionManager(connectionFactory);
        return transactionManager;
    }

    public static void main(String[] args) throws JMSException {
        // Launch the application
        ConfigurableApplicationContext context = SpringApplication.run(Sample3Requester.class, args);

        printStarted();

        // Create the JMS Template object to control connections and sessions.
        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
        jmsTemplate.setReceiveTimeout(5 * 1000); // How long to wait for a reply - milliseconds

        // Create a single message with a timestamp
        String payload = "Hello from IBM MQ at " + new Date();

        // Send the message and wait for a reply for up to the specified timeout
        Message replyMsg = jmsTemplate.sendAndReceive(Config.qName, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                message = session.createTextMessage(payload);
                System.out.println("Sending message: " + message.getText());
                return message;
            }
        });


        if (replyMsg != null) {
            if (replyMsg instanceof TextMessage) {
                System.out.println("Reply message is: " + ((TextMessage) replyMsg).getText());
            }
            else {
                System.out.println("Reply message is: " + replyMsg.toString());
            }
        }
        else {
            System.out.println("No reply received");
        }

        System.out.println("Done.");
        System.exit(0);
    }

    static void printStarted() {
        System.out.println();
        System.out.println("========================================");
        System.out.println("MQ JMS Request/Reply Sample started.");
        System.out.println("========================================");
    }
}
