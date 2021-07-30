package com.omarm.weatherappassessment.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.omarm.weatherappassessment.R;

import java.util.ArrayList;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.CityViewHolder> {

    Context context;

    private ArrayList<String> cityNameList = new ArrayList<>();


    public CityListAdapter(Context context) {
        this.context = context;
    }

    public void refresh() {
        notifyDataSetChanged();
    }

    public ArrayList<String> getCityNameList() {
        return cityNameList;
    }

    public void setCityNameList(ArrayList<String> cityNameList) {
        this.cityNameList = cityNameList;
    }

    public void addItem(String item, int position) {
        cityNameList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public void removeItem(int cityIndex) {
        cityNameList.remove(cityIndex);
        notifyItemRemoved(cityIndex);
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.city_list_item, parent, false);

        return new CityViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CityViewHolder holder, int position) {
        String cityName = cityNameList.get(position);

        holder.mCityName.setText(cityName);
    }

    @Override
    public int getItemCount() {
        return cityNameList.size();
    }

    public static class CityViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout viewDeleteForeground;

        public TextView mCityName;

        public CityViewHolder(View itemView) {
            super(itemView);

            viewDeleteForeground = itemView.findViewById(R.id.view_content_foreground);

            mCityName = itemView.findViewById(R.id.city_name);
        }
    }
}
