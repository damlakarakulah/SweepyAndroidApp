package com.egeerdil.cekilisapp2.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.egeerdil.cekilisapp2.Config;
import com.egeerdil.cekilisapp2.activity.MainActivity;
import com.egeerdil.cekilisapp2.db.ServiceConfig;
import com.egeerdil.cekilisapp2.model.User;
import com.egeerdil.cekilisapp2.task.UserInfoTask;
import com.egeerdil.cekilisapp2.R;
import com.egeerdil.cekilisapp2.activity.StartActivity;
import com.egeerdil.cekilisapp2.adapter.LotteryAdapter;
import com.egeerdil.cekilisapp2.model.Lottery;
import com.egeerdil.cekilisapp2.task.AsyncResponse;
import com.egeerdil.cekilisapp2.task.LotteryTask;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public abstract class BaseFragment extends Fragment {

    protected FragmentManager fragmentManager;
    protected Handler handler;
    protected int layoutId;
    protected static View view;
    protected String type;
    private LotteryAdapter lotteryAdapter;
    private RecyclerView recyclerView;
    private boolean isRefresh;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView header;
    private String headerName;
    private ImageButton favoriteButton;
    public TextView infoLabel;
    private ImageButton profileButton;
    private CardView profile_label;
    private LinearLayout profile_frame;
    public static  BaseFragment instance;
    private static ProgressDialog loadingDialog;
    private ImageButton logoutButton;
    private TextView username;
    private TextView email;
    private JSONArray lotteryArray;
    private List<Lottery> lotteryList;
    private SharedPreferences sharedPref;


    public BaseFragment(String type, String headerName){
        this.type = type;
        this.headerName = headerName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_base, container, false);

        getFragmentActivity();

        onViewCreated(view, savedInstanceState);
        sharedPref = getActivity().getSharedPreferences("MySharedPref",
                MODE_PRIVATE);


        return view;

    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setViewRef(view);
    }

    private void getFragmentActivity() {
        fragmentManager = StartActivity.Current.getSupportFragmentManager();

    }

    protected void setViewRef(View view){
        instance = this;
        header = view.findViewById(R.id.header);
        infoLabel = view.findViewById(R.id.info_label);
        favoriteButton = view.findViewById(R.id.favoriteButton);
        profileButton = view.findViewById(R.id.profile_button);
        logoutButton = view.findViewById(R.id.logout_button);
        username = view.findViewById(R.id.username_profile);
        email = view.findViewById(R.id.email_profile);
        header.setText(headerName);
        StartActivity.Current.activeFragment = this;
        profile_label = view.findViewById(R.id.profile_label);
        profile_frame = view.findViewById(R.id.profile_layout);
        profile_frame.setVisibility(View.GONE);
        profile_label.setVisibility(View.GONE);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartActivity.Current.changeFragment(ProfileFragment.newInstance(), "Profile", "ProfileFragment", null);
            }
        });
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartActivity.Current.changeFragment(FavoriteFragment.newInstance(), "Favorite", "FavoriteFragment", null);
            }
        });

        if(StartActivity.Current.activeFragment.getTag().equals("Profile")) {
            UserInfoTaskForProfile();
            return;
        }

        recyclerView = view.findViewById(R.id.recycler_lotteries);
        swipeRefreshLayout = view.findViewById(R.id.swiperefreshLotteries);

        //swipeRefreshLayout.setColorSchemeResources(R.color.blue);
        lotteryAdapter = new LotteryAdapter(getContext());
        setRecyclerView();
        setSwipeRefreshLayoutListener();
        if(StartActivity.Current.activeFragment.getTag().equals("Favorite")){
            UserInfoTask();
        }
        else {
            LotteryTask();
        }
    }

    private void UserInfoTaskForProfile(){
        UserInfoTask userInfoTask = new UserInfoTask(getContext(), new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                if(output == null){
                    if(!ServiceConfig.getConnectivityStatusBoolean(getActivity())){
                        Toast.makeText(getActivity(),"İnternet bağlantınızı kontrol ediniz.",Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                User user = (User)(output);
                Config.user = user;
                username.setText(Config.user.getUsername());
                email.setText(Config.user.getEmail());
                logoutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("password", "");
                        editor.apply();

                        Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                        getActivity().startActivity(mainIntent);
                        getActivity().finish();
                    }
                });
                profile_frame.setVisibility(View.VISIBLE);
                profile_label.setVisibility(View.VISIBLE);
                return;
            }
        });
        userInfoTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void UserInfoTask(){
        infoLabel.setVisibility(View.GONE);
        UserInfoTask userInfoTask = new UserInfoTask(getContext(), new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                if(output == null){
                    if(!ServiceConfig.getConnectivityStatusBoolean(getActivity())){
                        Toast.makeText(getActivity(),"İnternet bağlantınızı kontrol ediniz.",Toast.LENGTH_LONG).show();
                        isRefresh = false;
                        swipeRefreshLayout.setRefreshing(false);
                        return;
                    }
                }
                isRefresh = false;
                swipeRefreshLayout.setRefreshing(false);
                User user = (User)(output);
                Config.user = user;
                List<Lottery> lotteries = user.getFavList();
                if(lotteries != null){
                    Config.lotteryList = lotteries;
                    if(Config.lotteryList.size() == 0){
                        infoLabel.setText("Henüz hiç favoriniz yok.");
                        infoLabel.setVisibility(View.VISIBLE);
                    }
                    lotteryAdapter.UpdateData(lotteries);
                }

                lotteries = null;
            }
        });
        userInfoTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    public void onBackPressed() {

        int count = StartActivity.Current.getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            StartActivity.Current.onBackPressed();
        } else {
            StartActivity.Current.getSupportFragmentManager().popBackStack();
        }
        //getActivity().onBackPressed();
    }

    private void setSwipeRefreshLayoutListener(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(isRefresh)
                    return;

                isRefresh = true;
                if(StartActivity.Current.activeFragment.getTag().equals("Favorite")){
                    UserInfoTask();
                }
                else {
                    LotteryTask();
                }
            }
        });
    }


    public void LotteryTask(){

        if(this.getTag().equals("Home")){
            infoLabel.setVisibility(View.VISIBLE);
            infoLabel.setText("Anasayfada ekibimiz tarafından en çok beğenilmiş 10 çekiliş gösterilmektedir.");
        }
        else {
            infoLabel.setVisibility(View.GONE);
        }
        recyclerView.setAdapter(null);
        recyclerView.setAdapter(lotteryAdapter);
        swipeRefreshLayout.setRefreshing(true);
        new LotteryTask(getContext(), new AsyncResponse() {
            @Override
            public void processFinish(Object output) {

                if(output == null){
                    if(!ServiceConfig.getConnectivityStatusBoolean(getActivity())){
                        Toast.makeText(getActivity(),"İnternet bağlantınızı kontrol ediniz.",Toast.LENGTH_LONG).show();

                        isRefresh = false;
                        swipeRefreshLayout.setRefreshing(false);
                        return;
                    }
                }

                JSONObject lotteryObject = (JSONObject)output;

                try {
                    if(!type.equals("Home")){
                        lotteryArray = lotteryObject.getJSONArray("lotteries");

                    lotteryList = createLotteryList(lotteryArray);
                }
                else {
                    lotteryArray = lotteryObject.getJSONArray("lotteries");
                    if(lotteryArray.length() >= 10)
                        lotteryList = createLotteryList(lotteryArray).subList(0,10);
                    else
                        lotteryList = createLotteryList(lotteryArray);
                }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                isRefresh = false;
                swipeRefreshLayout.setRefreshing(false);

                List<Lottery> lotteries =lotteryList;
                if(lotteries != null){
                    Config.lotteryList = lotteries;
                    lotteryAdapter.UpdateData(lotteries);
                }

                lotteries = null;

            }
        }, type).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private List<Lottery> createLotteryList(JSONArray lotteries) {
        List<Lottery> lotteryList = new ArrayList<>();

        for(int i=0; i<lotteries.length(); i++){
            JSONObject jsonObject = lotteries.optJSONObject(i);
            String jsonString = jsonObject.toString();
            Gson gson = new Gson();
            final Lottery lottery = gson.fromJson(jsonString, Lottery.class);
            lotteryList.add(lottery);
        }
        Config.lotteryList = lotteryList;
        return lotteryList;
    }


    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(lotteryAdapter);

        /*ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, final int position, View v) {
                Config.selectedData = Config.dataList.get(position);
                StartActivity.Current.changeFragment(EventDetailFragment.newInstance(), "EventDetail", "EventDetailFragment", null);
            }
        });

         */

        if(Config.lotteryList != null)
            lotteryAdapter.UpdateData(Config.lotteryList);
    }

    @Override
    public void onResume() {
        super.onResume();
        StartActivity.Current.bottomNavigationView.getMenu().setGroupCheckable(0, true, true);

    }
}
