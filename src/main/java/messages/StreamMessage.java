package messages;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class StreamMessage implements Message {

        private static final Logger logger = Logger.getLogger(StreamMessage.class);

        private JSONObject jsonMessage;

        /*
         *this method using for create system messages
         * witch takes from client
         */
        public StreamMessage (String jsonMessage) {
            try {
                this.jsonMessage = new JSONObject( jsonMessage);
            } catch (JSONException e) {
                logger.trace(e);
                throw new IllegalArgumentException("Wrong Json");
            }
        }

        @Override
        public String getTextMessage() {
            return jsonMessage.toString();
        }

    }