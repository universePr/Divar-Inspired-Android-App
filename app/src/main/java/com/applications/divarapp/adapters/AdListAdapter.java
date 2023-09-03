package com.applications.divarapp.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.applications.divarapp.R;
import com.applications.divarapp.activities.MainActivity;
import com.applications.divarapp.models.AdsModel;
import com.applications.divarapp.network.API;
import com.applications.divarapp.utils.SPreferences;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.google.mlkit.nl.languageid.LanguageIdentifier;


import java.util.ArrayList;

public class AdListAdapter extends RecyclerView.Adapter<AdListAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(AdsModel item);
    }

    private ArrayList<AdsModel> adsItems = new ArrayList<>();
    private final LayoutInflater mInflater;
    private final Context context;
    //Click listener
    private final AdListAdapter.OnItemClickListener listener;

    // data is passed into the constructor
    public AdListAdapter(ArrayList<AdsModel> adsItems, Context context, AdListAdapter.OnItemClickListener listener) {
        this.adsItems = adsItems;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.listener = listener;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.ad_item_layout, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AdsModel ad = adsItems.get(position);
        holder.ad_title.setText(ad.getTitle());
        if(ad.getFinalPrice() != 0) {
            holder.ad_price.setText( context.getResources().getString(R.string.price) +": " + (int)ad.getFinalPrice() + " " + context.getResources().getString(R.string.dollar));
        }


        holder.ad_time.setText(ad.getDateTime() + " "+ context.getString(R.string.in) +" " + ad.getCityName());

        holder.translate_section.setOnClickListener(view -> {
            showTranslateBottomSheetDialog(
                    ad.getTitle(),
                    holder.ad_time.getText().toString(),
                    holder.ad_price.getText().toString(),
                    holder
            );
        });

        if(ad.isContainImage()){
            Glide.with(context)
                    .load(API.getBaseUrlApi() + ad.getImageUrls().get(0))
                    .fitCenter()
                    .into(holder.img);
        }else{
            holder.img.setScaleType(ImageView.ScaleType.CENTER);
        }
        if(!ad.isCanMessage()){
            holder.chat_img.setVisibility(View.INVISIBLE);
        }
        holder.bind(ad, listener);

    }
    private void showTranslateBottomSheetDialog(String title, String time, String price, ViewHolder holder){

        LanguageIdentifier languageIdentifier = LanguageIdentification.getClient();
        languageIdentifier.identifyLanguage(title)
                .addOnSuccessListener(
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(@Nullable String languageCode) {
                                if (languageCode.equals("und")) {
                                    Toast.makeText(context, context.getString(R.string.translation_failed), Toast.LENGTH_SHORT).show();
                                } else {
                                    if(languageCode.equals("en")){
                                        String lang = SPreferences.getDefaults("lang", context);

                                        if(lang.equals("es")){
                                            MainActivity.englishSpanishTranslator.translate(title)
                                                    .addOnSuccessListener(runnable -> {
                                                        holder.lang_progress.setVisibility(View.INVISIBLE);
                                                        holder.ad_title.setText(runnable);
                                                    });
                                            MainActivity.englishSpanishTranslator.translate(time)
                                                    .addOnSuccessListener(runnable -> {
                                                        holder.ad_time.setText(runnable);
                                                    });
                                            MainActivity.englishSpanishTranslator.translate(price)
                                                    .addOnSuccessListener(runnable -> {
                                                        holder.ad_price.setText(runnable);
                                                    });

                                        }else if(lang.equals("fa")){
                                            holder.lang_progress.setVisibility(View.VISIBLE);
                                            MainActivity.englishPersianTranslator.translate(title)
                                                    .addOnSuccessListener(runnable -> {
                                                        holder.lang_progress.setVisibility(View.INVISIBLE);
                                                        holder.ad_title.setText(runnable);
                                                    });
                                            MainActivity.englishPersianTranslator.translate(time)
                                                    .addOnSuccessListener(runnable -> {
                                                        holder.ad_time.setText(runnable);
                                                    });
                                            MainActivity.englishPersianTranslator.translate(price)
                                                    .addOnSuccessListener(runnable -> {
                                                        holder.ad_price.setText(runnable);
                                                    });

                                        }

                                    }
                                    else if(languageCode.equals("es")){
                                        holder.lang_progress.setVisibility(View.VISIBLE);
                                        MainActivity.spanishEnglishTranslator.translate(title)
                                                .addOnSuccessListener(runnable -> {
                                                    holder.lang_progress.setVisibility(View.INVISIBLE);
                                                    holder.ad_title.setText(runnable);
                                                });
                                        MainActivity.spanishEnglishTranslator.translate(time)
                                                .addOnSuccessListener(runnable -> {
                                                    holder.ad_time.setText(runnable);
                                                });
                                        MainActivity.spanishEnglishTranslator.translate(price)
                                                .addOnSuccessListener(runnable -> {
                                                    holder.ad_price.setText(runnable);
                                                });


                                    }else if(languageCode.equals("fa")){
                                        holder.lang_progress.setVisibility(View.VISIBLE);
                                        MainActivity.persianEnglishTranslator.translate(title)
                                                .addOnSuccessListener(runnable -> {
                                                    holder.lang_progress.setVisibility(View.INVISIBLE);
                                                    holder.ad_title.setText(runnable);
                                                });
                                        MainActivity.persianEnglishTranslator.translate(time)
                                                .addOnSuccessListener(runnable -> {
                                                    holder.ad_time.setText(runnable);
                                                });
                                        MainActivity.persianEnglishTranslator.translate(price)
                                                .addOnSuccessListener(runnable -> {
                                                    holder.ad_price.setText(runnable);
                                                });

                                    }

                                }

                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, context.getString(R.string.translation_failed), Toast.LENGTH_SHORT).show();
                            }
                        });


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

        LinearLayout translate_section;

        ProgressBar lang_progress;

        public ViewHolder(View itemView) {
            super(itemView);
            ad_title = itemView.findViewById(R.id.ad_title);
            ad_price = itemView.findViewById(R.id.ad_price);
            ad_time = itemView.findViewById(R.id.ad_time);
            //Image view
            img = itemView.findViewById(R.id.img);
            chat_img = itemView.findViewById(R.id.chat_img);
            translate_section = itemView.findViewById(R.id.translate_section);
            lang_progress = itemView.findViewById(R.id.lang_progress);

        }
        public void bind(AdsModel item, final AdListAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(v -> {
                listener.onItemClick(item);
            });
        }
    }
}
