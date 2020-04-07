package messages;

import messages.enums.ErrorType;
import org.jboss.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class ErrorMessage implements IMessage {

    private static final Logger logger = Logger.getLogger(ErrorMessage.class);

    private static final String ERROR_HEADER =
                    "{" +
                    "\"type\":\"ErrorMessage\"" +
                    "\"subType\":\"Response\"" +
                    "}";

    private JSONObject errorMessage;

    public ErrorMessage(ErrorType type) {
        try {
            errorMessage = new JSONObject(ERROR_HEADER);

            if (type == ErrorType.AUTH) {
                errorMessage.put("body","Auth");
            }

        } catch (JSONException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public String getTextMessage() {
        return errorMessage.toString();
    }
}
