package com.example.demo.ibmmq;

import com.example.demo.Config;
import com.example.demo.Sample3Requester;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.*;

@Component // for sample 3
public class Sample3Responder implements SessionAwareMessageListener {
    @JmsListener(destination = Config.qName)
    @Transactional(rollbackFor = Exception.class)
    public void onMessage(Message msg, Session session) throws JMSException {
        String text;

        if (msg instanceof TextMessage) {
            text = ((TextMessage) msg).getText();
        }
        else {
            text = msg.toString();
        }

        System.out.println();
        System.out.println("========================================");

        System.out.println("Responder received message: " + text);
        System.out.println("           Redelivery flag: " + msg.getJMSRedelivered());
        System.out.println("========================================");

        final String msgID = msg.getJMSMessageID();
        Destination dest = msg.getJMSReplyTo();
        MessageProducer replyDest = session.createProducer(msg.getJMSReplyTo());
        TextMessage replyMsg = session.createTextMessage("Replying to " + text);
        replyMsg.setJMSCorrelationID(msgID);
        replyDest.send(replyMsg);

        // We deliberately fail the first attempt at sending a reply. The message is
        // put back on its original queue and then redelivered. At that point, we
        // try to commit the reply.
        if (!msg.getJMSRedelivered()) {
            System.out.println("Doing a rollback");
            session.rollback();
            /*throw new JMSException("Instead of rollback"); - might prefer this to see what happens*/
        }
        else {
            System.out.println("Doing a commit");
            session.commit();
        }

    }
}
