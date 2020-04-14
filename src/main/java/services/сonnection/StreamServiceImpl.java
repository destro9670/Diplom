package services.сonnection;


import client.ClientThread;
import criptography.CriptographyAlghorytm;
import messages.ErrorMessage;
import messages.StreamMessage;
import messages.enums.MessageType;
import messages.enums.SubType;
import org.bouncycastle.util.encoders.Hex;
import org.jboss.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import services.сripto.CriptoServiseImpl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StreamServiceImpl implements StreamService {

    private static final Logger logger = Logger.getLogger(StreamServiceImpl.class);

    private static final String OPEN_RESPONSE =
            "{" +
                    "\"Type\":\"Response\"," +
                    "\"SubType\":\"Connect\"," +
                    "\"MessageType\":\"Stream\"," +
                    "\"Status\":\"OK\"" +
                    "}";

    private static final String OPEN_REQUEST =
            "{" +
                    "\"Type\":\"Request\"," +
                    "\"SubType\":\"Connect\"," +
                    "\"MessageType\":\"Stream\"" +
                    "}";

    private static final String CRIPTO_TEST_MESSAGE_HEADER =
            "{" +
                    "\"Type\":\"Request\"," +
                    "\"SubType\":\"Get\"," +
                    "\"MessageType\":\"Stream\"}";

    private final ClientThread client;
    private final CriptoServiseImpl criptoServise;

    public StreamServiceImpl(ClientThread client) {
        this.client = client;
        criptoServise = new CriptoServiseImpl(CriptographyAlghorytm.STRUMOCK);

    }

    @Override
    public void open() {
        if (isValidOpenRequestMessage())
            client.sendMessage(new StreamMessage(OPEN_RESPONSE));
        else {
            client.sendMessage(new ErrorMessage(SubType.CONNECT,MessageType.STREAM,"Bad Open Request"));
            throw new IllegalArgumentException("Wrong Open Request");
        }
        if (isValidCriptoStream()) {
            openCriptoStream();
        } else {
            client.sendMessage(new ErrorMessage(SubType.GET,MessageType.STREAM,"Bad decription"));
            throw new IllegalArgumentException("Wrong Cripto Response");
        }
    }

    private boolean isValidOpenRequestMessage() {
        try {
            JSONObject request = new JSONObject(client.takeData().getTextMessage());
            JSONObject etalon = new JSONObject(OPEN_REQUEST);

            return request.toString().equals(etalon.toString());

        } catch (IllegalArgumentException | JSONException e) {
            logger.error(e.getMessage());
            client.sendMessage(new ErrorMessage(SubType.CONNECT, MessageType.STREAM,"Bad JSON"));
            client.closeThread();
        }

        return false;
    }

    private boolean isValidCriptoStream() {

        try {
            JSONObject request = new JSONObject(CRIPTO_TEST_MESSAGE_HEADER);
            String randomText = generateRandomText();
            request.put("Body", new JSONObject().put("Msg",criptoServise.encrypt(randomText)));

            client.sendMessage(new StreamMessage(request));

            JSONObject response = new JSONObject(client.takeData().getTextMessage());


            return isValidTestCriptoResponse(response, randomText);
        } catch (JSONException e) {
            logger.error(e.getMessage());
            client.sendMessage(new ErrorMessage(SubType.GET,MessageType.STREAM,"Bad JSON"));
            client.closeThread();
            return false;
        }


    }

    private String generateRandomText() {
        String alphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"  //generate random text
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(128);

        for (int i = 0; i < 128; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int) (alphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(alphaNumericString
                    .charAt(index));
        }
        return sb.toString();
    }

    private boolean isValidTestCriptoResponse(JSONObject testMessage, String sendedRandomText) {
        try {
            return testMessage.getString("Type").equals("Response") &&
                    testMessage.getString("SubType").equals("Get") &&
                    testMessage.getJSONObject("Body")
                            .getString("Msg").equals(getSHA256(sendedRandomText));

        } catch (JSONException e) {
            logger.error(e.getMessage());
            client.sendMessage(new ErrorMessage(SubType.GET,MessageType.STREAM,"Bad JSON"));
            client.closeThread();
            return false;
        }
    }

    private String getSHA256(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(
                    text.getBytes(StandardCharsets.UTF_8));
            return new String(Hex.encode(hash));
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
            client.sendMessage(new ErrorMessage(SubType.GET,MessageType.STREAM,"Bad JSON"));
            client.closeThread();
            return null;
        }
    }

    private void openCriptoStream() {
        try {
            if (!new JSONObject(OPEN_REQUEST).toString().equals(criptoServise.decrypt(client.takeData()).getTextMessage())){
                client.sendMessage(new ErrorMessage(SubType.CONNECT,MessageType.STREAM,"Wrong Open Request"));
                throw new IllegalArgumentException("Wrong Open Request");
        }
            client.sendMessage(new StreamMessage(criptoServise.encrypt(OPEN_RESPONSE)));

        } catch (JSONException e) {
            logger.error(e.getMessage());
            client.sendMessage(new ErrorMessage(SubType.GET,MessageType.STREAM,"Bad JSON"));
            client.closeThread();
            throw new IllegalArgumentException("Wrong Json");
        }
    }
}
