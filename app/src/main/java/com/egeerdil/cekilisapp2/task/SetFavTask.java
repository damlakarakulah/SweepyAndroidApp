package com.egeerdil.cekilisapp2.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.egeerdil.cekilisapp2.db.FetchLotteries;
import com.egeerdil.cekilisapp2.db.Login;
import com.egeerdil.cekilisapp2.db.SetFav;

import org.json.JSONObject;


public class SetFavTask extends AsyncTask<Object, Void, Object> {

    private AsyncResponse delegate = null;
    private Context context;
    private FetchLotteries fetchLotteries;
    private String type;
    private ProgressDialog loadingDialog;
    private String _id;
    private boolean isFaved;
    private Login login;
    private String token = "";
    private JSONObject setFaved;

    public SetFavTask(Context context, AsyncResponse asyncResponse, String _id, boolean isFaved) {
        delegate = asyncResponse;
        this._id = _id;
        this.isFaved = isFaved;
        this.context = context;
    }

    public SetFavTask(Context context, AsyncResponse asyncResponse) {

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loadingDialog = new ProgressDialog(context);
        loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingDialog.setCancelable(false);
        loadingDialog.setIndeterminate(false);
        showLoadingDialog("Favoriler güncelleniyor...");
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
        if(!_id.equals("")){
            setFaved = new SetFav(context,_id, isFaved).setFaved();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object result) {
        if(loadingDialog != null && loadingDialog.isShowing())
            loadingDialog.dismiss();
        delegate.processFinish(setFaved);
    }

}
