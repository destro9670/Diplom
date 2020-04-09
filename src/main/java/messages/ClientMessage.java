package messages;

import org.jboss.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class ClientMessage implements Message {

    private static final Logger logger = Logger.getLogger(ClientMessage.class);

    private static final String FROM = "from";
    private static final String TO = "to";
    private static final String BODY = "body";
    private static final String TYPE = "type";
    private static final String SUBTYPE = "subType";

    private String from;
    private String to;
    private String body;
    private String type;
    private String subType;

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
            throw new IllegalArgumentException();
        }
    }


    /*
     *this method using for create system messages
     * witch sends to client
     */
    public ClientMessage(JSONObject jsonMessage){
        this.jsonMessage = jsonMessage;
    }

    /*
     *this method using for create messages
     * witch takes from client1 to client2
     */
    public ClientMessage(String from, String to, String body, String type, String subType) {
        this.from = from;
        this.to = to;
        this.body = body;
        this.type = type;
        this.subType = subType;
        jsonMessage = new JSONObject();
        try {
            jsonMessage.put(TYPE,type);
            jsonMessage.put(SUBTYPE,subType);
            jsonMessage.put(FROM,from);
            jsonMessage.put(TO,to);
            jsonMessage.put(BODY,body);
        } catch (JSONException e) {
            logger.error(e.getMessage());
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String getTextMessage() {
        return jsonMessage.toString();
    }

    public String getSender() {
        return from;
    }

    public String getTaker() {
        return to;
    }

    public String getBody() {
        return body;
    }

}
