package com.egeerdil.cekilisapp2.db;


import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Response;

public class CallService {

    private Connection connection;
    private Context context;
    private JSONObject returnObject;
    private JSONArray returnArray;
    private String method;
    private String httpGetURL;
    private Response response;


    public void createConnection() {
        if (this.method != null)
            this.connection = new Connection(this.context, method, httpGetURL);
    }

    public CallService(Context context) {
        this.context = context;

    }

    public JSONObject getService(JSONObject params, String method, String httpGetURL) throws JSONException {

        if (context != null) {

            this.method = method;
            this.httpGetURL = httpGetURL;
            if (this.connection != null && !ServiceConfig.closedConnection) {
                this.connection.closeConnection();
                while (ServiceConfig.closedConnection) {
                    this.connection = null;
                    createConnection();
                    this.connection.params = params;

                    returnObject = this.connection.getConnectionMethod();

                    break;
                }
            } else {
                this.connection = null;
                createConnection();
                this.connection.params = params;

                returnObject = this.connection.getConnectionMethod();

            }
        }


        if (returnObject != null)
            return returnObject;
        else {
            return null;
        }
    }


}

