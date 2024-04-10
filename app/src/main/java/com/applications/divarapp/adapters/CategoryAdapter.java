package com.applications.divarapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.applications.divarapp.R;
import com.applications.divarapp.models.CategoryModel;
import com.applications.divarapp.models.CityModel;
import com.applications.divarapp.network.API;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CategoryAdapter extends ArrayAdapter<CategoryModel> {
    //Items
    private final ArrayList<CategoryModel> items;
    //Context layouts
    private final int resourceLayout;
    private final Context mContext;


    public CategoryAdapter(Context context, int resource, ArrayList<CategoryModel> items) {
        super(context, resource, items);
        this.items = items;
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        CategoryModel p = getItem(position);
        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.category_name);
            if (tt1 != null) {
                switch (p.getId()) {
                    case 1:
                        tt1.setText(mContext.getString(R.string.unspecified_category));
                    break;
                    case 2:
                        tt1.setText(mContext.getString(R.string.vehicles));
                        break;
                    case 3:
                        tt1.setText(mContext.getString(R.string.digital_products));
                        break;
                    case 5:
                        tt1.setText(mContext.getString(R.string.home_and_kitchen));
                        break;
                    case 6:
                        tt1.setText(mContext.getString(R.string.services));
                        break;
                    case 7:
                        tt1.setText(mContext.getString(R.string.personal_items));
                        break;
                    case 8:
                        tt1.setText(mContext.getString(R.string.entertainment_and_leisure));
                        break;
                    case 9:
                        tt1.setText(mContext.getString(R.string.social));
                        break;
                    case 10:
                        tt1.setText(mContext.getString(R.string.industrial_equipment));
                        break;
                }
            }
            ImageView imageView = (ImageView) v.findViewById(R.id.img_category_icon);
            if (imageView != null && !p.getIconUrl().isEmpty()) {

                Glide.with(mContext)
                        .load(API.getBaseUrlApi() + p.getIconUrl())
                        .fitCenter()
                        .into(imageView);

            }
        }

        return v;
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public CategoryModel getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

}
