package ua.gabz.dm;

import org.jboss.logging.Logger;


import java.io.IOException;
import java.net.ServerSocket;

public class ServerStarter {

    private static final Logger logger = Logger.getLogger(ServerStarter.class);

    public static void main(String[] args) {
        try(ServerSocket ss = new ServerSocket(5222)) {
            ConnectionListener listener = new ConnectionListener();
            listener.listen(ss);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

    }


}
