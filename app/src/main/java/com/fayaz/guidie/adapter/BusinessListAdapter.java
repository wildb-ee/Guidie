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
import com.fayaz.guidie.dto.YelpBusinessResponse;

import java.util.List;

public class BusinessListAdapter extends RecyclerView.Adapter<BusinessListAdapter.BusinessListViewHolder> {




    List<YelpBusinessResponse> businesses;
    Context context;
    private OnClickListener onClickListener;

    public BusinessListAdapter(List<YelpBusinessResponse> businesses, Context context) {
        this.businesses = businesses;
        this.context = context;
    }

    @NonNull
    @Override
    public BusinessListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_business, parent,false);
        BusinessListViewHolder holder = new BusinessListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BusinessListViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.businessName.setText(businesses.get(position).getName());
        String rat=Double.toString(businesses.get(position).getRating());
        holder.businessRating.setText(rat);
        Glide.with(context).load(businesses.get(position).getImage_url()).into(holder.businessImage);
        holder.businessCategory.setText(businesses.get(position).getCategories().get(0).getTitle());

        holder.itemView.setOnClickListener(v -> {
            if (onClickListener != null) {
                onClickListener.onClick(position, businesses.get(position));
            }
        });
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, YelpBusinessResponse model);
    }
    @Override
    public int getItemCount() {
        return businesses.size();
    }

    public class BusinessListViewHolder extends RecyclerView.ViewHolder{
        private ImageView businessImage;
        private TextView businessName;
        private TextView businessCategory;
        private TextView businessRating;

        public BusinessListViewHolder(@NonNull View itemView) {
            super(itemView);
            businessImage = itemView.findViewById(R.id.business_image);
            businessName = itemView.findViewById(R.id.business_name);
            businessCategory = itemView.findViewById(R.id.business_category);
            businessRating = itemView.findViewById(R.id.business_rating);
        }
    }
}
