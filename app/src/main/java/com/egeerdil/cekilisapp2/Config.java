package com.egeerdil.cekilisapp2;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.egeerdil.cekilisapp2.model.Lottery;
import com.egeerdil.cekilisapp2.model.User;

import org.json.JSONArray;

import java.util.List;

public class Config {
    public static List<Lottery> lotteryList;
    public static Object selectedData;
    public static JSONArray jsonArray = new JSONArray();
    public static String JSONArrayString = "";
    public static User user;

}
