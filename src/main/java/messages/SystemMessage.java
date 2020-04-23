package messages;

import messages.enums.MessageStatus;
import messages.enums.MessageType;
import messages.enums.SubType;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class SystemMessage implements Message {

    private static final Logger logger = Logger.getLogger(ErrorMessage.class);

    private static final String HEADER =
            "{" +
                    "\"Type\":\"Response\"" +
                    "}";

    private JSONObject message;

    public SystemMessage(SubType subType, MessageType messageType, MessageStatus status, JSONObject body) {
        try {
            message = new JSONObject(HEADER);

            message.put("Body",body);

            if (messageType == MessageType.AUTH) {
                message.put("MessageType", "Auth");
            }
            if (messageType == MessageType.CLIENT) {
                message.put("MessageType", "Client");
            }
            if (messageType == MessageType.STREAM) {
                message.put("MessageType", "Stream");
            }
            if (messageType == MessageType.MESSAGE) {
                message.put("MessageType", "Message");
            }
            if (messageType == MessageType.ROOM) {
                message.put("MessageType", "Room");
            }

            if (subType == SubType.CONNECT) {
                message.put("SubType", "Connect");
            }
            if (subType == SubType.GET) {
                message.put("SubType", "Get");
            }
            if (subType == SubType.POST) {
                message.put("SubType", "Post");
            }
            if (subType == SubType.PUT) {
                message.put("SubType", "Put");
            }

            if (status == MessageStatus.OK) {
                message.put("Status", "OK");
            }

            if (status == MessageStatus.FAILED) {
                message.put("Status", "Failed");
            }

            if (status == MessageStatus.NULL) {
                //nothing to put
            }

        } catch (JSONException e) {
            logger.trace(e);
        }
    }

    @Override
    public String getTextMessage() {
        return message.toString();
    }

    @Override
    public String getBodyMessage() {
        try {
            return message.getJSONObject("Body").getString("Msg");
        } catch (JSONException e) {
            logger.trace(e);
            throw new IllegalArgumentException("Wrong Json");
        }
    }
}
