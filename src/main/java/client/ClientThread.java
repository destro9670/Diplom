package client;

import db.models.User;
import messages.Message;
import messages.ClientMessage;
import org.json.JSONException;
import services.communication.MessageAnalyserService;
import services.сonnection.AuthService;
import services.сonnection.AuthServiceImpl;
import services.сonnection.StreamServiceImpl;
import org.jboss.logging.Logger;
import utiles.ClientHolderUtil;

import javax.net.ssl.SSLSocket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ClientThread implements Runnable {

    private static final Logger logger = Logger.getLogger(ClientThread.class);

    private final SSLSocket socket;
    private final DataOutputStream dos;
    private final DataInputStream dis;
    private AuthService authService;
    private StreamServiceImpl stream;
    private User user;
    private MessageAnalyserService analyserService;
    private String inRoom;

    public ClientThread(SSLSocket socket) throws IOException {
        socket.startHandshake();
        socket.setKeepAlive(true);
        socket.setSoTimeout(2*60*60*1000);
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
            analyserService = new MessageAnalyserService(user);

            analyserService.sendUnsendedMessages();

            while (Thread.currentThread().isInterrupted()){


            analyserService.analyze(takeData());
            }


        }catch (IllegalArgumentException | IOException | JSONException e){
            logger.error(e.getMessage());
            closeThread();
        }catch (NullPointerException e){
            logger.error("Client Disconnected");
            closeThread();
        }
    }

    private void close() {

        try {
            ClientHolderUtil.getInstance().removeClient(user.getNick());
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

    public Message takeData() throws IOException {
        return new ClientMessage(dis.readUTF());
    }

    public synchronized String getInRoom() {
        return inRoom;
    }

    public synchronized void setInRoom(String inRoom) {
        this.inRoom = inRoom;
    }
}

