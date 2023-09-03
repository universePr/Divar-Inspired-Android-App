package com.applications.divarapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.applications.divarapp.R;
import com.applications.divarapp.activities.ChoseCategoryActivity;
import com.applications.divarapp.models.AdsModel;
import com.applications.divarapp.models.CategoryModel;
import com.applications.divarapp.network.API;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(CategoryModel item);
    }
    //Items
    private ArrayList<CategoryModel> items = new ArrayList<>();
    //Context layouts
    private final LayoutInflater mInflater;
    private final Context context;
    //Click listener
    private final OnItemClickListener listener;


    public CategoryListAdapter(ArrayList<CategoryModel> items, Context context, OnItemClickListener listener) {
        this.items = items;
        this.mInflater = LayoutInflater.from(context);
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.categories_items_recylerview, parent, false);
        return new CategoryListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryModel categoryModel = items.get(position);
        holder.category_name.setText(categoryModel.getName());
        if(categoryModel.getIconUrl() != null && !categoryModel.getIconUrl().equals(""))
            Glide.with(context)
                    .load(API.getBaseUrlApi() + categoryModel.getIconUrl())
                    .fitCenter()
                    .into(holder.img);
        holder.bind(categoryModel, listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder  {
        //Text views
        TextView category_name;
        //Image views
        ImageView img;

       public ViewHolder(View itemView) {
            super(itemView);
            category_name = itemView.findViewById(R.id.category_name);
            //Image view
            img = itemView.findViewById(R.id.img);
        }
        public void bind(CategoryModel item, final OnItemClickListener listener) {
           itemView.setOnClickListener(v -> {
               listener.onItemClick(item);
           });
        }

    }
}
