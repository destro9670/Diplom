package ua.gabz.dm.entities.message;

import org.json.JSONException;
import org.json.JSONObject;
import ua.gabz.dm.entities.enums.ErrorTypes;


/**
 * Created by destr on 15.03.2020.
 */
public class Error implements Message {

    public static String getMessage(ErrorTypes errorType){  //Error msg creator
        JSONObject object = new JSONObject();

        try {
            object.put("type","Error Message");
            object.put("subType","Response");
            switch (errorType) {
                case AUTH:
                    object.put("body", "Auth");
                    break;
                case CRIPTO:
                    object.put("body", "Cripto");
                    break;
                case STREAM:
                    object.put("body", "Stream");
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

}
