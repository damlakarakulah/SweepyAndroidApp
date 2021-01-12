package com.egeerdil.cekilisapp2.fragment;

import androidx.fragment.app.Fragment;

import com.egeerdil.cekilisapp2.activity.StartActivity;
import com.egeerdil.cekilisapp2.R;


public class CosmeticsFragment extends BaseFragment {


    public CosmeticsFragment() {
        super("Cosmetics", "Kozmetik");
    }

    public static Fragment newInstance() {
        return new CosmeticsFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        StartActivity.Current.bottomNavigationView.setSelectedItemId(R.id.action_cosmetics);
    }
}