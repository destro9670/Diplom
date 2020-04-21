package services.сonnection;


import client.ClientThread;
import messages.ErrorMessage;
import messages.StreamMessage;
import messages.enums.MessageType;
import messages.enums.SubType;
import org.jboss.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class StreamServiceImpl implements StreamService {

    private static final Logger logger = Logger.getLogger(StreamServiceImpl.class);

    private static final String OPEN_RESPONSE =
            "{" +
                    "\"Type\":\"Response\"," +
                    "\"SubType\":\"Connect\"," +
                    "\"MessageType\":\"Stream\"," +
                    "\"Status\":\"OK\"" +
                    "}";

    private final ClientThread client;

    public StreamServiceImpl(ClientThread client) {
        this.client = client;
    }

    @Override
    public void open() {
        if (isValidOpenRequestMessage())
            client.sendMessage(new StreamMessage(OPEN_RESPONSE));
        else {
            client.sendMessage(new ErrorMessage(SubType.CONNECT,MessageType.STREAM,"Bad Open Request"));
            throw new IllegalArgumentException("Wrong Open Request");
        }

    }

    private boolean isValidOpenRequestMessage() {
        try {
            JSONObject request = new JSONObject(client.takeData().getTextMessage());

            return request.getString("Type").equals("Request")
                    && request.getString("SubType").equals("Connect")
                    && request.getString("MessageType").equals("Stream");

        } catch (IllegalArgumentException | JSONException | IOException e) {
            logger.error(e.getMessage());
            client.sendMessage(new ErrorMessage(SubType.CONNECT, MessageType.STREAM,"Bad JSON"));
            client.closeThread();
        }

        return false;
    }

}
