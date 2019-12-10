package com.example.popularmovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.popularmovies.adapter.MovieAdapter;
import com.example.popularmovies.adapter.TrailerAdapter;
import com.example.popularmovies.model.Movie;
import com.example.popularmovies.model.MoviesResponse;
import com.example.popularmovies.model.Trailer;
import com.example.popularmovies.task.TrailersLoader;
import com.example.popularmovies.util.NetworkUtils;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.Locale;
import java.util.Objects;

public class MovieDetailActivity extends AppCompatActivity implements  TrailersLoader.OnTrailerLoadFinish, TrailerAdapter.OnTrailerClickListener{

    private String TAG = "MovieDetailActivity";
    private static int TRAILER_LOADER_ID = 102;
    public static String TRAILER_URL_EXTRA = "trailer_url_extra";


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

    private RecyclerView mRecyclerViewTrailer;


    private TrailersLoader trailersLoader;
    private TrailerAdapter mTrailerAdapter;
    private String lang;

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
        if(!intent.hasExtra(MOVIE_KEY)){
            return;
        }

        Movie movie = (Movie) Objects.requireNonNull(intent.getExtras()).getSerializable(MOVIE_KEY);
        setMovieDetailData(Objects.requireNonNull(movie));
        // endregion

        lang = Locale.getDefault().getLanguage();
        trailersLoader = new TrailersLoader(this, this);
        getSupportLoaderManager().initLoader(TRAILER_LOADER_ID, null, trailersLoader);

        getTrailers(movie.getId());
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

    private void getTrailers(Long movieId){
        URL trailerURL = NetworkUtils.buildUrlMovieVideos(movieId, lang);
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> loader = loaderManager.getLoader(TRAILER_LOADER_ID);

        Bundle bundle = new Bundle();
        bundle.putSerializable(TRAILER_URL_EXTRA, trailerURL);

        if(loader == null){
            loaderManager.initLoader(TRAILER_LOADER_ID, bundle, trailersLoader);
        } else{
            loaderManager.restartLoader(TRAILER_LOADER_ID, bundle, trailersLoader);
        }

    }

    @Override
    public void trailerLoadFinish(String trailersJson) {

        if (trailersJson == null) {
            Log.d(TAG, "Error Loading data, check connection");
            Toast.makeText(getApplicationContext(), "An Error occurred", Toast.LENGTH_LONG).show();
            return;
        }

        Gson gson = new Gson();
        Trailer trailer = gson.fromJson(trailersJson, Trailer.class);
        Log.d(TAG, trailersJson);
        setAdapterData(trailer);

    }

    private void setAdapterData(Trailer trailer) {
        Log.d(TAG, "Setting adapter");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        mRecyclerViewTrailer.setLayoutManager(linearLayoutManager);
        mRecyclerViewTrailer.setHasFixedSize(true);

        if (mTrailerAdapter == null) {
            mTrailerAdapter = new TrailerAdapter(trailer.getResults(), this);
            mRecyclerViewTrailer.setAdapter(mTrailerAdapter);
        } else {
            mTrailerAdapter.setTrailerDetails(trailer.getResults());
        }

    }

    @Override
    public void onTrailerClickListener(String trailerName) {

    }
}
