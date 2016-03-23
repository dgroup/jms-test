package examples.jms.queue;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.NamingException;

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
    private QueueConnection qcon;
    private QueueSession qsession;
    private QueueReceiver qreceiver;
    private boolean quit = false;

    /**
     * Creates all the necessary objects for receiving
     * messages from a JMS queue.
     *
     * @param ctx JNDI initial context
     * @param queueName name of queue
     */
    public QueueReceive(Context ctx, String queueName) {
        try {

            QueueConnectionFactory qconFactory = (QueueConnectionFactory) ctx.lookup(JMS_FACTORY);
            qcon = qconFactory.createQueueConnection();
            qsession = qcon.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = (Queue) ctx.lookup(queueName);
            qreceiver = qsession.createReceiver(queue);
            qreceiver.setMessageListener(this);
            qcon.start();

        } catch (NamingException |JMSException e) {
            throw new JmsInfrustructureException("Shit happens, mate", e);
        }
    }

    /**
     * Message listener interface.
     * @param msg  message
     */
    public void onMessage(Message msg) {
        try {
            String msgText = msg instanceof TextMessage? ((TextMessage)msg).getText() : msg.toString();

            System.out.println("Message Received: "+ msgText );

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




    /**
     * Closes JMS objects.
     * @exception JMSException if JMS fails to close objects due to internal error
     */
    public void close() {
        JmsUtil.close(qreceiver, qsession, qcon);
    }




    /**
     * @param `t3://127.0.0.1:7001`
     */
    public static void main(String[] args) throws NamingException, JMSException {
        if (args.length != 1) {
            System.out.println("Usage: java examples.jms.queue.QueueReceive WebLogicURL");
            return;
        }

        Context jndi = getWebLogicInitialContext(args[0]);

        try(QueueReceive qr = new QueueReceive(jndi, QUEUE)) {
            System.out.println("JMS Ready To Receive Messages (To quit, send a \"quit\" message).");

            // Wait until a "quit" message has been received.
            synchronized (qr) {
                while (!qr.quit) {
                    try {
                        qr.wait();
                    } catch (InterruptedException ie) {}
                }
            }
        }
    }
}