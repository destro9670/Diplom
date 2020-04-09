package messages;

import org.jboss.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class ClientMessage implements Message {

    private static final Logger logger = Logger.getLogger(ClientMessage.class);
    private JSONObject jsonMessage;

    ///TODO(1) finish class

    public ClientMessage(String jsonMessage) {
        try {
            this.jsonMessage = new JSONObject( jsonMessage);
        } catch (JSONException e) {
            logger.error(e.getMessage());
            throw new IllegalArgumentException();
        }
    }

    public ClientMessage(JSONObject jsonMessage){
        this.jsonMessage = jsonMessage;
    }

    @Override
    public String getTextMessage() {
        return jsonMessage.toString();
    }
}
