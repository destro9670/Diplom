package utiles;

import client.ClientThread;
import messages.ErrorMessage;
import messages.enums.MessageType;
import messages.enums.SubType;
import org.jboss.logging.Logger;
import services.datadase.UserServise;

import java.util.HashMap;

public final class ClientHolderUtil {
    private static volatile ClientHolderUtil instance = null;
    private static final Logger log = Logger.getLogger(ClientHolderUtil.class);
    private HashMap<String, ClientThread> onlineClients;

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

    public ClientThread getOnlineClient(String nickName) {

        log.info("user " + nickName + ": " + onlineClients.containsKey(nickName));
        return onlineClients.getOrDefault(nickName, null);
    }

    public synchronized boolean addNewOnlineClient(String nickName, ClientThread client){
        if(!onlineClients.containsKey(nickName)){
            onlineClients.put(nickName,client);
            log.info("New Client added secess:" + nickName);
            return true;
        }else{
            log.info("New Client added failed:" + nickName);
            client.sendMessage(new ErrorMessage(SubType.GET, MessageType.AUTH,"Your Account is Online"));
            throw new  IllegalArgumentException(nickName +":" + "Accaunt is Oline");
        }
    }

    public boolean contains(String nick){
        return onlineClients.containsKey(nick);
    }

    public void removeClient(String nickName){
        if(onlineClients.containsKey(nickName)) {
            onlineClients.remove(nickName);
            log.info(nickName + " deleted");
        }
    }


}
