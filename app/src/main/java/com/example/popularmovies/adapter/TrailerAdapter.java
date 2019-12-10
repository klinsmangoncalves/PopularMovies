package com.example.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.popularmovies.R;
import com.example.popularmovies.model.Trailer;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>{

    private List<Trailer.TrailerDetail> trailerDetails;
    private OnTrailerClickListener mTrailerClickListener;

    public interface OnTrailerClickListener {
        void onTrailerClickListener(String trailerName);
    };

    public TrailerAdapter(List<Trailer.TrailerDetail> trailerDetails, OnTrailerClickListener mTrailerClickListener) {
        this.trailerDetails = trailerDetails;
        this.mTrailerClickListener = mTrailerClickListener;
    }

    public void setTrailerDetails(List<Trailer.TrailerDetail> trailerDetails) {
        this.trailerDetails = trailerDetails;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.trailer_item, viewGroup, false);
        TrailerViewHolder trailerViewHolder = new TrailerViewHolder(view);
        return trailerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder trailerViewHolder, int i) {
        Trailer.TrailerDetail trailerDetail = trailerDetails.get(i);
        trailerViewHolder.trailerTitle.setText(trailerDetail.getName());
    }

    @Override
    public int getItemCount() {
        return trailerDetails.size();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView trailerTitle;

        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            trailerTitle = itemView.findViewById(R.id.tv_trailer_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int i = getAdapterPosition();
            Trailer.TrailerDetail trailer = trailerDetails.get(i);
            mTrailerClickListener.onTrailerClickListener(trailer.getKey());
        }
    }
}
