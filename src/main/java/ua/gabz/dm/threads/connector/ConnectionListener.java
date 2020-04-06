package ua.gabz.dm.threads.connector;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import sun.tools.jar.CommandLine;
import ua.gabz.dm.threads.client.ClientThread;
import ua.gabz.dm.threads.client.ClientThreadImpl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionListener implements Listener {

    private static final Logger logger = Logger.getLogger(ConnectionListener.class);

    @Override
    public void listen(ServerSocket ss) {
        while (true){
            try {
                createClientThread(ss.accept());
                logger.info("New Connection");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void createClientThread(Socket socket) {
        ClientThread clientThread = new ClientThreadImpl();
        try {
            clientThread.initializate(socket);
        } catch (IOException e) {
            logger.error(e);
            return;
        }
        clientThread.run();
    }
}
