package com.example.demo.ibmmq;

import com.example.demo.Config;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component // for sample 1 only , mark it when need to try other samples
public class Listener {
    static boolean warned = false;

    @JmsListener(destination = Config.qName)
    public void receiveMessage(String msg) {
        infinityWarning();

        System.out.println();
        System.out.println("========================================");
        System.out.println("Received message is: " + msg);
        System.out.println("========================================");

    }

    void infinityWarning() {
        if (!warned) {
            warned = true;
            System.out.println();
            System.out.println("========================================");
            System.out.println("MQ JMS Listener started for queue: " + Config.qName);
            System.out.println("NOTE: This program does not automatically end - it continues to wait");
            System.out.println("      for more messages, so you may need to hit BREAK to end it.");
            System.out.println("========================================");
        }
    }
}
