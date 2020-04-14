package utiles;

import client.ClientThread;
import org.jboss.logging.Logger;

import java.util.HashMap;

public final class ClientHolderUtil {
    private static volatile ClientHolderUtil instance = null;
    private static final Logger log = Logger.getLogger(ClientHolderUtil.class);
    private HashMap<String, ClientThread> onlineClients = null;

    private ClientHolderUtil(){
        onlineClients = new HashMap<>();
    }

    public static ClientHolderUtil getInstance(){
        ClientHolderUtil localInstance = instance;
        if(localInstance == null){
            synchronized (ClientHolderUtil.class){
                localInstance = instance;
                if (localInstance == null){
                    instance = localInstance = new ClientHolderUtil();
                }
            }
        }
        return localInstance;
    }

    public ClientThread getOnlineClient(String nickName){
        return onlineClients.get(nickName);
    }

    public boolean addNewOnlineClient(String nickName, ClientThread client){
        if(onlineClients.get(nickName)==null){
            onlineClients.put(nickName,client);
            log.info("New Client added secess:" + nickName);
            return true;
        }else{
            log.info("New Client added failed:" + nickName);
            return false;
        }
    }

    public void removeClient(String nickName){
        onlineClients.remove(nickName);
        log.info(nickName + " deleted");
    }


}
