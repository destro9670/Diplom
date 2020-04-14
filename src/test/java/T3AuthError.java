import criptography.CriptographyAlghorytm;
import org.bouncycastle.util.encoders.Hex;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.*;
import services.сripto.CriptoServise;
import services.сripto.CriptoServiseImpl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class T3AuthError extends Assert {

    private static final String OPEN_REQUEST=
            "{" +
                    "\"Type\":\"Request\"," +
                    "\"SubType\":\"Connect\"," +
                    "\"MessageType\":\"Stream\"" +
                    "}";

    private static final String OPEN_RESPONSE  =
            "{" +
                    "\"Type\":\"Response\"," +
                    "\"SubType\":\"Connect\"," +
                    "\"MessageType\":\"Stream\"," +
                    "\"Status\":\"OK\"" +
                    "}";

    private static final String AUTH_RESPONSE =
            "{" +
                    "\"Type\":\"Response\"," +
                    "\"SubType\":\"Get\"," +
                    "\"MessageType\":\"Auth\"" +
                    "}";

    private static final String WRONG_OPEN_REQUEST=
            "{" +
                    "\"Type\":\"Reqquest\"," +
                    "\"SubType\":\"Connect\"," +
                    "\"MessageType\":\"Stream\"" +
                    "}";

    private static final String WRONG_AUTH_REQUEST =
            "{" +
                    "\"Type\":\"Reqquest\"," +
                    "\"SubType\":\"Get\"," +
                    "\"MessageType\":\"Auth\"" +
                    "}";

    private static final String BAD_OPEN_REQUEST =
            "{" +
                    "\"Status\":\"Failed\"," +
                    "\"Type\":\"Response\"," +
                    "\"SubType\":\"Connect\"," +
                    "\"Body\":{" +
                    "\"msg\":\"Bad Open Request\"" +
                    "},\"MessageType\":\"Stream\"" +
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


    @BeforeClass
    public static void start() {
        t.start();
        try {
            Thread.sleep(15*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    private void openStream() {

        try {
            s = new Socket("127.0.0.1", 5222);

            dos = new DataOutputStream(s.getOutputStream());
            dis = new DataInputStream(s.getInputStream());
            criptoServise = new CriptoServiseImpl(CriptographyAlghorytm.STRUMOCK);

        } catch (IOException e) {
            e.printStackTrace();
        }



        try {

            dos.writeUTF(OPEN_REQUEST);
            JSONObject response = new JSONObject( dis.readUTF());

            assertEquals(response.toString(), new JSONObject(OPEN_RESPONSE).toString());

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void wrongOpenStream() {

        try {
            s = new Socket("127.0.0.1", 5222);

            dos = new DataOutputStream(s.getOutputStream());
            dis = new DataInputStream(s.getInputStream());
            criptoServise = new CriptoServiseImpl(CriptographyAlghorytm.STRUMOCK);

        } catch (IOException e) {
            e.printStackTrace();
        }


        try {

            dos.writeUTF(WRONG_OPEN_REQUEST);
            JSONObject response = new JSONObject( dis.readUTF());

            assertEquals(response.toString(), new JSONObject(BAD_OPEN_REQUEST).toString());

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }


    private void confirmCripto() {
        try {
            JSONObject request = new JSONObject(dis.readUTF());

            assertEquals( "Request", request.getString("Type"));
            assertEquals("Get", request.getString("SubType"));
            assertEquals("Stream", request.getString("MessageType"));

            String randomText = request.getJSONObject("Body").getString("Msg");

            JSONObject response = new JSONObject();

            response.put("Type","Response");
            response.put("SubType","Get");
            response.put("MessageType","Stream");
            response.put("Body",new JSONObject()
                    .put("Msg",getSHA256(criptoServise.decrypt(randomText))));

            dos.writeUTF(response.toString());

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void wrongConfirmCripto() {
        try {
            JSONObject request = new JSONObject(dis.readUTF());

            assertEquals( "Request", request.getString("Type"));
            assertEquals("Get", request.getString("SubType"));
            assertEquals("Stream", request.getString("MessageType"));

            String randomText = request.getJSONObject("Body").getString("Msg");

            JSONObject response = new JSONObject();

            response.put("Type","Response");
            response.put("SubType","Get");
            response.put("MessageType","Stream");
            response.put("Body",new JSONObject()
                    .put("Msg",criptoServise.decrypt(randomText)));

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

    @Test
    public void wrongOpenCriptoStream(){
        openStream();

        wrongConfirmCripto();

        try {
            dos.writeUTF(criptoServise.encrypt(OPEN_REQUEST));
            JSONObject response = new JSONObject(criptoServise.decrypt(dis.readUTF()));

            assertEquals(response.toString(),new JSONObject("{\"Status\":\"Failed\",\"Type\":\"Response\",\"SubType\":\"Get\",\"Body\":{\"msg\":\"Bad decription\"},\"MessageType\":\"Stream\"}").toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void wrongAuth1(){

        openCriptoStream();

        try {
            /*String request = criptoServise.decrypt(dis.readUTF());
            assertEquals(request,new JSONObject(AUTH_REQUEST).toString());
            */
            JSONObject request = new JSONObject(AUTH_RESPONSE);
            request.put("Body",new JSONObject()
                    .put("Login","user11")
                    .put("Password", getSHA256("user1")));

            dos.writeUTF(criptoServise.encrypt(request.toString()));

            JSONObject response = new JSONObject(criptoServise.decrypt(dis.readUTF()));

            assertEquals("Response",response.getString("Type"));
            assertEquals("Get",response.getString("SubType"));
            assertEquals("Auth",response.getString("MessageType"));
            assertEquals("Failed",response.getString("Status"));
            assertEquals("Bad Password or Login",response.getJSONObject("Body").getString("Description"));

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void wrongAuth2(){

        openCriptoStream();

        try {
            /*String request = criptoServise.decrypt(dis.readUTF());
            assertEquals(request,new JSONObject(AUTH_REQUEST).toString());
            */
            JSONObject request = new JSONObject(AUTH_RESPONSE);
            request.put("Body",new JSONObject()
                    .put("Login","user1")
                    .put("Password", getSHA256("user11")));

            dos.writeUTF(criptoServise.encrypt(request.toString()));

            JSONObject response = new JSONObject(criptoServise.decrypt(dis.readUTF()));

            assertEquals("Response",response.getString("Type"));
            assertEquals("Get",response.getString("SubType"));
            assertEquals("Auth",response.getString("MessageType"));
            assertEquals("Failed",response.getString("Status"));
            assertEquals("Bad Login or Password",response.getJSONObject("Body").getString("Description"));

            openCriptedAuthStream();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void wrongAuth3(){

        openCriptoStream();

        try {
            /*String request = criptoServise.decrypt(dis.readUTF());
            assertEquals(request,new JSONObject(AUTH_REQUEST).toString());
            */
            JSONObject request = new JSONObject(AUTH_RESPONSE);
            request.put("Body",new JSONObject()
                    .put("Login","user1")
                    .put("Password", getSHA256("user1")));

            dos.writeUTF(criptoServise.encrypt(request.toString()));

            JSONObject response = new JSONObject(criptoServise.decrypt(dis.readUTF()));

            assertEquals("Response",response.getString("Type"));
            assertEquals("Get",response.getString("SubType"));
            assertEquals("Auth",response.getString("MessageType"));
            assertEquals("OK",response.getString("Status"));
            assertEquals("user1",response.getJSONObject("Body").getString("Nick"));

            openWrongCriptedAuthStream();

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

    private void openWrongCriptedAuthStream() {
        try {

            dos.writeUTF(criptoServise.encrypt(WRONG_OPEN_REQUEST));
            JSONObject response = new JSONObject( criptoServise.decrypt(dis.readUTF()));

            assertEquals(response.toString(), new JSONObject("Expected :{\"Status\":\"Failed\",\"Type\":\"Response\",\"SubType\":\"Connect\",\"Body\":{\"msg\":\"Bad Open Request\"},\"MessageType\":\"Stream\"}\n").toString());

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }


    @AfterClass
    public static void stop() {
        try {
            t.interrupt();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
