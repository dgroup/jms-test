package examples.jms.queue;

import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.naming.Context;
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
public class QueueSend implements AutoCloseable {

    private final JMSContext context;
    private final JMSProducer producer;
    private final Queue queue;


    public QueueSend(Context ctx, String queueName) {
        context     = lookup(ctx, JMS_FACTORY, QueueConnectionFactory.class).createContext();
        producer    = context.createProducer();
        queue       = lookup(ctx, queueName, Queue.class);
    }


    public void send(String message) {
        producer.send(queue, message);
    }


    public static void main(String[] args) throws IOException {
        Context jndi = args.length == 0
            ? getDefaultWeblogicJndiContext()
            : getWebLogicJndiContext(args[0]);

        try(QueueSend qs = new QueueSend(jndi, QUEUE)) {
            // Read user input and send to jms queue
            BufferedReader msgStream = new BufferedReader(new InputStreamReader(System.in));
            String line;
            do {
                System.out.println("Enter message (\"quit\" to quit): ");
                line = msgStream.readLine();
                if (isNoneBlank(line)) {
                    qs.send(line);
                    System.out.println("JMS Message Sent: " + line + "\n");
                }
            } while (!"quit".equalsIgnoreCase(line));
        }
    }

    @Override
    public void close() {
        JmsUtil.close(context);
    }
}