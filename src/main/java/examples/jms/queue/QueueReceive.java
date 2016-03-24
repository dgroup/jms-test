package examples.jms.queue;

import javax.jms.*;
import javax.naming.Context;

import static examples.jms.queue.JmsUtil.*;

/**
 * This example shows how to establish a connection to
 * and receive messages from a JMS queue. The classes in this
 * package operate on the same JMS queue. Run the classes together to
 * witness messages being sent and received, and to browse the queue
 * for messages.  This class is used to receive and remove messages
 * from the queue.
 *
 * @author Copyright (c) 1999-2005 by BEA Systems, Inc. All Rights Reserved.
 */
public class QueueReceive implements MessageListener, AutoCloseable {

    private final JMSContext context;
    private final JMSConsumer consumer;

    private boolean quit = false;

    public QueueReceive(Context ctx, String queueName) {
        Queue queue = lookup(ctx, queueName, Queue.class);
        context     = lookup(ctx, JMS_FACTORY, QueueConnectionFactory.class).createContext();
        consumer    = context.createConsumer(queue);
        consumer.setMessageListener(this);
    }


    @Override
    public void onMessage(Message msg) {
        try {
            String msgText = msg instanceof TextMessage? ((TextMessage)msg).getText() : msg.toString();

            System.out.println("Message received: "+ msgText );

            if ("quit".equalsIgnoreCase(msgText)) {
                synchronized(this) {
                    quit = true;
                    this.notifyAll(); // Notify main thread to quit
                }
            }
        } catch (JMSException jmse) {
            System.err.println("An exception occurred: "+jmse.getMessage());
        }
    }

    public void close() {
        JmsUtil.close(context, consumer);
    }




    public static void main(String[] args) {
        Context jndi = args.length == 0
            ? getDefaultWeblogicJndiContext()
            : getWebLogicJndiContext(args[0]);

        try(QueueReceive qr = new QueueReceive(jndi, QUEUE)) {
            System.out.println("JMS Ready To Receive Messages (To quit, send a \"quit\" message).");

            // Wait until a "quit" message has been received.
            synchronized (qr) {
                while (!qr.quit) {
                    try {
                        qr.wait();
                    } catch (InterruptedException ie) {
                        // no actions required for our case
                    }
                }
            }
        }
    }
}