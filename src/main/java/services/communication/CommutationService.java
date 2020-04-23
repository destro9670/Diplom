package services.communication;

import client.ClientThread;
import db.models.Content;
import db.models.Room;
import db.models.User;
import messages.ClientMessage;
import messages.Message;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import services.datadase.MessageServise;
import services.datadase.RoomServise;
import services.datadase.UserServise;
import utiles.ClientHolderUtil;

class CommutationService {

    private final static Logger logger = Logger.getLogger(CommutationService.class);
    private final RoomServise roomServise;
    private final MessageServise messageServise;


    CommutationService() {
        roomServise = new RoomServise();
        messageServise = new MessageServise();

    }

    void sendSystemMessageToUser(User taker, User sender, Message message,boolean isSaved) {
        ClientThread takerThread = ClientHolderUtil.getInstance().getOnlineClient(taker.getNick());
        Room room = roomServise.findRoomByName("system_" + taker.getNick()).get(0);

        Content content = new Content(message.getTextMessage());


        db.models.Message msg = new db.models.Message(sender, taker, false, false, " ", room);

        if(!isSaved) {
            messageServise.save(msg, content);
            logger.info("new Message saved");
        }
        if (takerThread != null) {

            takerThread.sendMessage(message);

            msg.setReadStatus(true);
            msg.setSendStatus(true);
            messageServise.update(msg);

        }


    }

    void sendClientMessageToUser(User taker, db.models.Message message, Content content, boolean saved) {

        ClientThread takerThread = ClientHolderUtil.getInstance().getOnlineClient(taker.getNick());

        if (!saved) {
            messageServise.save(message, content);
            logger.info("new Message saved");
        }
        if (takerThread != null) {
            try {
                JSONObject body = new JSONObject(message.toString());
                JSONObject msg = new JSONObject().put("Type", "Request")
                        .put("SubType", "Put")
                        .put("MessageType", "Message")
                        .put("Body", body);
                Message clientMessage = new ClientMessage(msg.toString());

                takerThread.sendMessage(clientMessage);

                message.setReadStatus(takerThread.getInRoom().equals(message.getRoom().getName()));
                message.setSendStatus(true);
                messageServise.update(message);


            } catch (JSONException e) {
                logger.trace(e);
            }
        }

    }


}
