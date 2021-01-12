package com.egeerdil.cekilisapp2.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.egeerdil.cekilisapp2.db.FetchLotteries;
import com.egeerdil.cekilisapp2.db.Login;

import org.json.JSONObject;


public class LoginTask extends AsyncTask<Object, Void, Object> {

    private AsyncResponse delegate = null;
    private Context context;
    private FetchLotteries fetchLotteries;
    private String type;
    private ProgressDialog loadingDialog;
    private String username;
    private String password;
    private JSONObject login;
    private String token = "";

    public LoginTask(Context context, AsyncResponse asyncResponse, String username, String password) {
        delegate = asyncResponse;
        this.username = username;
        this.password = password;
        this.context = context;
    }

    public LoginTask(Context context, AsyncResponse asyncResponse) {

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loadingDialog = new ProgressDialog(context);
        loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingDialog.setCancelable(false);
        loadingDialog.setIndeterminate(false);
        showLoadingDialog("Giriş yapılıyor...");
    }

    public void showLoadingDialog(String message) {
        loadingDialog.setMessage(message);
        try {
            loadingDialog.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected Object doInBackground(Object... strings) {
        if(!username.equals("") && !password.equals("")){
            login = new Login(context,username,password).login();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object result) {
        if(loadingDialog != null && loadingDialog.isShowing())
            loadingDialog.dismiss();


        delegate.processFinish(login);
    }

}
