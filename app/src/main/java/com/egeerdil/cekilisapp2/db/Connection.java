package com.egeerdil.cekilisapp2.db;


import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;

import com.egeerdil.cekilisapp2.SweepyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@SuppressWarnings("deprecation")
public class Connection {

    private Context context = null;
    private String method = null;
    private String httpGetURL;
    private OkHttpClient okHttpClient;
    static String responseText = "";


    private Response response;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public JSONObject params;
    public JSONObject jsonObject;
    public JSONArray jsonArray;

    public Connection(Context context, String method, String httpGetURL) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.context = context;
        this.method = method;
        this.httpGetURL = httpGetURL;
        okHttpClient = ServiceConfig.getClient();


        if (ServiceConfig.httpclient == null) {
            ServiceConfig.httpclient = ServiceConfig.getClient();
        }
    }


    public JSONObject getConnectionMethod() {
        okHttpClient = getOkHttpClient();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (this.method.equals("POST")) {
            try {

                RequestBody body = RequestBody.create(JSON, params.toString());
                Request request;
                if(ServiceConfig.Token == null) {
                    request = new Request.Builder()
                            .url(httpGetURL.trim())
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Accept", "application/json")
                            .post(body)
                            .build();
                }
                else {
                    request = new Request.Builder()
                            .url(httpGetURL.trim())
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Accept", "application/json")
                            .addHeader("authorization", "Bearer " + ServiceConfig.Token)
                            .post(body)
                            .build();
                }

                response = okHttpClient.newCall(request).execute();

                if (response.isSuccessful() && response.code() == 200) {
                    String responseText = response.body().string();
                    Log.d("Response", responseText);
                    ServiceConfig.responseCode = 200;
                    jsonObject = new JSONObject(responseText);
                } else if(response.code() == 401) {
                    responseText = response.body().string();
                    Log.d("Response", responseText);
                    response.body().close();
                    ServiceConfig.responseCode = 401;
                }
                else
                    jsonObject = null;

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                closeConnection();
            }

        } else if (method.equals("GET")) {
            try {
                //RequestBody body = null;
                //body = RequestBody.create(JSON, params.toString());
                Request request = new Request.Builder()
                        .url(httpGetURL.trim())
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Accept", "application/json")
                        .addHeader("authorization", "Bearer " + ServiceConfig.Token)
                        //.put(body)
                        .build();
                response = getOkHttpClient().newCall(request).execute();
                if (response.isSuccessful() && response.code() == 200) {
                    String responseText = response.body().string();
                    Log.d("Response", responseText); //
                    ServiceConfig.responseCode = 200;
                    jsonObject = new JSONObject(responseText);

                }
                else if(response.code() == 401) {
                    responseText = response.body().string();
                    Log.d("Response", responseText);
                    response.body().close();
                    ServiceConfig.responseCode = 401;
                    //Toast.makeText(context, "Oturum sonlandırıldı. Tekrar giriş yapınız.", Toast.LENGTH_LONG).show();
                }
                else
                    jsonObject = null;

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                closeConnection();
            }
        } else if (this.method.equals("DELETE")) {
            try {
                RequestBody body = RequestBody.create(JSON, params.toString());

                Request request = new Request.Builder()
                        .url(httpGetURL.trim())
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Accept", "application/json")
                        .delete(body)
                        .build();

                response = okHttpClient.newCall(request).execute();

                if (response.isSuccessful() && response.code() == 200) {
                    responseText = response.body().string();
                    Log.d("Response", responseText);
                    response.body().close();
                    ServiceConfig.responseCode = 200;
                    jsonObject = new JSONObject(responseText);
                } else if(response.code() == 401) {
                    responseText = response.body().string();
                    Log.d("Response", responseText);
                    response.body().close();
                    ServiceConfig.responseCode = 401;
                    //Toast.makeText(context, "Oturum sonlandırıldı. Tekrar giriş yapınız.", Toast.LENGTH_LONG).show();
                }
                else{
                    jsonObject = null;
                    System.out.println("not successful");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeConnection();
            }

        }
        else if (this.method.equals("PUT")) {

            try {
                RequestBody body = RequestBody.create(JSON, params.toString());
                Request request = new Request.Builder()
                        .url(httpGetURL.trim())
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Accept", "application/json")
                        .addHeader("authorization", "Bearer " + ServiceConfig.Token)
                        .put(body)
                        .build();

                response = okHttpClient.newCall(request).execute();

                if (response.isSuccessful() && response.code() == 200) {
                    responseText = response.body().string();
                    Log.d("Response", responseText);
                    response.body().close();
                    ServiceConfig.responseCode = 200;
                    jsonObject = new JSONObject(responseText);
                }
                else if(response.code() == 401) {
                    responseText = response.body().string();
                    Log.d("Response", responseText);
                    response.body().close();
                    ServiceConfig.responseCode = 401;
                    //Toast.makeText(context, "Oturum sonlandırıldı. Tekrar giriş yapınız.", Toast.LENGTH_LONG).show();
                }
                else {
                    jsonObject = null;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeConnection();
            }
        }

        return jsonObject;

    }


    public void closeConnection() {

        if (response != null && response.body() != null)
            response.body().close();

        if (ServiceConfig.httpclient != null)
            ServiceConfig.httpclient.dispatcher().executorService().shutdown();

        response = null;

    }

    private OkHttpClient getOkHttpClient(){

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.MINUTES) // connect timeout
                .writeTimeout(5, TimeUnit.MINUTES) // write timeout
                .readTimeout(5, TimeUnit.MINUTES); // read timeout

        okHttpClient = builder.build();

        return okHttpClient;
    }

    private OkHttpClient provideOkHttpClient() {
        Cache cache = new Cache(new File(context.getCacheDir(), "http-cache"), 10 * 1024 * 1024);
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(provideCacheInterceptor())
                .cache(cache)
                .build();
    }


    private Interceptor provideCacheInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());

                CacheControl cacheControl = new CacheControl.Builder()
                        .maxAge(10, TimeUnit.MINUTES)
                        .build();

                response = response.newBuilder()
                        .header("Cache-Control", cacheControl.toString())
                        .build();
                return response;
            }
        };
    }

}
