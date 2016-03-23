package examples.jms.queue;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

import static javax.naming.Context.INITIAL_CONTEXT_FACTORY;
import static javax.naming.Context.PROVIDER_URL;

final class JmsUtil {

    // Defines the JMS connection factory for the queue.
    final static String JMS_FACTORY  = "jms/TestConnectionFactory";

    // Defines the queue.
    final static String QUEUE        = "jms/TestJMSQueue";


    private JmsUtil(){
        // no instances required
    }



    static InitialContext getWebLogicJndiContext(String url) {
        try {

            Hashtable<String, String> env = new Hashtable<>();
            env.put(INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
            env.put(PROVIDER_URL, url);
            return new InitialContext(env);

        } catch (NamingException e){
            throw new JmsInfrustructureException("Unable to build a JNDI context for ULR: "+url, e);
        }
    }



    static void close(AutoCloseable ...closeable) {
        try {
            for(AutoCloseable close : closeable)
                close.close();
        } catch (Exception e) {
            throw new JmsInfrustructureException("Something wrong with our JMS stuff", e);
        }
    }
}