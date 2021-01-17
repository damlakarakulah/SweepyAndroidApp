package com.egeerdil.cekilisapp2.db;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class SetFav {

    private String url = ServiceConfig.serviceURL;
    private CallService callService;
    private Gson gson;
    private Context context;
    private String _id;
    private boolean isFaved;
    private String token = "";
    private int status;
    private int response;
    private JSONObject setFaved;

    public SetFav(Context context, String _id, boolean isFaved) {
        gson = new Gson();
        this.context = context;
        url = ServiceConfig.serviceURL;
        this._id = _id;
        this.isFaved = isFaved;
    }

    public JSONObject setFaved(){
        callService = new CallService(context);
        url = ServiceConfig.serviceURL + "lottery/setFaved";
        JSONObject params = new JSONObject();

        try {
            params.put("_id", _id);
            params.put("isFaved", isFaved);
            setFaved = callService.getService(params,"PUT",url);
            if(ServiceConfig.responseCode == 401) {
                Toast.makeText(context, "Oturum sonlandırıldı. Tekrar giriş yapınız.", Toast.LENGTH_LONG).show();
                return null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return setFaved;
    }

}
