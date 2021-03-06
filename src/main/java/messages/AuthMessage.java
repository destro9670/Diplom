package messages;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class AuthMessage implements Message {
    private static final Logger logger = Logger.getLogger(AuthMessage.class);

    private JSONObject jsonMessage;

    /*
     *this method using for create system messages
     * witch takes from client
     */
    public AuthMessage(String jsonMessage) {
        try {
            this.jsonMessage = new JSONObject( jsonMessage);
        } catch (JSONException e) {
            logger.trace(e);
            throw new IllegalArgumentException("Wrong Json");
        }
    }


    /*
     *this method using for create system messages
     * witch sends to client
     */
    public AuthMessage (JSONObject jsonMessage){
        this.jsonMessage = jsonMessage;
    }

    @Override
    public String getTextMessage() {
        return jsonMessage.toString();
    }

}
