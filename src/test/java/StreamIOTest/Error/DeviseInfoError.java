package StreamIOTest.Error;

import StreamIOTest.OK.StreamIO;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ua.gabz.dm.ServerStarter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by destr on 01.04.2020.
 */
public class DeviseInfoError extends Assert {

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
            Thread.sleep(1000);
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
            deviceInfo.put("Device","Mobille");
            deviceInfo.put("Cripto","Strumock");

            dos.writeUTF(deviceInfo.toString());

            String response2 = dis.readUTF();

            assertNotEquals(deviceInfo.toString(),null);
            assertNotEquals(response2,null);
            JSONObject jsResponce2 = new JSONObject(response2);

            obj = new JSONObject();
            obj.put("type", "Error Message");
            obj.put("subType", "Response");
            obj.put("body", "Stream");

            assertEquals(obj.toString(),jsResponce2.toString());

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