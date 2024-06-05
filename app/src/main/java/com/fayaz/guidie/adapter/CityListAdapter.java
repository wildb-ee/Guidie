package com.fayaz.guidie.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fayaz.guidie.R;

import java.util.List;

import kotlinx.coroutines.channels.ActorKt;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.CityListViewHolder> {




    List<City> cities;
    Context context;
    private OnClickListener onClickListener;

    public CityListAdapter(List<City> cities, Context context) {
        this.cities = cities;
        this.context = context;
    }

    @NonNull
    @Override
    public CityListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_city, parent,false);
        CityListViewHolder holder = new CityListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CityListViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.cityName.setText(cities.get(position).getName());
        holder.cityCountry.setText(cities.get(position).getCountry());
        Glide.with(context).load(cities.get(position).getImage()).into(holder.cityImage);

        holder.itemView.setOnClickListener(v -> {
            if (onClickListener != null) {
                onClickListener.onClick(position, cities.get(position));
            }
        });
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, City model);
    }
    @Override
    public int getItemCount() {
        return cities.size();
    }

    public class CityListViewHolder extends RecyclerView.ViewHolder{
        private ImageView cityImage;
        private TextView cityName;
        private TextView cityCountry;

        public CityListViewHolder(@NonNull View itemView) {
            super(itemView);
            cityImage = itemView.findViewById(R.id.city_image);
            cityName = itemView.findViewById(R.id.city_name);
            cityCountry = itemView.findViewById(R.id.city_location);
        }
    }
}
