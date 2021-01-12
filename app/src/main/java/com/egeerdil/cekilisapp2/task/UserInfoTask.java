package com.egeerdil.cekilisapp2.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.egeerdil.cekilisapp2.R;
import com.egeerdil.cekilisapp2.db.FetchLotteries;
import com.egeerdil.cekilisapp2.db.FetchUserInfo;
import com.egeerdil.cekilisapp2.model.User;

import org.json.JSONObject;


public class UserInfoTask extends AsyncTask<Object, Void, Object> {

    private AsyncResponse delegate = null;
    private Context context;
    private ProgressDialog loadingDialog;
    private String token = "";
    private User user;

    public UserInfoTask(Context context, AsyncResponse asyncResponse) {
        delegate = asyncResponse;
        this.context = context;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loadingDialog = new ProgressDialog(context, R.style.ProgressBar);
        //loadingDialog.setProgressStyle(R.style.ProgressBar);
        loadingDialog.setCancelable(false);
        loadingDialog.setIndeterminate(false);
        showLoadingDialog("Yükleniyor...");
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
        user = new FetchUserInfo(context).getUserInfo();
        return null;
    }

    @Override
    protected void onPostExecute(Object result) {
        if(loadingDialog != null && loadingDialog.isShowing())
            loadingDialog.dismiss();


        delegate.processFinish(user);
    }

}
