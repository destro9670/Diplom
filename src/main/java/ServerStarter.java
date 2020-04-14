import connection.ConnectionListener;
import org.flywaydb.core.Flyway;
import org.jboss.logging.Logger;


import java.io.IOException;
import java.net.ServerSocket;

public class ServerStarter {

    private static final Logger logger = Logger.getLogger(ServerStarter.class);
    ///TODO(1) normal console control
    public static void main(String[] args) {
        migrate();

        try(ServerSocket ss = new ServerSocket(5222)) {
            ConnectionListener listener = new ConnectionListener();
            listener.listen(ss);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static void migrate(){
        Flyway flyway = Flyway.configure().dataSource("jdbc:postgresql://localhost:5432/test_db",
                "postgres", "qwerqwer").load();
        flyway.migrate();
    }


}
