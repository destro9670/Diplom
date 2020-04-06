package ua.gabz.dm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ua.gabz.dm.threads.connector.ConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;

@SpringBootApplication
public class ServerStarter {

    public static void main(String[] args) {
        SpringApplication.run(ServerStarter.class, args);
        try {
            ServerSocket ss = new ServerSocket(5222);
            ConnectionListener listener = new ConnectionListener();
            listener.listen(ss);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
