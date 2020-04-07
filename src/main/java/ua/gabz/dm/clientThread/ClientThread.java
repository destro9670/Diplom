package ua.gabz.dm.clientThread;

import org.jboss.logging.Logger;
import ua.gabz.dm.messages.Error;
import ua.gabz.dm.messages.IMessage;
import ua.gabz.dm.messages.Message;
import ua.gabz.dm.messages.enums.ErrorType;
import ua.gabz.dm.services.AuthService;
import ua.gabz.dm.services.IAuthService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientThread implements IClientThread {

    private static final Logger logger = Logger.getLogger(ClientThread.class);

    private final Socket socket;
    private final DataOutputStream dos;
    private final DataInputStream dis;
    private IAuthService authService;

    public ClientThread(Socket socket) throws IOException {
        this.socket = socket;
        this.dos = new DataOutputStream(socket.getOutputStream());
        this.dis = new DataInputStream(socket.getInputStream());
    }


    @Override
    public void run() {
        logger.info("New connection accepted ");
        authService = new AuthService(this);
        try {
            authService.toAuthorize();



        }catch (IllegalArgumentException e){
            logger.error(e.getMessage());
            sendMessage(new Error(ErrorType.Auth));
            closeThread();
        }
    }

    @Override
    public void close() throws Exception {
        dos.close();
        dis.close();
        socket.close();
    }


    public void closeThread(){
        try {
            close();
        } catch (Exception e1) {
            logger.error(e1.getMessage());
        }
    }


    @Override
    public void sendMessage(IMessage msg) {

        try {
            dos.writeUTF(msg.getTextMessage());

        } catch (IOException e) {

            logger.error(e.getMessage());
            closeThread();
        }

    }

    @Override
    public IMessage takeData() {
        IMessage msg = null;
        try {
            msg = new Message(dis.readUTF());
        } catch (IOException e) {
            logger.error(e.getMessage());

            try {
                close();

            } catch (Exception e1) {
                logger.error(e1.getMessage());
            }
        }
        return msg;
    }
}

