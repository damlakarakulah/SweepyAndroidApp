package com.egeerdil.cekilisapp2.db;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

public class Login {

    private String url = ServiceConfig.serviceURL;
    private CallService callService;
    private Gson gson;
    private Context context;
    private String username;
    private String password;
    private String message = "";
    private String token = "";
    private int status;
    private JSONObject loginObject;

    public Login(Context context, String username, String password) {
        gson = new Gson();
        this.context = context;
        url = ServiceConfig.serviceURL;
        this.username = username;
        this.password = password;
    }

    public JSONObject login() {
        callService = new CallService(context);
        url = ServiceConfig.serviceURL + "user/login";
        JSONObject params = new JSONObject();

        try {
            params.put("username", username);
            params.put("password", password);
            loginObject = callService.getService(params, "POST", url);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return loginObject;

    }
}
