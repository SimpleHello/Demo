package quertz.demo.main;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * 
 * @author John
 *
 */
public class SyncServer {

    private final AbstractApplicationContext springContext;

    public SyncServer() {
        springContext = new ClassPathXmlApplicationContext("classpath:conf/applicationContext.xml");
    }

    public void stop() {
        springContext.close();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SyncServer server = new SyncServer();
        Runtime.getRuntime().addShutdownHook(new ShutdownThread(server));
    }

    private static class ShutdownThread extends Thread {

        private final SyncServer server;

        public ShutdownThread(SyncServer server) {
            this.server = server;
        }

        @Override
        public void run() {
            server.stop();
        }
    }
    
}
