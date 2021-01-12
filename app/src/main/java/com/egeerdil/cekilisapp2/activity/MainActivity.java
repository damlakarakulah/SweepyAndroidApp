package com.egeerdil.cekilisapp2.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.egeerdil.cekilisapp2.SweepyApplication;
import com.egeerdil.cekilisapp2.db.Login;
import com.egeerdil.cekilisapp2.db.ServiceConfig;
import com.egeerdil.cekilisapp2.task.AsyncResponse;
import com.egeerdil.cekilisapp2.task.LoginTask;
import com.egeerdil.cekilisapp2.R;
import com.google.gson.Gson;
import com.jgabrielfreitas.core.BlurImageView;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    Button button;
    Gson gson;
    EditText usernameTextView;
    EditText passwordTextView;
    String username = "";
    String password = "";
    Context context;
    TextView signup;
    BlurImageView blurImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setView();
        gson = new Gson();
        context = MainActivity.this;

        usernameTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                username = String.valueOf(usernameTextView.getText());
            }
        });

        passwordTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                password = String.valueOf(passwordTextView.getText());
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (username.equals("") || password.equals("")) {
                    Toast.makeText(context, "Tüm alanları doldurduğunuzdan emin olun", Toast.LENGTH_LONG).show();

                } else {
                    LoginTask loginTask = new LoginTask(context, new AsyncResponse() {
                        @Override
                        public void processFinish(Object output) {
                            if(output == null){
                                if(!ServiceConfig.getConnectivityStatusBoolean(context)){
                                    Toast.makeText(context,"İnternet bağlantınızı kontrol ediniz.",Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }
                            JSONObject login = (JSONObject) output;

                            String token = null;
                            String message = null;
                            int status = 0;

                            try {
                                token = login.has("token") ? login.getString("token") : "";
                                message = login.has("message") ? login.getString("message") : "";
                                status = login.has("status") ? login.getInt("status") : 0;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (message != null && !message.equals(""))
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                            if (status == 1) {
                                if(token!= null && !token.equals("")) {
                                    ServiceConfig.Token = token;
                                }
                                else{
                                    Toast.makeText(context, "Bir sorun oluştu.", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                Intent mainIntent = new Intent(MainActivity.this, StartActivity.class);
                                MainActivity.this.startActivity(mainIntent);
                                MainActivity.this.finish();
                            }

                        }
                    }, username, password);

                    loginTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(MainActivity.this, SignupActivity.class);
                MainActivity.this.startActivity(mainIntent);
                MainActivity.this.finish();
            }
        });

    }


    private void setView() {
        button = findViewById(R.id.button);
        usernameTextView = findViewById(R.id.username1);
        passwordTextView = findViewById(R.id.password1);
        signup = findViewById(R.id.signUp);
        blurImageView = (BlurImageView)findViewById(R.id.BlurImageView);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}