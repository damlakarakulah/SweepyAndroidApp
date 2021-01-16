package com.egeerdil.cekilisapp2.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.egeerdil.cekilisapp2.db.ServiceConfig;
import com.egeerdil.cekilisapp2.task.AsyncResponse;
import com.egeerdil.cekilisapp2.task.SetFavTask;
import com.egeerdil.cekilisapp2.R;
import com.egeerdil.cekilisapp2.activity.StartActivity;
import com.egeerdil.cekilisapp2.model.Lottery;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.List;

public class LotteryAdapter extends RecyclerView.Adapter<LotteryAdapter.LotterHolder> {
    public List<Lottery> myDataset;
    public Context context;
    private Toast toast;

    public void UpdateData(List<Lottery> lotteryList) {
        this.myDataset = lotteryList;
        notifyDataSetChanged();
    }

    public LotteryAdapter(Context context){
        this.context = context;
    }


    @NotNull
    @Override
    public LotterHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_data, parent, false);
        return new LotterHolder(view);

    }

    @Override
    public void onBindViewHolder(@NotNull final LotterHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.setLottery(myDataset.get(position));

    }

    @Override
    public int getItemCount() {
        return myDataset != null ? myDataset.size() : 0;
    }

    public class LotterHolder extends RecyclerView.ViewHolder {

        private final TextView lotteryLink;
        public TextView lotteryDesc;
        public TextView image;
        public TextView date;
        public CardView lotteryFrame;
        private TextView time;
        private ImageButton event_icon;
        private final ImageView imageView;
        private final ImageButton saveButton;
        JSONObject jsonObject = new JSONObject();
        boolean refresh = true;


        LotterHolder(View view) {

            super(view);
            this.lotteryDesc = view.findViewById(R.id.description);
            this.lotteryLink = view.findViewById(R.id.link);
            this.lotteryFrame = view.findViewById(R.id.lotteryFrame);
            this.image = view.findViewById(R.id.link);
            this.imageView = view.findViewById(R.id.image);
            this.saveButton = view.findViewById(R.id.saveButton);
            this.saveButton.setOnClickListener(null);
            //this.saveButton.setSelected(false);
        }


        void setLottery(final Lottery lottery) {

            lotteryDesc.setText(lottery.getName());
            lotteryLink.setText("Çekilişe Git");
            Picasso.get().load(lottery.getPhoto_link()).into(imageView);

            this.lotteryLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(lottery.getLink().trim())));
                        StartActivity.Current.startActivity(browse);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(context,"Linke ulaşılamıyor.",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            if(StartActivity.Current.activeFragment != null && (StartActivity.Current.activeFragment.getTag().equals("Favorite") || StartActivity.Current.activeFragment.getTag().equals("Menu"))){
                StartActivity.Current.bottomNavigationView.setSelectedItemId(-1);
            }
            refresh = true;


            saveButton.setSelected(lottery.getIsFaved());



            this.saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!ServiceConfig.getConnectivityStatusBoolean(context)){
                        toast = Toast.makeText(context,"İnternet bağlantınızı kontrol ediniz.",Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }

                    if(toast != null){
                        toast.cancel();
                    }

                    if(ServiceConfig.Token == null){
                        Toast.makeText(context, "Favorilere ekleme yapmak için kullanıcı girişi yapılmış olmalı.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    saveButton.setSelected(!saveButton.isSelected());
                    SetFavTask setFavTask = new SetFavTask(context, new AsyncResponse() {
                        @Override
                        public void processFinish(Object output) {
                            JSONObject jsonObject = (JSONObject) output;
                            System.out.println("USER DATAAA\n" + jsonObject);
                        }
                    }, lottery.get_id(), saveButton.isSelected());
                    setFavTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            });

        }
    }
}