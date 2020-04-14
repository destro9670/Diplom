package client;

import db.models.User;
import messages.Message;
import messages.ClientMessage;
import services.сonnection.AuthService;
import services.сonnection.AuthServiceImpl;
import services.сonnection.StreamServiceImpl;
import org.jboss.logging.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientThread implements Runnable {

    private static final Logger logger = Logger.getLogger(ClientThread.class);

    private final Socket socket;
    private final DataOutputStream dos;
    private final DataInputStream dis;
    private AuthService authService;
    private StreamServiceImpl stream;
    private User user;

    public ClientThread(Socket socket) throws IOException {
        this.socket = socket;
        this.dos = new DataOutputStream(socket.getOutputStream());
        this.dis = new DataInputStream(socket.getInputStream());
        authService = new AuthServiceImpl(this);
        stream = new StreamServiceImpl(this);
    }


    @Override
    public void run() {
        logger.info("New connection accepted ");


        try {
            stream.open();
            user = authService.toAuthorize();

            while (Thread.currentThread().isInterrupted()){
                takeData();
            }


        }catch (IllegalArgumentException e){
            logger.error(e.getMessage());
            closeThread();
        }
    }

    private void close() {

        try {
            dos.close();
            dis.close();
            socket.close();
            Thread.currentThread().interrupt();
        } catch (Exception e1) {
            logger.error(e1.getMessage());
        }
    }


    public void closeThread(){
            close();
    }

    public synchronized void sendMessage(Message msg) {

        try {
            dos.writeUTF(msg.getTextMessage());

        } catch (IOException e) {

            logger.error(e.getMessage());
            closeThread();
        }

    }

    public Message takeData() {
        Message msg = null;
        try {
            msg = new ClientMessage(dis.readUTF());
        } catch (IOException e) {
            logger.error(e.getMessage());
            closeThread();
        }
        return msg;
    }

}

