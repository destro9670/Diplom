package ua.gabz.dm.clientThread;

import ua.gabz.dm.messages.IMessage;

import java.io.IOException;
import java.net.Socket;

public interface IClientThread extends Runnable, AutoCloseable{

    void sendMessage(IMessage msg);

    IMessage takeData();


}
