package com.egeerdil.cekilisapp2.db;

import android.content.Context;

import com.egeerdil.cekilisapp2.Config;
import com.egeerdil.cekilisapp2.model.Lottery;
import com.egeerdil.cekilisapp2.model.User;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.egeerdil.cekilisapp2.Config.user;

public class FetchUserInfo {

    private String url = ServiceConfig.serviceURL;
    private CallService callService;
    private Gson gson;
    private Context context;
    private JSONObject userObject;
    private List<Lottery> lotteryList;
    private JSONArray lotteryArray;
    private User user;

    public FetchUserInfo(Context context) {
        gson = new Gson();
        this.context = context;
        url = ServiceConfig.serviceURL;
    }

    public User getUserInfo() {
        callService = new CallService(context);
        url = ServiceConfig.serviceURL + "user/getUserInfo";
        JSONObject params = new JSONObject();

        try {
            JSONObject jsonObject = callService.getService(params, "GET", url);
            if(jsonObject == null)
                return null;
            userObject = jsonObject.getJSONObject("user");
            user = gson.fromJson(userObject.toString(), User.class);
            lotteryArray = userObject.getJSONArray("favs");
            lotteryList = createLotteryList(lotteryArray);
            user.setFavList(lotteryList);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;

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

    public List<Lottery> getLotteryList() {
        return lotteryList;
    }
}
