package client;

import messages.IMessage;

public interface IClientThread extends Runnable, AutoCloseable{

    void sendMessage(IMessage msg);

    IMessage takeData();

    void closeThread();


}
