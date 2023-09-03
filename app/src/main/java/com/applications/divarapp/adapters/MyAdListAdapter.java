package com.applications.divarapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.applications.divarapp.R;
import com.applications.divarapp.models.AdsModel;
import com.applications.divarapp.network.API;
import com.bumptech.glide.Glide;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MyAdListAdapter extends RecyclerView.Adapter<MyAdListAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(AdsModel item);
    }

    private ArrayList<AdsModel> adsItems = new ArrayList<>();
    private final LayoutInflater mInflater;
    private final Context context;
    //Click listener
    private final MyAdListAdapter.OnItemClickListener listener;

    // data is passed into the constructor
    public MyAdListAdapter(ArrayList<AdsModel> adsItems, Context context, MyAdListAdapter.OnItemClickListener listener) {
        this.adsItems = adsItems;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.listener = listener;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.my_ad_item_layout, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AdsModel ad = adsItems.get(position);
        holder.ad_title.setText(ad.getTitle());
        if(ad.getFinalPrice() != 0) {
            holder.ad_price.setText(context.getResources().getString(R.string.price) + ": " + (int)ad.getFinalPrice() + " " + context.getString(R.string.dollar));
        }

        holder.ad_time.setText(ad.getDateTime() + " " + context.getString(R.string.in)+" " + ad.getCityName());



        if(ad.isContainImage()){
            Glide.with(context)
                    .load(API.getBaseUrlApi() + ad.getImageUrls().get(0))
                    .fitCenter()
                    .into(holder.img);
        }
        if(ad.isHide()){
            holder.ad_status.setText(R.string.expired);
            holder.ad_status.setBackgroundColor(context.getResources().getColor(R.color.ad_deactivate));
        }

        if(!ad.isCanMessage()){
            holder.chat_img.setVisibility(View.INVISIBLE);
        }
        holder.bind(ad, listener);

    }

    @Override
    public int getItemCount() {
        return adsItems.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        //Text views
        TextView ad_title;
        TextView ad_price;
        TextView ad_time;
        TextView ad_status;
        //Image views
        ImageView img;
        ImageView chat_img;

        public ViewHolder(View itemView) {
            super(itemView);
            ad_title = itemView.findViewById(R.id.ad_title);
            ad_price = itemView.findViewById(R.id.ad_price);
            ad_time = itemView.findViewById(R.id.ad_time);
            ad_status = itemView.findViewById(R.id.txt_ad_state);
            //Image view
            img = itemView.findViewById(R.id.img);
            chat_img = itemView.findViewById(R.id.chat_img);
        }
        public void bind(AdsModel item, final MyAdListAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(v -> {
                listener.onItemClick(item);
            });
        }
    }
}
