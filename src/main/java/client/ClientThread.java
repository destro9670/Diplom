package client;

import messages.ErrorMessage;
import messages.Message;
import messages.ClientMessage;
import messages.enums.ErrorType;
import services.AuthServiceImpl;
import services.AuthService;
import services.StreamServiceImpl;
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
            authService.toAuthorize();



        }catch (IllegalArgumentException e){
            logger.error(e.getMessage());
            sendMessage(new ErrorMessage(ErrorType.AUTH));
            closeThread();
        }
    }

    private void close() throws Exception {
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

    public void sendMessage(Message msg) {

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
            sendMessage(new ErrorMessage(ErrorType.STREAM));
            closeThread();
        }
        return msg;
    }
}

