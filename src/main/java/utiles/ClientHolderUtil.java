package utiles;

import client.ClientThread;
import db.models.User;
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
    private final UserServise userServise;

    private ClientHolderUtil(){
        this.userServise = new UserServise();
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
            User user = userServise.findUserByNick(nickName).get(0);
            user.setActive(true);
            userServise.update(user);
            onlineClients.put(nickName,client);
            log.info("New Client added secess:" + nickName);
            return true;
        }else{
            log.info("New Client added failed:" + nickName);
            client.sendMessage(new ErrorMessage(SubType.GET, MessageType.AUTH,"Your Account is Online"));
            throw new  IllegalArgumentException(nickName +":" + "Accaunt is Oline");
        }
    }

    public boolean containsClient(String nick){
        return onlineClients.containsKey(nick);
    }

    public void removeClient(String nickName){
        User user = userServise.findUserByNick(nickName).get(0);
        user.setActive(false);
        userServise.update(user);
        onlineClients.remove(nickName);
        log.info(nickName + " deleted");
    }


}
