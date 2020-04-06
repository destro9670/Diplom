package ua.gabz.dm.threads.connector;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by destr on 15.03.2020.
 */
public interface Listener {

    void listen(ServerSocket ss);

    void createClientThread(Socket socket);
}
