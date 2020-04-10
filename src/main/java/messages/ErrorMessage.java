package messages;

import messages.enums.ErrorType;
import org.jboss.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class ErrorMessage implements Message {

    private static final Logger logger = Logger.getLogger(ErrorMessage.class);

    private static final String ERROR_HEADER =
                    "{" +
                    "\"type\":\"ErrorMessage\"," +
                    "\"subType\":\"Response\"" +
                    "}";

    private JSONObject message;

    public ErrorMessage(ErrorType type) {
        try {
            message = new JSONObject(ERROR_HEADER);

            if (type == ErrorType.AUTH_PROCESS) {
                message.put("body","AuthProcess");
            }
            if (type == ErrorType.STREAM) {
                message.put("body","Stream");
            }
            if (type == ErrorType.AUTH_DATA) {
                message.put("body","AuthData");
            }
            if (type == ErrorType.CRIPTO) {
                message.put("body","Cripto");
            }

        } catch (JSONException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public String getTextMessage() {
        return message.toString();
    }
}
