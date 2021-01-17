package com.egeerdil.cekilisapp2.db;
import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class Signup {

    private String url = ServiceConfig.serviceURL;
    private CallService callService;
    private Gson gson;
    private Context context;
    private String name;
    private String password;
    private String email;
    private String message = "";
    private String token = "";
    private int status;
    private JSONObject signupObject;

    public Signup(Context context, String name) {
        gson = new Gson();
        this.context = context;
        url = ServiceConfig.serviceURL;
        this.name = name;
    }

    public JSONObject signup() {
        callService = new CallService(context);
        url = ServiceConfig.serviceURL + "user/signup";
        JSONObject params = new JSONObject();

        try {
            params.put("name", name);
            signupObject = callService.getService(params, "POST", url);
            if(ServiceConfig.responseCode == 401) {
                Toast.makeText(context, "Oturum sonlandırıldı. Tekrar giriş yapınız.", Toast.LENGTH_LONG).show();
                return null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return signupObject;

    }

}
