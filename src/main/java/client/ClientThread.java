package client;

import messages.ErrorMessage;
import messages.IMessage;
import messages.Message;
import messages.enums.ErrorType;
import services.AuthService;
import services.IAuthService;
import services.Stream;
import org.jboss.logging.Logger;

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
    private Stream stream;

    public ClientThread(Socket socket) throws IOException {
        this.socket = socket;
        this.dos = new DataOutputStream(socket.getOutputStream());
        this.dis = new DataInputStream(socket.getInputStream());
        authService = new AuthService(this);
        stream = new Stream(this);
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

