package services;


import client.IClientThread;
import criptography.CriptographyAlghorytm;
import messages.ErrorMessage;
import messages.Message;
import messages.enums.ErrorType;
import org.bouncycastle.util.encoders.Hex;
import org.jboss.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Stream implements IStream {

    private static final Logger logger = Logger.getLogger(Stream.class);

    private static final String OPEN_RESPONSE =
            "{" +
                    "\"type\":\"StreamMessage\"" +
                    "\"subType\":\"Response\"" +
                    "\"body\":\"Accepted\"" +
                    "}";

    private static final String OPEN_REQUEST =
            "{" +
                    "\"type\":\"StreamMessage\"" +
                    "\"subType\":\"Response\"" +
                    "\"body\":\"Accepted\"" +
                    "}";

    private static final String CRIPTO_TEST_MESSAGE_HEADER=
            "{" +
                    "\"type\":\"CriptoStreamMessage\"" +
                    "\"subType\":\"Request\"" +
                    "}";

    private final IClientThread client;
    private final CriptoServise criptoServise;

    public Stream(IClientThread client) {
        this.client = client;
        criptoServise = new CriptoServise(CriptographyAlghorytm.STRUMOCK);

    }

    @Override
    public void open() {
        if (isValidOpenRequestMessage())
            client.sendMessage(new Message(OPEN_RESPONSE));
        else
            throw new IllegalArgumentException();

        if(isValidCriptoStream()){
            openCriptoStream();
        }else
            throw new IllegalArgumentException();

    }

    private boolean isValidOpenRequestMessage() {
        try {
            String request = client.takeData().getTextMessage();

            return request.equals(OPEN_REQUEST);

        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            client.sendMessage(new ErrorMessage(ErrorType.STREAM));
            client.closeThread();
        }

        return false;
    }

    private boolean isValidCriptoStream(){

        try {
            JSONObject testMessage = new JSONObject(CRIPTO_TEST_MESSAGE_HEADER);
            String randomText = generateRandomText();
            testMessage.put("body",criptoServise.encrypt(randomText));

            client.sendMessage(new Message(testMessage));
            return isValidTestCriptoResponse(new JSONObject(client.takeData().getTextMessage()), randomText);
        } catch (JSONException e) {
            logger.error(e.getMessage());
            client.sendMessage(new ErrorMessage(ErrorType.SERVER));
            client.closeThread();
            return false;
        }


    }

    private String generateRandomText(){
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

    private boolean isValidTestCriptoResponse(JSONObject testMessage, String sendedRandomText){
        try {
            return testMessage.getString("type").equals("CriptoStreamMessage") &&
                    testMessage.getString("subType").equals("Response") &&
                    testMessage.getString("body").equals(getSHA256(sendedRandomText));

        } catch (JSONException e) {
            logger.error(e.getMessage());
            client.sendMessage(new ErrorMessage(ErrorType.SERVER));
            client.closeThread();
            return false;
        }
    }

    private String getSHA256(String text){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(
                    text.getBytes(StandardCharsets.UTF_8));
            return new String(Hex.encode(hash));
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
            client.sendMessage(new ErrorMessage(ErrorType.SERVER));
            client.closeThread();
            return null;
        }
    }

    private void openCriptoStream(){
        if(!OPEN_REQUEST.equals(criptoServise.decrypt(client.takeData()).getTextMessage())) throw new IllegalArgumentException();
        client.sendMessage(criptoServise.encrypt(OPEN_REQUEST));
    }
}
