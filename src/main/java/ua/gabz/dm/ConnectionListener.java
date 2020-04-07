package ua.gabz.dm;

import org.jboss.logging.Logger;
import ua.gabz.dm.clientThread.ClientThread;
import ua.gabz.dm.clientThread.IClientThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionListener implements IConnectionListener {
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
        try( IClientThread clientThread = new ClientThread(socket)) {
            clientThread.run();
        } catch (Exception e) {
            logger.error(e);
        }
    }

}
