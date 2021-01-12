package com.egeerdil.cekilisapp2.fragment;

import android.view.View;

import androidx.fragment.app.Fragment;

import com.egeerdil.cekilisapp2.activity.StartActivity;


public class FavoriteFragment extends BaseFragment {


    public FavoriteFragment() {
        super("Favorite", "Favorilerim");
    }

    public static Fragment newInstance() {
        return new FavoriteFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        //StartActivity.Current.bottomNavigationView.setVisibility(View.GONE);
        StartActivity.Current.bottomNavigationView.getMenu().setGroupCheckable(0, false, true);

    }

    @Override
    protected void setViewRef(View view) {
        super.setViewRef(view);
    }
}
