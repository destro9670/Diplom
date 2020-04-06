package ua.gabz.dm.entities.message;

import org.bouncycastle.util.encoders.Hex;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jsonb.JsonbAutoConfiguration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ua.gabz.dm.criptography.Cripography;
import ua.gabz.dm.criptography.Strumock;
import ua.gabz.dm.database.models.Users;
import ua.gabz.dm.database.repositories.UsersRepository;
import ua.gabz.dm.entities.enums.CriptoAlgoritm;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by destr on 15.03.2020.
 */
@Service
public class Stream implements Message {
    private Cripography cripography;

    @Autowired
    private UsersRepository users;

     public boolean validate(String msg){ //checking valid of OpenStream Request
     try{
         JSONObject obj = new JSONObject(msg);
         if (!"Stream Message".equals(obj.getString("type")))
            return false;

        if (!"Request".equals(obj.getString("subType")))
            return false;

        if (!"Open".equals(obj.getString("body")))
            return false;
        return true;
    } catch (JSONException e) {
        return false;
    }
    }

    public String getConfirmMessage(){  //creating OK msg
         JSONObject object = new JSONObject();
        try {
            object.put("type","Stream Message");
            object.put("subType","Response");
            object.put("body", "Accepted");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    public String getCriptoTestMessage(String criptedMessage, CriptoAlgoritm algoritm){ //cripted function
        JSONObject msg = new JSONObject();
        try{
            msg.put("type","CriptoStreamMessage");
            msg.put("subType", "Request");
            switch (algoritm){
                case AES:
                    //auth = AES;

                    msg.put("body",cripography.encript(criptedMessage.getBytes()));
                    msg.put("alghoritm","AES");
                    break;
                case Strumock:
                    cripography = new Strumock();

                    msg.put("body",new String(cripography.encript(criptedMessage.getBytes()),StandardCharsets.UTF_8));
                    msg.put("alghorytm","Strumock");
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return msg.toString();
    }

    public boolean validateCriptoStream(String hashMessage, String msg){    // chack correct cript by client
        String sha256hex = getSHA256(msg);
        try {
            JSONObject obj = new JSONObject(hashMessage);
            if (!obj.getString("type").equals("CriptoStreamMessage")) return false;
            if (!obj.getString("subType").equals("Response")) return false;
            if (obj.getString("body").isEmpty()) return false;
            else {


                if (obj.getString("body").equals(sha256hex))
                    return true;
                else {
                    return false;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }


    }

    public boolean validateCripto(String msg,CriptoAlgoritm algoritm){ // validate cripted openStream Request
        return validate(decriptText(msg,algoritm));

    }

    public String getConfirmCriptoMessage(CriptoAlgoritm algoritm){ /// create cripted OK msg
        String msg = new String(cripography.encript(getConfirmMessage().getBytes()),StandardCharsets.UTF_8);



       /* switch (algoritm){
            case Strumock:
                msg = new String(cripography.decript(msg.getBytes()),StandardCharsets.UTF_8);
                break;
            case AES:
                break;
        }*/

        return msg;
    }

    public String getAuthInfoRequest(CriptoAlgoritm algoritm){
        switch (algoritm){
            case Strumock:
                return new String(cripography.decript(getAuthMessage().getBytes()),StandardCharsets.UTF_8);
            case AES:
                break;
        }

        return "";
    }


    private String getAuthMessage(){
        JSONObject msg = new JSONObject();

        try {
            msg.put("type","Auth");
            msg.put("subType","Request");
        } catch (JSONException e) {
            msg = null;
        }

        return msg.toString();
    }

    public boolean validateAuth(String msg,CriptoAlgoritm algoritm) { // validate cripted openStream Request
         msg = decriptText(msg,algoritm);
            ///TODO(1) get AUTH param from DB

        try {
            JSONObject authMsg = new JSONObject(msg);
            System.out.println("auth in: " + msg);
            if(!authMsg.getString("type").equals("Auth")) return false;
            if(!authMsg.getString("subType").equals("Response")) return false;
            if(authMsg.getString("body").isEmpty()) return false;
            else{
                System.out.println("2");
                if(isValidAuthData(authMsg.getString("body"))){
                    return true;
                }else{
                    return false;
                }
            }
        } catch (JSONException | IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }

    }

    private boolean isValidAuthData(String data) {
        System.out.println("1");
        String[] parametrs = data.split(",");

        if (parametrs.length > 2)
            throw new IllegalArgumentException();

        System.out.println(Arrays.toString(parametrs));
        Users user = users.findUsersByLogin(parametrs[0]);


        if (user == null)
            return false;

        String passwd = user.getPassword();

        String passwdHexSHA256= "";

        passwdHexSHA256 =getSHA256(passwd);

        System.out.println("in: " + parametrs[1]);
        System.out.println("out: " + passwdHexSHA256);
        if(parametrs[1].equals(passwdHexSHA256))
            return true;
        else
            return false;
    }

    private String getSHA256(String text){
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hash = digest.digest(
                text.getBytes(StandardCharsets.UTF_8));
        return new String(Hex.encode(hash));
    }


    private String decriptText(String text, CriptoAlgoritm algoritm){
        switch (algoritm) {
            case Strumock:
                text = new String(cripography.decript(text.getBytes()), StandardCharsets.UTF_8);
                break;
            case AES:
                break;
        }
        return text;
    }



}
