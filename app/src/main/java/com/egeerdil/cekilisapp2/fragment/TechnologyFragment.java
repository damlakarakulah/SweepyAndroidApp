package com.egeerdil.cekilisapp2.fragment;

import androidx.fragment.app.Fragment;

import com.egeerdil.cekilisapp2.R;
import com.egeerdil.cekilisapp2.activity.StartActivity;


public class TechnologyFragment extends BaseFragment {


    public TechnologyFragment() {
        super("Technology" , "Teknoloji");
    }

    public static Fragment newInstance() {
        return new TechnologyFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        StartActivity.Current.bottomNavigationView.setSelectedItemId(R.id.action_technology);
    }

}
