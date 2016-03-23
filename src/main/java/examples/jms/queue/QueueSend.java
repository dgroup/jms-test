package examples.jms.queue;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.NamingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static examples.jms.queue.JmsUtil.*;
import static org.apache.commons.lang3.StringUtils.isNoneBlank;

/** This example shows how to establish a connection
 * and send messages to the JMS queue. The classes in this
 * package operate on the same JMS queue. Run the classes together to
 * witness messages being sent and received, and to browse the queue
 * for messages. The class is used to send messages to the queue.
 *
 * @author Copyright (c) 1999-2005 by BEA Systems, Inc. All Rights Reserved.
 */
public class QueueSend implements AutoCloseable
{
    private QueueConnection qcon;
    private QueueSession qsession;
    private QueueSender qsender;
    private TextMessage msg;


    /**
     * Creates all the necessary objects for sending
     * messages to a JMS queue.
     *
     * @param ctx JNDI initial context
     * @param queueName name of queue
     */
    public QueueSend(Context ctx, String queueName) {
        try {

            QueueConnectionFactory qconFactory = (QueueConnectionFactory) ctx.lookup(JMS_FACTORY);
            qcon = qconFactory.createQueueConnection();
            qsession = qcon.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = (Queue) ctx.lookup(queueName);
            qsender = qsession.createSender(queue);
            msg = qsession.createTextMessage();
            qcon.start();

        } catch (NamingException|JMSException e){
            throw new JmsNotificationException("Shit happens, mate.", e);
        }
    }


    public void send(String message) {
        try {

            msg.setText(message);
            qsender.send(msg);

        } catch (JMSException e){
            throw new JmsNotificationException("Unable to send a message: " + message, e);
        }
    }

    public void close() {
        JmsUtil.close(qsender, qsession, qcon);
    }

    /**
     * @param `t3://127.0.0.1:7001`
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java examples.jms.queue.QueueSend WebLogicURL");
            return;
        }

        Context jndi = getWebLogicInitialContext(args[0]);

        try(QueueSend qs = new QueueSend(jndi, QUEUE)){
            readAndSend(qs);
        }
    }


    private static void readAndSend(QueueSend qs)
        throws IOException
    {
        BufferedReader msgStream = new BufferedReader(new InputStreamReader(System.in));
        String line;
        do {
            System.out.println("Enter message (\"quit\" to quit): ");
            line = msgStream.readLine();
            if (isNoneBlank(line)) {
                qs.send(line);
                System.out.println("JMS Message Sent: "+line+"\n");
            }
        } while (! "quit".equalsIgnoreCase(line));

    }
}