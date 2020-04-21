package services.сonnection;

import client.ClientThread;
import db.models.User;
import messages.AuthMessage;
import messages.ErrorMessage;
import messages.enums.MessageType;
import messages.enums.SubType;
import org.bouncycastle.util.encoders.Hex;
import org.jboss.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import services.datadase.UserServise;
import utiles.ClientHolderUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class AuthServiceImpl implements AuthService {

    private static final Logger logger = Logger.getLogger(AuthServiceImpl.class);

    private static final String AUTH_RESPONSE=
            "{" +
                    "\"Type\":\"Response\"," +
                    "\"SubType\":\"Get\"," +
                    "\"MessageType\":\"Auth\"," +
                    "\"Status\":\"OK\"" +
                    "}";

    private final ClientThread client;
    private final UserServise userServise;

    public AuthServiceImpl(ClientThread client) {
        this.client = client;
        userServise = new UserServise();
    }

    @Override
    public User toAuthorize() {
        try {
            JSONObject authRequest = new JSONObject(client.takeData().getTextMessage());

            if (!(authRequest.getString("Type").equals("Request") &&
                    authRequest.getString("SubType").equals("Get") &&
                    authRequest.getString("MessageType").equals("Auth"))) {

                client.sendMessage(new ErrorMessage(SubType.GET,MessageType.AUTH,"Bad JSON"));

                throw new IllegalArgumentException("Wrong Json");
            }

            JSONObject authData = authRequest.getJSONObject("Body");

            String login = authData.getString("Login");
            String password= authData.getString("Password");
            logger.error( "Password: " + password + "\n" + "Login: " + login);

            List<User> users = userServise.findUserByLogin(login);

            if (users.isEmpty()) {
                client.sendMessage(new ErrorMessage(SubType.GET,MessageType.AUTH,"Bad Login or Password"));

                throw new IllegalArgumentException("Wrong Login or Password");
            }
            if (users.size() != 1) {
                logger.error("multiple users with the same usernames");
                client.sendMessage(new ErrorMessage(SubType.GET,MessageType.AUTH,"Bad Auth"));
                throw new IllegalArgumentException("Wrong Auth");
            }

            User user = users.get(0);

            if (!confirmPassword(user, password)) {
                client.sendMessage(new ErrorMessage(SubType.GET,MessageType.AUTH,"Bad Login or Password"));
                throw new IllegalArgumentException("Wrong Login or Password");
            }
            JSONObject authResponse = new JSONObject(AUTH_RESPONSE);
            authResponse.put("Body",new JSONObject().put("Nick",user.getNick()));

            client.sendMessage(new AuthMessage(authResponse));

            openStream();

            if (ClientHolderUtil.getInstance().contains(user.getNick())){
                ClientThread clientThread = ClientHolderUtil.getInstance().getOnlineClient(user.getNick());
                ///client.sendMessage(new StreamMessage()); message for disconnect
                clientThread.closeThread();
                logger.error(user.getNick() + " try to sing in twice");
                ///TODO(1) allert to admin
            }

            ClientHolderUtil.getInstance().addNewOnlineClient(user.getNick(),client);

            return user;


        } catch (JSONException | IOException e) {
            logger.error(e.getMessage());
            client.sendMessage(new ErrorMessage(SubType.GET, MessageType.AUTH,"Bad JSON"));
            client.closeThread();
        }

        //newer returned
        return null;

    }

    private boolean confirmPassword(User user, String sendedPassword) {
        String hashPassword = getSHA256(user.getPassword());

        return hashPassword.equals(sendedPassword);
    }

    private String getSHA256(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(
                    text.getBytes(StandardCharsets.UTF_8));
            return new String(Hex.encode(hash));
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
            client.sendMessage(new ErrorMessage(SubType.GET,MessageType.AUTH,"Bad JSON"));
            client.closeThread();
            return "";
        }
    }

    private void openStream() {
         String openResponse=
                "{" +
                        "\"Type\":\"Response\"," +
                        "\"SubType\":\"Connect\"," +
                        "\"MessageType\":\"Stream\"," +
                        "\"Status\":\"OK\"" +
                        "}";

        String openRequest=
                "{" +
                        "\"Type\":\"Request\"," +
                        "\"SubType\":\"Connect\"," +
                        "\"MessageType\":\"Stream\"" +
                        "}";



        try {
            String request =client.takeData().getTextMessage();

            if (!request.equals(new JSONObject(openRequest).toString())) {
                client.sendMessage(new ErrorMessage(SubType.CONNECT,MessageType.STREAM,"Bad Open Request"));

                throw new IllegalArgumentException("Wrong Open Request");
            }
        } catch (JSONException | IOException e) {
            logger.error(e.getMessage());
            client.sendMessage(new ErrorMessage(SubType.CONNECT,MessageType.STREAM,"Bad JSON"));
            throw new IllegalArgumentException("Wrong Json");
        }

        client.sendMessage(new AuthMessage(openResponse));

    }

}