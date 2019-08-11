package com.example.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class MovieDetailActivity extends AppCompatActivity {

    /** String used to handle the data transferred from intent */
    public static final String MOVIE_KEY = "movie_key";

    /** Movie poster image */
    private ImageView mPosterDetailImageView;

    /** Movie description */
    private TextView mTitleTextView;

    /** Movie description */
    private TextView mSynopsisTextView;

    /** Movie rating by the users */
    private TextView mUserRatingTextView;

    /** Movie release date */
    private TextView mReleasedDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // region Bind View Components
        mPosterDetailImageView = findViewById(R.id.iv_detail_poster);
        mTitleTextView = findViewById(R.id.tv_movie_title);
        mSynopsisTextView = findViewById(R.id.tv_synopsis);
        mUserRatingTextView = findViewById(R.id.tv_user_rating);
        mReleasedDateTextView = findViewById(R.id.tv_release_date);
        // endregion

        // region Handle the data from Intent and set data
        Intent intent =  getIntent();
        if(intent.hasExtra(MOVIE_KEY)){
            Movie movie = (Movie) Objects.requireNonNull(intent.getExtras()).getSerializable(MOVIE_KEY);
            setMovieDetailData(Objects.requireNonNull(movie));
        }
        // endregion
    }

    /** Set the data retrieved from Intent in UI
     * @param movie to display
     * */
    private void setMovieDetailData(Movie movie){
        mSynopsisTextView.setText(movie.getOverview());
        mTitleTextView.setText(movie.getTitle());
        mUserRatingTextView.setText(String.valueOf(movie.getVoteAverage()));
        mReleasedDateTextView.setText(movie.getReleaseDate());
        Picasso.with(this).load(movie.getPosterPathUrl()).into(mPosterDetailImageView);
    }
}
