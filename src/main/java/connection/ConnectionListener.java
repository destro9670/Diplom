package connection;

import org.jboss.logging.Logger;
import client.ClientThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionListener {
    private static final Logger logger = Logger.getLogger(ConnectionListener.class);

    public void listen(ServerSocket ss) {
        while (true){
            try {
                createClientThread(ss.accept());
                logger.info("New Connection");
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }


    }


    private void createClientThread(Socket socket) {
        try( ClientThread clientThread = new ClientThread(socket)) {
            clientThread.start();
        } catch (Exception e) {
            logger.error(e);
        }
    }

}
