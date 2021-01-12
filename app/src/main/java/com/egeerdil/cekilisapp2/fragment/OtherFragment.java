package com.egeerdil.cekilisapp2.fragment;

import androidx.fragment.app.Fragment;

import com.egeerdil.cekilisapp2.R;
import com.egeerdil.cekilisapp2.activity.StartActivity;


public class OtherFragment extends BaseFragment {


    public OtherFragment() {
        super("Other", "Diğer");
    }

    public static Fragment newInstance() {
        return new OtherFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        StartActivity.Current.bottomNavigationView.setSelectedItemId(R.id.action_other);
    }

}
