package services;

import criptography.CriptographyAlghorytm;
import messages.ErrorMessage;
import messages.enums.ErrorType;
import org.jboss.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import client.IClientThread;

public class AuthService implements IAuthService {

    private static final Logger logger = Logger.getLogger(AuthService.class);

    private static final String AUTH_REQUEST =
            "{" +
                    "\"type\":\"Auth\"" +
                    "\"subType\":\"Request\"" +
                    "}";

    private final IClientThread client;
    private final CriptoServise criptoServise;

    public AuthService(IClientThread client) {
        this.client = client;
        criptoServise = new CriptoServise(CriptographyAlghorytm.STRUMOCK);
    }

    @Override
    public boolean toAuthorize() {
        client.sendMessage(criptoServise.encrypt(AUTH_REQUEST));
        try {
            JSONObject authResponse= new JSONObject(criptoServise.decrypt(client.takeData()).getTextMessage());


            if (authResponse.getString("type").equals("Auth") &&
                    authResponse.getString("subType").equals("Response")) {

                String[] authData = authResponse.getString("body").split("_");

                if(authData.length!=2)
                    throw new IllegalArgumentException();

                ///TODO(1) finish auth method





            } else throw new IllegalArgumentException();
        } catch (JSONException e) {
            logger.error(e.getMessage());
            logger.error(e.getMessage());
            client.sendMessage(new ErrorMessage(ErrorType.SERVER));
            client.closeThread();
            return false;
        }


        return false;
    }
}
