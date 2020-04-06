package ua.gabz.dm.auth;

import org.jboss.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import ua.gabz.dm.criptography.Cripography;
import ua.gabz.dm.entities.enums.CriptoAlgoritm;
import ua.gabz.dm.entities.enums.Platform;
import ua.gabz.dm.entities.message.Error;
import ua.gabz.dm.entities.enums.ErrorTypes;
import ua.gabz.dm.entities.message.Stream;
import ua.gabz.dm.threads.client.ClientThread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by destr on 15.03.2020.
 */

@Service
public class AuthService implements Auth {

    private static final Logger logger = Logger.getLogger(AuthService.class);
    private Platform platform;
    private CriptoAlgoritm algoritm;
    private Stream stream;
    private Cripography cripography;


    public AuthService() {
        stream = new Stream();
    }

    @Override
    public boolean openStream(DataInputStream dis, DataOutputStream dos, ClientThread client) {


        try {
            String msg = dis.readUTF();
            if (stream.validate(msg))    ///check valid of input openStream Request
                client.send(stream.getConfirmMessage());  ///send OK msg
            else {
                client.send(Error.getMessage(ErrorTypes.STREAM)); /// if input msg not valid close session
                client.close();
            }
            return true;
        } catch (IOException e) {
            client.send(Error.getMessage(ErrorTypes.STREAM));
            client.close();
            return false;
        }


    }

    @Override
    public boolean openCriptoStream(DataInputStream dis, DataOutputStream dos, ClientThread client) {
        try {
            String msg = dis.readUTF();
            if (!stream.validateCripto(msg, algoritm)) {///check valid of start cripting Response

                client.send(Error.getMessage(ErrorTypes.STREAM)); // if false close session
                client.close();
            }
            return true;
        } catch (IOException e) {
            client.send(Error.getMessage(ErrorTypes.STREAM));  // if exception close session
            client.close();
            return false;
        }

    }


    @Override
    public boolean cripto(DataInputStream dis, DataOutputStream dos, ClientThread client) {

        try {
            String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"  //generate random text
                    + "0123456789"
                    + "abcdefghijklmnopqrstuvxyz";

            // create StringBuffer size of AlphaNumericString
            StringBuilder sb = new StringBuilder(15);

            for (int i = 0; i < 15; i++) {

                // generate a random number between
                // 0 to AlphaNumericString variable length
                int index
                        = (int) (AlphaNumericString.length()
                        * Math.random());

                // add Character one by one in end of sb
                sb.append(AlphaNumericString
                        .charAt(index));
            }

            logger.info(sb.toString());


            dos.writeUTF(stream.getCriptoTestMessage(sb.toString(), algoritm)); //send to client encripted random next
            String confirmMassage = dis.readUTF();

            if (stream.validateCriptoStream(confirmMassage, sb.toString()))   //check client correct decripting
                dos.writeUTF(stream.getConfirmCriptoMessage(algoritm));     // if true send submit msg to client
            else {
                client.send(Error.getMessage(ErrorTypes.CRIPTO)); //if false close session
                client.close();
                return false;
            }

        } catch (Exception e) {
            client.send(Error.getMessage(ErrorTypes.CRIPTO));
            client.close();
        }


        return true;
    }

    @Override
    public boolean initClient() {
        return false;
    }       //??

    @Override
    public boolean getClientParametrs(DataInputStream dis, DataOutputStream dos, ClientThread client) {
        try {
            String clientParams = dis.readUTF();        // get client device info
            JSONObject msg = new JSONObject(clientParams);  // start chack valid of input text
            if (!msg.get("type").equals("DeviceParam")) throw new RuntimeException();
            if (!msg.get("subType").equals("info")) throw new RuntimeException();
            if (!(msg.get("Device").equals("Desktop") || msg.get("Device").equals("Mobile") || msg.get("Device").equals("Web")))
                throw new RuntimeException();
            else {
                switch (msg.getString("Device")) { // remember type of device
                    case "Desktop":
                        platform = Platform.Desktop;
                        break;
                    case "Mobile":
                        platform = Platform.Mobile;
                        break;
                    case "Web":
                        platform = Platform.Web;
                        break;
                }
            }
            if (!(msg.get("Cripto").equals("Strumock")/*||msg.get("Cripto").equals("AES")*/))
                throw new RuntimeException();
            else {   //remember submited cripto algoritms
                switch (msg.getString("Cripto")) {
                    case "Strumock":
                        algoritm = CriptoAlgoritm.Strumock;
                        break;
                }
            }


        } catch (IOException | JSONException | RuntimeException e) {
            client.send(Error.getMessage(ErrorTypes.STREAM)); //if error close session
            client.close();
            return false;
        }
        return true;
    }


    public Platform getPlatform() {
        return platform;
    }      //getters

    public CriptoAlgoritm getAlgoritm() {
        return algoritm;
    }

    @Override
    public boolean toAutorize(DataInputStream dis, DataOutputStream dos, ClientThread client) {
        try {
            dos.writeUTF(stream.getAuthInfoRequest(algoritm)); //send auth request
            if (stream.validateAuth(dis.readUTF(), algoritm))
                return true;
            else
                return false;

        } catch (IOException e) {
            client.send(Error.getMessage(ErrorTypes.AUTH)); //if error close session
            client.close();
            return false;
        }
    }
}
