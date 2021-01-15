package com.egeerdil.cekilisapp2.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.fragment.app.Fragment;

import com.egeerdil.cekilisapp2.R;
import com.egeerdil.cekilisapp2.SweepyApplication;
import com.egeerdil.cekilisapp2.activity.StartActivity;
import com.egeerdil.cekilisapp2.db.ServiceConfig;


public class HomeFragment extends BaseFragment {


    public HomeFragment() {
        super("Home", "Anasayfa");
    }

    public static Fragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        StartActivity.Current.bottomNavigationView.setSelectedItemId(R.id.action_home);
    }

    @Override
    public void onBackPressed() {
        if(ServiceConfig.Token == null){
            getActivity().recreate();
        }
        System.exit(0);
    }
}
