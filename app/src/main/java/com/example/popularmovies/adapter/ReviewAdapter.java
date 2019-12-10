package com.example.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.popularmovies.R;
import com.example.popularmovies.model.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review.ReviewDetail> mReviews;

    public ReviewAdapter(List<Review.ReviewDetail> mReviews) {
        this.mReviews = mReviews;
    }

    public List<Review.ReviewDetail> getmReviews() {
        return mReviews;
    }

    public void setmReviews(List<Review.ReviewDetail> mReviews) {
        this.mReviews = mReviews;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.review_item, viewGroup, false);
        ReviewViewHolder viewHolder = new ReviewViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder reviewViewHolder, int i) {
        Review.ReviewDetail reviewDetail = mReviews.get(i);
        reviewViewHolder.tvAuthor.setText(reviewDetail.getAuthor() + " says:");
        reviewViewHolder.tvReviewContent.setText(reviewDetail.getContent());
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder{

        TextView tvAuthor;
        TextView tvReviewContent;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tv_review_author);
            tvReviewContent = itemView.findViewById(R.id.tv_review_content);
        }

    }
}
