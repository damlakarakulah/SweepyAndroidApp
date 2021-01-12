package com.egeerdil.cekilisapp2.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.egeerdil.cekilisapp2.R;
import com.egeerdil.cekilisapp2.db.FetchLotteries;
import com.egeerdil.cekilisapp2.db.Login;
import com.egeerdil.cekilisapp2.db.Signup;

import org.json.JSONObject;


public class SignupTask extends AsyncTask<Object, Void, Object> {

    private String email;
    private AsyncResponse delegate = null;
    private Context context;
    private FetchLotteries fetchLotteries;
    private String type;
    private ProgressDialog loadingDialog;
    private String username;
    private String password;
    private JSONObject signup;
    private String token = "";

    public SignupTask(Context context, AsyncResponse asyncResponse, String username, String email, String password) {
        delegate = asyncResponse;
        this.username = username;
        this.email = email;
        this.password = password;
        this.context = context;
    }

    public SignupTask(Context context, AsyncResponse asyncResponse) {

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loadingDialog = new ProgressDialog(context, R.style.ProgressBar);
        //loadingDialog.setProgressStyle(R.style.ProgressBar);
        loadingDialog.setCancelable(false);
        loadingDialog.setIndeterminate(false);
        showLoadingDialog("Kayıt oluşturuluyor...");
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
            signup = new Signup(context,username,email,password).signup();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object result) {
        if(loadingDialog != null && loadingDialog.isShowing())
            loadingDialog.dismiss();


        delegate.processFinish(signup);
    }

}
