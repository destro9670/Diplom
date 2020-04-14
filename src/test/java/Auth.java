
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

public class Auth extends Assert {

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

    private static final String AUTH_REQUEST =
            "{" +
                    "\"type\":\"Auth\"," +
                    "\"subType\":\"Request\"" +
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

    private static final test t = new test();

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

    @Test
    public void openStream() {
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

    @Test
    public void openCriptoStream(){
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

    @Test
    public void auth(){

        openCriptoStream();

        try {
            String request = criptoServise.decrypt(dis.readUTF());
            assertEquals(request,new JSONObject(AUTH_REQUEST).toString());

            JSONObject response = new JSONObject();
            response.put("type","Auth");
            response.put("subType","Response");
            response.put("body","user1" +"_"+ getSHA256("user1"));

            dos.writeUTF(criptoServise.encrypt(response.toString()));

            openCriptedAuthStream();

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

    private void openCriptedAuthStream() {
        try {

            dos.writeUTF(criptoServise.encrypt(OPEN_REQUEST));
            JSONObject response = new JSONObject( criptoServise.decrypt(dis.readUTF()));

            assertEquals(response.toString(), new JSONObject(OPEN_RESPONSE).toString());

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
