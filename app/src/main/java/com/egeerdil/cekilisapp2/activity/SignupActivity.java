package com.egeerdil.cekilisapp2.activity;


import androidx.annotation.NonNull;
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
import com.egeerdil.cekilisapp2.task.AsyncResponse;
import com.egeerdil.cekilisapp2.task.SignupTask;
import com.egeerdil.cekilisapp2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONException;
import org.json.JSONObject;


public class SignupActivity extends AppCompatActivity {
    private Context context;
    private TextView login;
    private TextView signup;
    private EditText nameSurname;
    private EditText emailText;
    private EditText passwordText;
    private String password2 = "";
    private EditText passwordText2;
    private String name = "";
    private String email = "";
    private  String password = "";
    private SharedPreferences sharedPref;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        context = SignupActivity.this;
        sharedPref = getSharedPreferences("MySharedPref",
                MODE_PRIVATE);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseAuth.setLanguageCode("tr");

        setView();

    }


    private void setView() {
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup_button);
        nameSurname = findViewById(R.id.nameSurname);
        emailText = findViewById(R.id.email_signup);
        passwordText = findViewById(R.id.password_signup);
        passwordText2 = findViewById(R.id.password_signup2);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(SignupActivity.this, LoginActivity.class);
                SignupActivity.this.startActivity(mainIntent);
                SignupActivity.this.finish();
            }
        });

        nameSurname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                name = s.toString();
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

        passwordText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                password2 = s.toString();
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.length() == 0 || email.length() == 0 || password.length() == 0)
                    Toast.makeText(context, "Tüm alanları doldurduğunuzdan emin olunuz.", Toast.LENGTH_LONG).show();
                else if(!password.equals(password2)){
                    Toast.makeText(context, "Şifre ile Şifre (Tekrar) birbirinden farklı olamaz.",Toast.LENGTH_SHORT).show();
                }
                else if(passwordText.length() < 6){
                    Toast.makeText(context, "Şifreniz en az 6 karakter olmalıdır.",Toast.LENGTH_SHORT).show();
                }
                else if(!ServiceConfig.getConnectivityStatusBoolean(context)){
                    Toast.makeText(context,"İnternet bağlantınızı kontrol ediniz.",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(context,"Lütfen bekleyiniz...",Toast.LENGTH_LONG).show();
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                        //progresbar.setVisibility(VIEV.VISIBLE)
                        if (task.isSuccessful()) {
                            firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    firebaseUser = firebaseAuth.getCurrentUser();
                                    //Toast.makeText(context, "Giriş yapıldı.", Toast.LENGTH_LONG).show();
                                    firebaseUser.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                                            System.out.println("RESULT" + "\n" + task.getResult().getToken());
                                            ServiceConfig.Token = task.getResult().getToken();
                                            SignupTask();
                                        }
                                    });

                                } else {
                                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
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

                if(status == 1) {
                    String emailOfUser = firebaseUser.getEmail();
                    String addMessage = " E-posta adresinizi doğruladıktan sonra giriş yapabilirsiniz: " + emailOfUser;
                    if(message != null && !message.equals("")) {
                        Toast.makeText(context, message + addMessage, Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(context, "Kullanıcı kaydı olıuşturuldu." + addMessage, Toast.LENGTH_LONG).show();

                    }
                }

                else {
                    Toast.makeText(context, "Kullanıcı kaydedilirken bir hata oluştu.", Toast.LENGTH_LONG).show();
                }

            }
        }, name, email, password);
        signupTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        System.exit(0);
    }
}