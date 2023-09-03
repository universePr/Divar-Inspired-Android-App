package com.applications.divarapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.applications.divarapp.R;
import com.applications.divarapp.models.AdBookMarkModelResponse;
import com.applications.divarapp.models.AdsModel;
import com.applications.divarapp.network.API;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdBookmarkListAdapter extends RecyclerView.Adapter<AdBookmarkListAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(AdBookMarkModelResponse item);
    }

    private ArrayList<AdBookMarkModelResponse> adsItems = new ArrayList<>();
    private final LayoutInflater mInflater;
    private final Context context;
    //Click listener
    private final AdBookmarkListAdapter.OnItemClickListener listener;

    // data is passed into the constructor
    public AdBookmarkListAdapter(ArrayList<AdBookMarkModelResponse> adsItems, Context context, AdBookmarkListAdapter.OnItemClickListener listener) {
        this.adsItems = adsItems;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.listener = listener;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.my_ad_bookmark_item_layout, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AdBookMarkModelResponse ad = adsItems.get(position);
        holder.ad_title.setText(ad.getTitle());
        if(ad.getFinalPrice() != 0) {
            holder.ad_price.setText(context.getResources().getString(R.string.price) +": " + (int)ad.getFinalPrice() + " " + context.getResources().getString(R.string.dollar));
        }


        holder.ad_time.setText(ad.getDateTime() + " "+ context.getResources().getString(R.string.in) +" " + ad.getCityName());



        if(ad.isContainImage()){
            Glide.with(context)
                    .load(API.getBaseUrlApi() + ad.getImageUrls().get(0))
                    .fitCenter()
                    .into(holder.img);
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
        //Image views
        ImageView img;
        ImageView chat_img;
        RelativeLayout note_section;
        TextView note;


        public ViewHolder(View itemView) {
            super(itemView);
            ad_title = itemView.findViewById(R.id.ad_title);
            ad_price = itemView.findViewById(R.id.ad_price);
            ad_time = itemView.findViewById(R.id.ad_time);
            //Image view
            img = itemView.findViewById(R.id.img);
            chat_img = itemView.findViewById(R.id.chat_img);
            //Layout
            note_section = itemView.findViewById(R.id.note_section);
            note = itemView.findViewById(R.id.txt_note);
        }
        public void bind(AdBookMarkModelResponse item, final AdBookmarkListAdapter.OnItemClickListener listener) {
            if(!item.isNoteCheck()){
                note_section.setVisibility(View.GONE);
            }else{
                note.setText(item.getNote());
            }
            itemView.setOnClickListener(v -> {
                listener.onItemClick(item);
            });
        }
    }

}
