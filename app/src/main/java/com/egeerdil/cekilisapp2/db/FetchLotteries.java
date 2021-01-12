package com.egeerdil.cekilisapp2.db;

import android.content.Context;

import com.egeerdil.cekilisapp2.Config;
import com.egeerdil.cekilisapp2.model.Lottery;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FetchLotteries {

    private List<Lottery> lotteryList;
    private CallService callService;
    private String url = ServiceConfig.serviceURL;
    private Context context;
    private JSONArray lotteryArray;
    private String type;
    private Gson gson;

    public FetchLotteries(Context context, String type) {
        gson = new Gson();
        this.context = context;
        url = ServiceConfig.serviceURL;
        this.type = type;
    }


    public JSONObject getLotteries() {
        try{
            url = ServiceConfig.serviceURL + "lottery";
            JSONObject params = new JSONObject();
            callService = new CallService(this.context);
            if(type.equals("Home")) {
                url = ServiceConfig.serviceURL + "lottery";
                this.url += "/getAllLotteries";
            }
            else if(type.equals("Cosmetics")) {
                url = ServiceConfig.serviceURL + "lottery";
                this.url += "/getLotteriesOf";
                params = new JSONObject();
                params.put("category", "cosmetics");
            }
            else if(type.equals("Fashion")) {
                url = ServiceConfig.serviceURL + "lottery";
                this.url += "/getLotteriesOf";
                params = new JSONObject();
                params.put("category", "fashion");
            }
            else if(type.equals("Other")) {
                url = ServiceConfig.serviceURL + "lottery";
                this.url += "/getLotteriesOf";
                params = new JSONObject();
                params.put("category", "other");
            }
            else if(type.equals("Technology")) {
                url = ServiceConfig.serviceURL + "lottery";
                this.url += "/getLotteriesOf";
                params = new JSONObject();
                params.put("category", "technology");
            }

            JSONObject jsonObject2 = null;

            System.out.println(url);
            if(type.equals("Home"))
                jsonObject2 = callService.getService(params,"GET",url);
            else
                jsonObject2 = callService.getService(params, "POST", url);

            return jsonObject2;


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Lottery> createLotteryList(JSONArray lotteries) {
        List<Lottery> lotteryList = new ArrayList<>();

        for(int i=0; i<lotteries.length(); i++){
            JSONObject jsonObject = lotteries.optJSONObject(i);
            String jsonString = jsonObject.toString();
            Gson gson = new Gson();
            final Lottery lottery = gson.fromJson(jsonString, Lottery.class);
            lotteryList.add(lottery);
        }
        Config.lotteryList = lotteryList;
        return lotteryList;
    }


}
