package com.egeerdil.cekilisapp2.activity;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.egeerdil.cekilisapp2.db.ServiceConfig;
import com.egeerdil.cekilisapp2.db.Signup;
import com.egeerdil.cekilisapp2.task.AsyncResponse;
import com.egeerdil.cekilisapp2.task.SignupTask;
import com.egeerdil.cekilisapp2.R;

import org.json.JSONException;
import org.json.JSONObject;


public class SignupActivity extends AppCompatActivity {
    private Context context;
    private TextView login;
    private TextView signup;
    private EditText usernameText;
    private EditText emailText;
    private EditText passwordText;
    private String username = "";
    private String email = "";
    private  String password = "";
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        context = SignupActivity.this;
        sharedPref = getSharedPreferences("MySharedPref",
                MODE_PRIVATE);
        setView();

    }


    private void setView() {
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup_button);
        usernameText = findViewById(R.id.username_signup);
        emailText = findViewById(R.id.email_signup);
        passwordText = findViewById(R.id.password_signup);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(SignupActivity.this, MainActivity.class);
                SignupActivity.this.startActivity(mainIntent);
                SignupActivity.this.finish();
            }
        });

        usernameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                username = s.toString();
            }
        });

        emailText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                email = s.toString();
            }
        });

        passwordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                password = s.toString();
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.length() == 0 || email.length() == 0 || password.length() == 0)
                    Toast.makeText(context, "Tüm alanları doldurduğunuzdan emin olun", Toast.LENGTH_LONG).show();
                else {
                    SignupTask();
                }
            }
        });
    }

    private void SignupTask() {
        SignupTask signupTask = new SignupTask(context, new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                JSONObject signupObject = (JSONObject) output;
                if(signupObject == null){
                    if(!ServiceConfig.getConnectivityStatusBoolean(context)){
                        Toast.makeText(context,"İnternet bağlantınızı kontrol ediniz.",Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                String message = null;
                int status = 0;
                try {
                    message = signupObject.has("message") ? signupObject.getString("message") : "";
                    status = signupObject.has("status") ? signupObject.getInt("status") : 0;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(message != null && !message.equals("")) {
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(context, "Kullanıcı kaydedilirken bir hata oluştu.", Toast.LENGTH_LONG).show();
                }

                if(status == 1){
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("username", username);
                    editor.apply();
                }

            }
        },username, email, password);
        signupTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}