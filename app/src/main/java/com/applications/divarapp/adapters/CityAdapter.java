package com.applications.divarapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.applications.divarapp.R;
import com.applications.divarapp.models.CityModel;

import java.util.ArrayList;

public class CityAdapter extends ArrayAdapter<CityModel> {
    //Items
    private ArrayList<CityModel> items;
    //Create backup for returning Items
    private final ArrayList<CityModel> backupItems;
    //Items length
    private int length = 0;
    //Context layouts
    private final int resourceLayout;
    private final Context mContext;

    public CityAdapter(Context context, int resource, ArrayList<CityModel> items) {
        super(context, resource, items);
        this.items = items;
        this.backupItems = items;
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

        CityModel p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.text);
            if (tt1 != null) {
                tt1.setText(p.getName());
            }
        }

        return v;
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public CityModel getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    //Searching in city items
    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                items = (ArrayList<CityModel>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                int len = constraint.length();
                if(len > length) length = len;
                else{
                    items = backupItems;
                    length = len;
                }
                FilterResults results = new FilterResults();
                ArrayList<CityModel> FilteredArrayCities = new ArrayList<CityModel>();

                // perform your search here using the searchConstraint String.

                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < items.size(); i++) {
                    CityModel data = items.get(i);
                    if (data.getName().toLowerCase().startsWith(constraint.toString()))  {
                        FilteredArrayCities.add(data);
                    }
                }

                results.count = FilteredArrayCities.size();
                results.values = FilteredArrayCities;


                return results;
            }
        };

        return filter;
    }
}
