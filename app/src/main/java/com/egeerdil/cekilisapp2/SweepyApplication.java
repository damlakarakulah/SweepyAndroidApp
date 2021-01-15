package com.egeerdil.cekilisapp2;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.egeerdil.cekilisapp2.activity.LoginActivity;
import com.egeerdil.cekilisapp2.activity.SignupActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SweepyApplication extends Application implements Application.ActivityLifecycleCallbacks {
    public static Activity mActivity;
    public static Context mContext;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    public void onCreate() {
        super.onCreate();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser =  firebaseAuth.getInstance().getCurrentUser();
        this.registerActivityLifecycleCallbacks(this);

    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        mActivity = activity;
        mContext=SweepyApplication.this;

        if(firebaseAuth == null || !firebaseUser.isEmailVerified()){
            if (!firebaseUser.isEmailVerified()) {
                try {
                    Toast.makeText(mContext, "Lütfen e-postanızı doğrulayınız.", Toast.LENGTH_LONG).show();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                return;
            }
            startActivity(new Intent(SweepyApplication.this, LoginActivity.class));
        }

        this.registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }
}
