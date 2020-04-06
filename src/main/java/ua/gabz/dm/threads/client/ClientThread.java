package ua.gabz.dm.threads.client;

import ua.gabz.dm.entities.message.Message;
import ua.gabz.dm.entities.message.Stream;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by destr on 15.03.2020.
 */
public interface ClientThread extends Runnable {

    void initializate(Socket socket) throws IOException;

    void send(String msg);

    Message take();

    boolean authorithate();

    void close();

}
