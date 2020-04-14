package messages;

import org.jboss.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class ClientMessage implements Message {

    private static final Logger logger = Logger.getLogger(ClientMessage.class);


    private JSONObject jsonMessage;

    /*
    *this method using for create system messages
    * witch takes from client
     */
    public ClientMessage(String jsonMessage) {
        try {
            this.jsonMessage = new JSONObject( jsonMessage);
        } catch (JSONException e) {
            logger.error(e.getMessage());
            throw new IllegalArgumentException("Wrong Json");
        }
    }


    /*
     *this method using for create system messages
     * witch sends to client
     */
    public ClientMessage(JSONObject jsonMessage){
        this.jsonMessage = jsonMessage;
    }

    @Override
    public String getTextMessage() {
        return jsonMessage.toString();
    }

    @Override
    public String getBodyMessage() {
        try {
            return jsonMessage.getString("body");
        } catch (JSONException e) {
            logger.error(e.getMessage());
            throw new IllegalArgumentException("Wrong Json");
        }
    }

}
