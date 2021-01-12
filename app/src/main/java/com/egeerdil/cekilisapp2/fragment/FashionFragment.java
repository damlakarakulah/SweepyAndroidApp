package com.egeerdil.cekilisapp2.fragment;

import androidx.fragment.app.Fragment;

import com.egeerdil.cekilisapp2.R;
import com.egeerdil.cekilisapp2.activity.StartActivity;


public class FashionFragment extends BaseFragment {


    public FashionFragment() {
        super("Fashion", "Moda");
    }

    public static Fragment newInstance() {
        return new FashionFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        StartActivity.Current.bottomNavigationView.setSelectedItemId(R.id.action_fashion);
    }
}
