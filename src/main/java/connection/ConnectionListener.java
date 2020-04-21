package connection;

import org.jboss.logging.Logger;
import client.ClientThread;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import java.io.IOException;

public class ConnectionListener {
    private static final Logger logger = Logger.getLogger(ConnectionListener.class);

    public void listen(SSLServerSocket ss) {
        while (true){
            try {
                createClientThread((SSLSocket)ss.accept());
                logger.info("New Connection");
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }


    }


    private void createClientThread(SSLSocket socket) {
        ClientThread clientThread = null;
        try {
            clientThread = new ClientThread(socket);
            new Thread(clientThread).start();
        } catch (Exception e) {
            logger.error(e);

            if(clientThread != null)
                clientThread.closeThread();
        }
    }

}
