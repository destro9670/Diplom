package ua.gabz.dm.threads.client;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import ua.gabz.dm.auth.Auth;
import ua.gabz.dm.auth.AuthService;
import ua.gabz.dm.entities.enums.CriptoAlgoritm;
import ua.gabz.dm.entities.message.Message;
import ua.gabz.dm.threads.connector.ConnectionListener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by destr on 15.03.2020.
 */
public class ClientThreadImpl implements ClientThread {

    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    private CriptoAlgoritm algoritm;

    private static final Logger logger = Logger.getLogger(ClientThreadImpl.class);


    private Auth auth;

    @Override
    public void initializate(Socket socket) throws IOException { ///initialize base params
        this.socket = socket;
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
        auth = new AuthService();
    }

    @Override
    public void send(String msg) { /// method which send msg to client device
        try {
            dos.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Message take() {
        return null;
    }

    @Override
    public boolean authorithate() { ///auth proces

        if( auth.openStream(dis, dos,this)&&   /// try to open JSON stream
            auth.getClientParametrs(dis,dos,this)&&  ///getint info about client device
            auth.cripto(dis, dos,this)&&  ///cripto closing stream
            auth.openCriptoStream(dis,dos,this)&& ///open new cripted stream
            auth.toAutorize(dis,dos,this)    )
            return true;
        else
            return false;
    }

    @Override
    public void close() {  ///ending session
        try {
            dis.close();
            dos.close();
            socket.close();
            this.finalize();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

    @Override
    public void run() {

        if(authorithate()){
            logger.debug("auth done");
            while(true){

            }
        }
    }
}
