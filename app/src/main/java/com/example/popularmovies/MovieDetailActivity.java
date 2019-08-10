package com.example.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    /** String used to handle the data transferred from intent */
    public static final String MOVIE_KEY = "movie_key";

    /** Movie poster image */
    ImageView mPosterDetailImageView;

    /** Movie description */
    TextView mSynopsisTextView;

    /** Movie rating by the users */
    TextView mUserRatingTextView;

    /** Movie release date */
    TextView mReleasedDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // region Bind View Components
        mPosterDetailImageView = findViewById(R.id.iv_detail_poster);
        mSynopsisTextView = findViewById(R.id.tv_synopsis);
        mUserRatingTextView = findViewById(R.id.tv_user_rating);
        mReleasedDateTextView = findViewById(R.id.tv_release_date);
        // endregion

        // region Handle the data from Intent and set data
        Intent intent =  getIntent();
        if(intent.hasExtra(MOVIE_KEY)){
            Movie movie = (Movie) intent.getExtras().getSerializable(MOVIE_KEY);
            setMovieDetailData(movie);
        }
        // endregion
    }

    /** Set the data retrieved from Intent in UI
     * @param movie to display
     * */
    public void setMovieDetailData(Movie movie){
        mSynopsisTextView.setText(movie.getOverview());
        mUserRatingTextView.setText(String.valueOf(movie.getVoteAverage()));
        mReleasedDateTextView.setText(movie.getReleaseDate());
        Picasso.with(this).load(movie.getPosterPathUrl()).into(mPosterDetailImageView);
    }
}
