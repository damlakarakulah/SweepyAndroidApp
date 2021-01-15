package com.egeerdil.cekilisapp2.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.egeerdil.cekilisapp2.fragment.BaseFragment;
import com.egeerdil.cekilisapp2.fragment.CosmeticsFragment;
import com.egeerdil.cekilisapp2.fragment.FashionFragment;
import com.egeerdil.cekilisapp2.fragment.HomeFragment;
import com.egeerdil.cekilisapp2.fragment.OtherFragment;
import com.egeerdil.cekilisapp2.fragment.MenuFragment;
import com.egeerdil.cekilisapp2.fragment.TechnologyFragment;
import com.egeerdil.cekilisapp2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

public class StartActivity extends AppCompatActivity {

    private FragmentTransaction transaction;
    private Fragment newFragment;

    public BottomNavigationView bottomNavigationView;
    public FrameLayout viewLayout;
    public BaseFragment activeFragment;
    public FragmentManager fragmentManager = getSupportFragmentManager();
    public static StartActivity Current;
    ProgressDialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

        viewLayout = findViewById(R.id.viewlayout);

        Current = this;

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        setView();


    }

    @Override
    public void onBackPressed() {
        if(activeFragment != null)
            activeFragment.onBackPressed();
        else
            super.onBackPressed();

    }

    public BaseFragment changeFragment(Fragment fragment, String tag, String backstack, Bundle bundle){
        if(newFragment != null && newFragment.getTag() != null && newFragment.getTag().equals(tag))
            return null;

        if(fragmentManager == null)
            fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        newFragment = fragment;
        if(bundle != null)
            newFragment.setArguments(bundle);
        transaction.replace(R.id.viewlayout, newFragment, tag);
        transaction.addToBackStack(backstack);
        transaction.commitAllowingStateLoss();
        return (BaseFragment) newFragment;
    }

    private void setView() {
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.action_home:
                                changeFragment(HomeFragment.newInstance(), "Home", "HomeFragment",null);
                                return true;
                            case R.id.action_cosmetics:
                                changeFragment(CosmeticsFragment.newInstance(), "Cosmetics",   "CosmeticsFragment",  null);
                                return true;
                            case R.id.action_technology:
                                changeFragment(TechnologyFragment.newInstance(), "Technology", "TechnologyFragment", null);
                                return true;
                            case R.id.action_fashion:
                                changeFragment(FashionFragment.newInstance(), "Fashion", "FashionFragment", null);
                                return true;
                            case R.id.action_other:
                                changeFragment(OtherFragment.newInstance(), "Other", "OtherFragment", null);
                                return true;
                        }

                        //this.onNavigationItemSelected(null);
                        return false;
                    }

                });


        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }


}
