package com.applications.divarapp.adapters;

import android.content.Context;
import android.media.Image;
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

import java.util.ArrayList;

public class SubCategoryAdapter extends ArrayAdapter<CategoryModel.SubCategoryModel> {

    //Items
    private final ArrayList<CategoryModel.SubCategoryModel> items;
    //Context layouts
    private final int resourceLayout;
    private final Context mContext;

    public SubCategoryAdapter(Context context, int resource, ArrayList<CategoryModel.SubCategoryModel> items) {
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

        CategoryModel.SubCategoryModel p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.category_name);
            if (tt1 != null) {
                tt1.setText(p.getName());
            }
            if(p.getSubs() == null){
                NextBtn(v, false);
            }
            else NextBtn(v, p.getSubs() == null || p.getSubs().size() != 0);
        }

        return v;
    }
    private void NextBtn(View v, boolean show){
        ImageView next = (ImageView) v.findViewById(R.id.img_next);
        if (next != null) {
            if(show) next.setVisibility(View.VISIBLE); else next.setVisibility(View.GONE);
        }
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public CategoryModel.SubCategoryModel getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

}
