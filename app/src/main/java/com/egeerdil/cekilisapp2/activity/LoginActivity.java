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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.egeerdil.cekilisapp2.db.ServiceConfig;
import com.egeerdil.cekilisapp2.task.AsyncResponse;
import com.egeerdil.cekilisapp2.task.LoginTask;
import com.egeerdil.cekilisapp2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.gson.Gson;
import com.jgabrielfreitas.core.BlurImageView;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {
    Button button;
    Gson gson;
    EditText emailTextView;
    EditText passwordTextView;
    String email = "";
    String password = "";
    Context context;
    TextView signup;
    BlurImageView blurImageView;
    SharedPreferences sharedPref;
    private static final int RC_SIGN_IN = 123;
    private BottomSheetBehavior bottomSheet;
    private AppBarLayout bottomSheetLayout;
    private FrameLayout sheetFrame;
    public static LoginActivity instance;
    TextView forgotPassword;
    TextView continueWithoutLogin;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setView();
        gson = new Gson();
        context = LoginActivity.this;
        instance = this;

        sharedPref = getSharedPreferences("MySharedPref",
                MODE_PRIVATE);


        continueWithoutLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceConfig.Token = null;
                startStartActivity();

            }
        });

        emailTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                email = String.valueOf(emailTextView.getText());
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

        /*final BottomSheet bottomViewSelectGroup = new BottomSheet(getApplicationContext(), R.style.CustomBottomSheetDialogTheme, MainActivity.this);
        bottomViewSelectGroup.init();

         */

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.length() == 0) {
                    Toast.makeText(context, "E-posta adresinizi giriniz.", Toast.LENGTH_LONG).show();
                }
                else{
                    firebaseAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                       Toast.makeText(context, "Şifrenizi sıfırlamak için e-postanızı kontol ediniz: " + email,Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        Toast.makeText(context,"E-post adresinzi doğru girdiğinizden emin olunuz.", Toast.LENGTH_LONG);
                                    }
                                }
                            });
                }
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.length() == 0 || password.length() == 0)
                    Toast.makeText(context, "Tüm alanları doldurduğunuzdan emin olunuz.", Toast.LENGTH_LONG).show();
                else if (!ServiceConfig.getConnectivityStatusBoolean(context)) {
                    Toast.makeText(context, "İnternet bağlantınızı kontrol ediniz.", Toast.LENGTH_LONG).show();
                }
                else {
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = firebaseAuth.getCurrentUser();

                                if (!user.isEmailVerified()) {
                                    Toast.makeText(context, "Lütfen e-posta adresinizi doğrulayınız: " + user.getEmail(), Toast.LENGTH_LONG).show();
                                    return;
                                }

                                user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                                        ServiceConfig.Token = task.getResult().getToken();

                                        Toast.makeText(context, "Hoşgeldin!", Toast.LENGTH_LONG).show();

                                        startStartActivity();
                                    }
                                });
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(LoginActivity.this, "Giriş yapılamadı. E-posta adresi veya şifreniz hatalı.",
                                        Toast.LENGTH_SHORT).show();
                                // ...
                            }
                        }
                    });
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(LoginActivity.this, SignupActivity.class);
                LoginActivity.this.startActivity(mainIntent);
                LoginActivity.this.finish();
            }
        });

    }

    private void startStartActivity() {
        Intent mainIntent = new Intent(LoginActivity.this, StartActivity.class);
        LoginActivity.this.startActivity(mainIntent);
        LoginActivity.this.finish();
    }

    @Override
    public void onBackPressed() {
        System.exit(0);
    }

    private void setView() {
        button = findViewById(R.id.button);
        emailTextView = findViewById(R.id.username1);
        passwordTextView = findViewById(R.id.password1);
        signup = findViewById(R.id.signUp);
        blurImageView = (BlurImageView)findViewById(R.id.BlurImageView);
        continueWithoutLogin = findViewById(R.id.noLoginContinue);
        forgotPassword = findViewById(R.id.forgotpassword);
        //bottomSheetLayout = findViewById(R.id.activity_firebase_ui);
        //setMobileBottomSheetLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}