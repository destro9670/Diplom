import connection.ConnectionListener;
import org.flywaydb.core.Flyway;
import org.jboss.logging.Logger;


import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public class ServerStarter {

    private static final Logger logger = Logger.getLogger(ServerStarter.class);

    ///TODO(1) normal console control
    public static void main(String[] args) {
       // migrate();
        try {
            int port = 5222;
            KeyStore keystore = KeyStore.getInstance("JKS");
            keystore.load(new FileInputStream("C:\\Users\\destr\\OneDrive\\Рабочий стол\\диплом\\блоки\\V_4\\Server_V_4_1\\src\\main\\resources\\keystore\\pigeon_server.ks"),
                    "server".toCharArray());
            logger.info("Incoming Connection\r\n");

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory
                    .getDefaultAlgorithm());
            kmf.init(keystore, "123456".toCharArray());
            ///TODO(2) password from property file

            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(keystore);
            logger.info("KeyStore Stored\r\n");
            TrustManager[] trustManagers = tmf.getTrustManagers();

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(kmf.getKeyManagers(), trustManagers, null);

            SSLServerSocketFactory sslServerSocketfactory = (SSLServerSocketFactory) context.getServerSocketFactory();
            SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketfactory.createServerSocket(port);
            sslServerSocket.setEnabledCipherSuites(sslServerSocket.getSupportedCipherSuites());
            sslServerSocket.setNeedClientAuth(true);
            ConnectionListener listener = new ConnectionListener();
            listener.listen(sslServerSocket);
        } catch (IOException | CertificateException | NoSuchAlgorithmException | UnrecoverableKeyException | KeyStoreException | KeyManagementException e) {
            logger.error(e.getMessage());
        }
    }

    private static void migrate() {
        Flyway flyway = Flyway.configure().dataSource("jdbc:postgresql://localhost:5432/test_db",
                "postgres", "qwerqwer").load();
        flyway.migrate();
    }


}
