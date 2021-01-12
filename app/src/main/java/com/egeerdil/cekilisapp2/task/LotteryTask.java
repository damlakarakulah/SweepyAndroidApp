package com.egeerdil.cekilisapp2.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.egeerdil.cekilisapp2.R;
import com.egeerdil.cekilisapp2.db.FetchLotteries;
import com.egeerdil.cekilisapp2.model.Lottery;

import org.json.JSONObject;

import java.util.List;
import java.util.jar.JarEntry;


public class LotteryTask extends AsyncTask<Object, Void, Object> {

    private AsyncResponse delegate = null;
    private Context context;
    private JSONObject lotteryObject;
    private String type;
    private ProgressDialog loadingDialog;

    public LotteryTask(Context context, AsyncResponse asyncResponse, String type) {
        delegate = asyncResponse;
        this.type = type;
        this.context = context;
    }

    public LotteryTask(Context context, AsyncResponse asyncResponse) {

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
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
        lotteryObject = new FetchLotteries(context, type).getLotteries();
        return lotteryObject;
    }

    @Override
    protected void onPostExecute(Object result) {
        delegate.processFinish(lotteryObject);
    }

}
