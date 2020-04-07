package messages;

import org.jboss.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class Message implements IMessage {

    private static final Logger logger = Logger.getLogger(Message.class);
    private JSONObject jsonMessage;

    ///TODO(1) finish class

    public Message(String jsonMessage) {
        try {
            this.jsonMessage = new JSONObject( jsonMessage);
        } catch (JSONException e) {
            logger.error(e.getMessage());
            throw new IllegalArgumentException();
        }
    }

    public Message(JSONObject jsonMessage){
        this.jsonMessage = jsonMessage;
    }

    @Override
    public String getTextMessage() {
        return jsonMessage.toString();
    }
}
