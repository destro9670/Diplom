package services;

import client.ClientThread;
import criptography.CriptographyAlghorytm;
import db.models.User;
import messages.ErrorMessage;
import messages.ClientMessage;
import messages.enums.ErrorType;
import org.bouncycastle.util.encoders.Hex;
import org.jboss.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class AuthServiceImpl implements AuthService {

    private static final Logger logger = Logger.getLogger(AuthServiceImpl.class);

    private static final String AUTH_REQUEST =
            "{" +
                    "\"type\":\"Auth\"," +
                    "\"subType\":\"Request\"" +
                    "}";

    private final ClientThread client;
    private final CriptoServise criptoServise;
    private final UserServise userServise;

    public AuthServiceImpl(ClientThread client) {
        this.client = client;
        criptoServise = new CriptoServiseImpl(CriptographyAlghorytm.STRUMOCK);
        userServise = new UserServise();
    }

    @Override
    public void toAuthorize() {
        client.sendMessage(new ClientMessage(criptoServise.encrypt(AUTH_REQUEST)));
        try {
            JSONObject authResponse = new JSONObject(criptoServise.decrypt(client.takeData()).getTextMessage());

            if (!(authResponse.getString("type").equals("Auth") &&
                    authResponse.getString("subType").equals("Response")))
                throw new IllegalArgumentException();

            String[] authData = authResponse.getString("body").split("_");

            if (authData.length != 2)
                throw new IllegalArgumentException();

            List<User> users = userServise.findUserByLogin(authData[0]);

            if (users.isEmpty())
                throw new IllegalArgumentException();

            if (users.size() != 1)
                throw new IllegalArgumentException();

            User user = users.get(0);

            if (!confirmPassword(user, authData[1]))
                throw new IllegalArgumentException();

            openStream();

            ///TODO(1) create active user map(servise)


        } catch (JSONException e) {
            logger.error(e.getMessage());
            logger.error(e.getMessage());
            client.sendMessage(new ErrorMessage(ErrorType.SERVER));
            client.closeThread();
        }


    }

    private boolean confirmPassword(User user, String sendedPassword) {
        return getSHA256(user.getPassword()).equals(sendedPassword);
    }

    private String getSHA256(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(
                    text.getBytes(StandardCharsets.UTF_8));
            return new String(Hex.encode(hash));
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
            client.sendMessage(new ErrorMessage(ErrorType.SERVER));
            client.closeThread();
            return "";
        }
    }

    private void openStream() {
        String openResponse =
                "{" +
                        "\"type\":\"StreamMessage\"," +
                        "\"subType\":\"Response\"," +
                        "\"body\":\"Accepted\"" +
                        "}";

        String openRequest =
                "{" +
                        "\"type\":\"StreamMessage\"," +
                        "\"subType\":\"Response\"," +
                        "\"body\":\"Open\"" +
                        "}";

        String request =criptoServise.decrypt(client.takeData().getTextMessage());

        try {
            if (!request.equals(new JSONObject(openRequest).toString())) {
                throw new IllegalArgumentException();
            }
        } catch (JSONException e) {
            logger.error(e.getMessage());
            throw new IllegalArgumentException();
        }

        client.sendMessage(new ClientMessage(criptoServise.encrypt(openResponse)));

    }

}
