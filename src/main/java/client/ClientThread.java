package client;

import db.models.User;
import messages.Message;
import messages.ClientMessage;
import org.json.JSONException;
import services.communication.MessageAnalyserService;
import services.сonnection.AuthService;
import services.сonnection.AuthServiceImpl;
import services.сonnection.StreamServiceImpl;
import utiles.ClientHolderUtil;

import javax.net.ssl.SSLSocket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

public class ClientThread implements Runnable {

    private static final Logger logger = Logger.getLogger(ClientThread.class);

    private final SSLSocket socket;
    private final DataOutputStream dos;
    private final DataInputStream dis;
    private AuthService authService;
    private StreamServiceImpl stream;
    private User user;
    private MessageAnalyserService analyserService;
    private String inRoom = "NuN";

    public ClientThread(SSLSocket socket) throws IOException {
        socket.startHandshake();
        socket.setKeepAlive(true);
        socket.setSoTimeout(2 * 60 * 60 * 1000);
        this.socket = socket;

        this.dos = new DataOutputStream(socket.getOutputStream());
        this.dis = new DataInputStream(socket.getInputStream());
        authService = new AuthServiceImpl(this);
        stream = new StreamServiceImpl(this);
    }


    @Override
    public void run() {
        try {
            stream.open();
            logger.info("Open Stream success");

            user = authService.toAuthorize();
            logger.info(user.getNick() + " login success");

            analyserService = new MessageAnalyserService(user);
            logger.info("Start sending not receiving  messages");

            analyserService.sendUnsendedMessages();

            logger.info("Start working with client");

            while (!Thread.currentThread().isInterrupted()) {
                analyserService.analyze(takeData());

            }

            logger.info(user.getNick() + "disconnected");

        } catch (IllegalArgumentException | IOException | JSONException e) {
            logger.trace(e);
            closeThread();
        } catch (NullPointerException e) {
            logger.info("Client Disconnected");
            logger.trace(e);
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
        } catch (Exception e) {
            logger.trace(e);
        }
    }


    public void closeThread() {
        close();
    }

    public synchronized void sendMessage(Message msg) {

        try {
            dos.writeUTF(msg.getTextMessage());
            if(user != null)
                System.out.println("sended by " + user.getNick() + ": "+msg.getTextMessage());

        } catch (IOException e) {

            logger.trace(e);
            closeThread();
        }

    }

    public Message takeData() throws IOException {
        String data = dis.readUTF();
        if(user!= null)
            System.out.println("taked by " + user.getNick() + ": " + data);
        return new ClientMessage(data);
    }

    public synchronized String getInRoom() {
        return inRoom;
    }

    public synchronized void setInRoom(String inRoom) {
        this.inRoom = inRoom;
    }
}

