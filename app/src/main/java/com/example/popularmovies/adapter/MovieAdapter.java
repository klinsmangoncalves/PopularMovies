package com.example.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.popularmovies.R;
import com.example.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;
import java.util.List;


/**
 * This class is used to set the movies list into a RecyclerView in order to display the movies list.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    /**
     * The list of movies to display in adapter.
     * */
    private List<Movie> mMovies;
    private int mCurrentPage;
    private int mTotalPages;

    /**
     * The listener to handle the item click.
     * */
    private final MovieAdapterClickListener mClickListener;


    /**
     * Constructor using the initial list and the class to be called when clicked.
     * @param movies with the list to display
     * @param listener the listener handler
     * */
    public MovieAdapter(List<Movie> movies, MovieAdapterClickListener listener) {
        mMovies = movies;
        mClickListener = listener;
    }


    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_item, viewGroup, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int i) {
        Movie movie = mMovies.get(i);
        movieViewHolder.mMovieName.setText(movie.getTitle());

        Picasso.with(movieViewHolder.ivPoster.getContext())
                .load(movie.getPosterPathUrl())
                .into(movieViewHolder.ivPoster);
    }

    /**
     * Set the movies in list. Used to updated the adapter.
     * @param movies list to display
     * */
    public void setMovies(List<Movie> movies) {
        this.mMovies = movies;
        notifyDataSetChanged();
    }

    public void setCurrentPage(int currentPage) {
        mCurrentPage = currentPage;
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    public void setTotalPages(int totalPages) {
        mTotalPages = totalPages;
    }

    public boolean hasNextPage(){
        return mCurrentPage < mTotalPages;
    }

    public boolean hasBackPage(){
        return mCurrentPage > 1;
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }


    /**
     * Class used to create a View Holder and handle the click.
     * */
    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /** Used to display the movie title in adapter */
        final TextView mMovieName;

        /** Used to display the image poster in adapter */
        final ImageView ivPoster;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            mMovieName = itemView.findViewById(R.id.tv_movie_name);
            ivPoster = itemView.findViewById(R.id.iv_movie_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int i = getAdapterPosition();
            Movie selectedMovie = mMovies.get(i);
            mClickListener.onMovieItemClickListener(selectedMovie);
        }
    }
}
