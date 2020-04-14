import criptography.CriptographyAlghorytm;
import org.bouncycastle.util.encoders.Hex;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import services.сripto.CriptoServise;
import services.сripto.CriptoServiseImpl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthError extends Assert {

    private static final String OPEN_RESPONSE =
            "{" +
                    "\"type\":\"StreamMessage\"," +
                    "\"subType\":\"Response\"," +
                    "\"body\":\"Accepted\"" +
                    "}";

    private static final String OPEN_REQUEST =
            "{" +
                    "\"type\":\"StreamMessage\"," +
                    "\"subType\":\"Response\"," +
                    "\"body\":\"Open\"" +
                    "}";

    private static final String WRONG_OPEN_REQUEST =
            "{" +
                    "\"type\":\"StreamMessage\"," +
                    "\"subType\":\"Response\"," +
                    "\"body\":\"Oen\"" +
                    "}";

    private static final String AUTH_REQUEST =
            "{" +
                    "\"type\":\"Auth\"," +
                    "\"subType\":\"Request\"" +
                    "}";

    private static final String ERROR_AUTH_PROCESS=
            "{" +
                    "\"type\":\"ErrorMessage\"," +
                    "\"subType\":\"Response\"," +
                    "\"body\":\"AuthProcess\"" +
                    "}";

    private static final String ERROR_STREAM=
            "{" +
                    "\"type\":\"ErrorMessage\"," +
                    "\"subType\":\"Response\"," +
                    "\"body\":\"Stream\"" +
                    "}";

    private static final String ERROR_AUTH_DATA=
            "{" +
                    "\"type\":\"ErrorMessage\"," +
                    "\"subType\":\"Response\"," +
                    "\"body\":\"AuthData\"" +
                    "}";

    private static final String ERROR_CRIPTO=
            "{" +
                    "\"type\":\"ErrorMessage\"," +
                    "\"subType\":\"Response\"," +
                    "\"body\":\"Cripto\"" +
                    "}";

    static class test extends Thread {
        @Override
        public void run() {
            ServerStarter.main(new String[]{""});
        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }
    }

    private static final Auth.test t = new Auth.test();

    private Socket s;
    private DataOutputStream dos;
    private DataInputStream dis;
    private CriptoServise criptoServise ;


    @Before
    public void start() {
        t.start();

        try {
            s = new Socket("127.0.0.1", 5222);

            dos = new DataOutputStream(s.getOutputStream());
            dis = new DataInputStream(s.getInputStream());
            criptoServise = new CriptoServiseImpl(CriptographyAlghorytm.STRUMOCK);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void openStream() {
        try {

            dos.writeUTF(OPEN_REQUEST);
            JSONObject response = new JSONObject( dis.readUTF());

            assertEquals(response.toString(), new JSONObject(OPEN_RESPONSE).toString());

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }


    private void confirmCripto() {
        try {
            JSONObject request = new JSONObject(dis.readUTF());

            assertEquals( "CriptoStreamMessage", request.getString("type"));
            assertEquals("Request", request.getString("subType"));

            String randomText = request.getString("body");

            JSONObject response = new JSONObject();

            response.put("type","CriptoStreamMessage");
            response.put("subType","Response");
            response.put("body",getSHA256(criptoServise.decrypt(randomText)));

            dos.writeUTF(response.toString());

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }


    private void openCriptoStream(){
        openStream();

        confirmCripto();

        try {
            dos.writeUTF(criptoServise.encrypt(OPEN_REQUEST));
            JSONObject response = new JSONObject(criptoServise.decrypt(dis.readUTF()));

            assertEquals(response.toString(),new JSONObject(OPEN_RESPONSE).toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }



    private String getSHA256(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(
                    text.getBytes(StandardCharsets.UTF_8));
            return new String(Hex.encode(hash));
        } catch (NoSuchAlgorithmException e) {
            return " ";
        }
    }

    @Test
    public void failedOpenStream(){
        try {

            dos.writeUTF(WRONG_OPEN_REQUEST);
            JSONObject response = new JSONObject( dis.readUTF());

            assertEquals(response.toString(), new JSONObject(ERROR_STREAM).toString());

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void failedCriptoSream(){
        openStream();

        confirmWrongCripto();

        try {
            dos.writeUTF(criptoServise.encrypt(OPEN_REQUEST));
            JSONObject response = new JSONObject(criptoServise.decrypt(dis.readUTF()));

            assertEquals(response.toString(),new JSONObject(ERROR_CRIPTO).toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void confirmWrongCripto() {
        try {
            JSONObject request = new JSONObject(dis.readUTF());

            assertEquals( "CriptoStreamMessage", request.getString("type"));
            assertEquals("Request", request.getString("subType"));

            String randomText = request.getString("body");

            JSONObject response = new JSONObject();

            response.put("type","CriptoStreamMessage");
            response.put("subType","Response");
            response.put("body",criptoServise.decrypt(randomText));

            dos.writeUTF(response.toString());

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void failedAuth(){

        openCriptoStream();

        try {
            String request = criptoServise.decrypt(dis.readUTF());
            assertEquals(request,new JSONObject(AUTH_REQUEST).toString());

            JSONObject response = new JSONObject();
            response.put("type","Auth");
            response.put("subType","Response");
            response.put("body","user12" +"_"+ getSHA256("user1"));

            dos.writeUTF(criptoServise.encrypt(response.toString()));

            failedOpenCriptedAuthStream1();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void failedOpenCriptedAuthStream1() {
        try {

            dos.writeUTF(criptoServise.encrypt(OPEN_REQUEST));
            JSONObject response = new JSONObject( criptoServise.decrypt(dis.readUTF()));

            assertEquals(response.toString(), new JSONObject(ERROR_AUTH_DATA).toString());

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void failedAuthStream(){

        openCriptoStream();

        try {
            String request = criptoServise.decrypt(dis.readUTF());
            assertEquals(request,new JSONObject(AUTH_REQUEST).toString());

            JSONObject response = new JSONObject();
            response.put("type","Auth");
            response.put("subType","Response");
            response.put("body","user1" +"_"+ getSHA256("user1"));

            dos.writeUTF(criptoServise.encrypt(response.toString()));

            failedOpenCriptedAuthStream2();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void failedOpenCriptedAuthStream2() {
        try {

            dos.writeUTF(criptoServise.encrypt(WRONG_OPEN_REQUEST));
            JSONObject response = new JSONObject( criptoServise.decrypt(dis.readUTF()));

            assertEquals(response.toString(), new JSONObject(ERROR_STREAM).toString());

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @After
    public void stop() {
        try {
            t.interrupt();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }



}
