package StreamIOTest.OK;

import org.bouncycastle.util.encoders.Hex;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ua.gabz.dm.ServerStarter;
import newServer.criptography.Cripography;
import newServer.criptography.Strumock;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Auth extends Assert {


    static class test extends Thread{
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

    @Before
    public void start(){

        t.start();
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIO(){
        try {
            Socket s = new Socket("127.0.0.1",5222);
            JSONObject obj = new JSONObject();
            obj.put("type", "Stream Message");
            obj.put("subType", "Request");
            obj.put("body", "Open");
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            dos.writeUTF(obj.toString());

            String response = dis.readUTF();
            obj = new JSONObject();
            obj.put("type","Stream Message");
            obj.put("subType","Response");
            obj.put("body", "Accepted");


            assertNotEquals(response,null);
            assertNotEquals(obj.toString(),null);
            assertEquals(response,obj.toString());

            JSONObject deviceInfo = new JSONObject();
            deviceInfo.put("type","DeviceParam");
            deviceInfo.put("subType","info");
            deviceInfo.put("Device","Mobile");
            deviceInfo.put("Cripto","Strumock");

            dos.writeUTF(deviceInfo.toString());

            String response2 = dis.readUTF();

            assertNotEquals(deviceInfo.toString(),null);
            assertNotEquals(response2,null);
            JSONObject jsResponce2 = new JSONObject(response2);

            JSONObject serverRequesr = new JSONObject();
            serverRequesr.put("type","CriptoStreamMessage");
            serverRequesr.put("subType", "Request");

            assertEquals(jsResponce2.get("type"),serverRequesr.get("type"));
            assertEquals(jsResponce2.get("subType"),serverRequesr.get("subType"));
            String sha256hex = "";
            String msg = jsResponce2.getString("body");
            switch (jsResponce2.getString("alghorytm")){
                case "Strumock":
                    Cripography cripography = new Strumock();
                    String decriptedMsg = new String(cripography.decript(jsResponce2.getString("body").getBytes()), StandardCharsets.UTF_8);
                    MessageDigest digest = null;
                    try {
                        digest = MessageDigest.getInstance("SHA-256");
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    byte[] hash = digest.digest(
                            decriptedMsg.getBytes(StandardCharsets.UTF_8));
                    sha256hex = new String(Hex.encode(hash));
                    break;
            }

            JSONObject criptoRequest = new JSONObject();
            criptoRequest.put("type","CriptoStreamMessage");
            criptoRequest.put("subType","Response");
            criptoRequest.put("body",sha256hex);

            dos.writeUTF(criptoRequest.toString());

            obj = new JSONObject();
            obj.put("type", "Stream Message");
            obj.put("subType", "Request");
            obj.put("body", "Open");
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
            dos.writeUTF(obj.toString());

            response = dis.readUTF();
            obj = new JSONObject();
            obj.put("type","Stream Message");
            obj.put("subType","Response");
            obj.put("body", "Accepted");


            assertNotEquals(response,null);
            assertNotEquals(obj.toString(),null);
            assertEquals(response,obj.toString());

            JSONObject authRequest = new JSONObject(dis.readUTF());
            JSONObject constAuthRequest = new JSONObject();
            constAuthRequest.put("type","Auth");
            constAuthRequest.put("subType","Request");

            assertEquals(authRequest.toString(),constAuthRequest.toString());

            JSONObject authResponce = new JSONObject();
            authResponce.put("type","Auth");
            authResponce.put("subType", "Response");

            sha256hex = "";
            msg = "user1";
            switch (jsResponce2.getString("alghorytm")){
                case "Strumock":
                    Cripography cripography = new Strumock();
                    String decriptedMsg = new String(cripography.decript(jsResponce2.getString("body").getBytes()), StandardCharsets.UTF_8);
                    MessageDigest digest = null;
                    try {
                        digest = MessageDigest.getInstance("SHA-256");
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    byte[] hash = digest.digest(
                            decriptedMsg.getBytes(StandardCharsets.UTF_8));
                    sha256hex = new String(Hex.encode(hash));
                    break;
            }

            authResponce.put("body","user1"+","+sha256hex);
            dos.writeUTF(authResponce.toString());


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @After
    public void stop(){
        try {
            t.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
