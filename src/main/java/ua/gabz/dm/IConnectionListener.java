package ua.gabz.dm;

import java.net.ServerSocket;
import java.net.Socket;

public interface IConnectionListener {

    void listen(ServerSocket ss);

    void createClientThread(Socket socket);
}
