package messages;

import messages.enums.MessageType;
import messages.enums.SubType;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class ErrorMessage implements Message {

    private static final Logger logger = Logger.getLogger(ErrorMessage.class);



    private JSONObject message;

    public ErrorMessage( SubType subType, MessageType messageType, String description) {
        try {
            message = new JSONObject()
                    .put("Type","Response")
                    .put("Status","Failed")
                    .put("Body",new JSONObject()
                        .put("msg",description));
            logger.error(description);

            if (messageType == MessageType.AUTH) {
                message.put("MessageType","Auth");
            }
            if (messageType == MessageType.CLIENT) {
                message.put("MessageType","Client");
            }
            if (messageType == MessageType.STREAM) {
                message.put("MessageType","Stream");
            }
            if (messageType == MessageType.MESSAGE) {
                message.put("MessageType","Message");
            }
            if (messageType == MessageType.ROOM) {
                message.put("MessageType","Room");
            }

            if (subType == SubType.CONNECT) {
                message.put("SubType","Connect");
            }
            if (subType == SubType.GET) {
                message.put("SubType","Get");
            }
            if (subType == SubType.POST) {
                message.put("SubType","Post");
            }
            if (subType == SubType.PUT) {
                message.put("SubType","Put");
            }

        } catch (JSONException e) {
            logger.trace(e);
        }
    }

    @Override
    public String getTextMessage() {
        return message.toString();
    }


}
