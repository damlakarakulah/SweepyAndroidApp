package com.egeerdil.cekilisapp2.db;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Editable;
import android.webkit.CookieManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionSpec;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class ServiceConfig {

    public static String serviceURL ="https://shrouded-escarpment-40589.herokuapp.com/";
    public static OkHttpClient httpclient;
    public static Boolean closedConnection=false;
    public static String Token;

    private static Boolean hasHttpsService = false;


    public static OkHttpClient getClient() {
        OkHttpClient okHttpClient;

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(1, TimeUnit.MINUTES) // connect timeout
                .writeTimeout(1, TimeUnit.MINUTES) // write timeout
                .readTimeout(1, TimeUnit.MINUTES); // read timeout

        okHttpClient = builder.build();
        return okHttpClient;
    }

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;

    public static boolean getConnectivityStatusBoolean(Context context) {
        int conn = getConnectivityStatus(context);
        boolean connected;
        if (conn == TYPE_NOT_CONNECTED) {
            connected = false;//"Not connected to Internet";
        } else {
            connected = true;
        }
        return connected;
    }

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }



}

