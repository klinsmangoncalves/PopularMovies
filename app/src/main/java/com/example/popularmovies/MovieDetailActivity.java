package com.example.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.popularmovies.adapter.ReviewAdapter;
import com.example.popularmovies.adapter.TrailerAdapter;
import com.example.popularmovies.database.AppDatabase;
import com.example.popularmovies.database.FavoriteEntity;
import com.example.popularmovies.model.Movie;
import com.example.popularmovies.model.Review;
import com.example.popularmovies.model.Trailer;
import com.example.popularmovies.task.FavoriteLoader;
import com.example.popularmovies.task.ReviewsLoader;
import com.example.popularmovies.task.TrailersLoader;
import com.example.popularmovies.util.NetworkUtils;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.Locale;
import java.util.Objects;

public class MovieDetailActivity extends AppCompatActivity implements  TrailersLoader.OnTrailerLoadFinish, TrailerAdapter.OnTrailerClickListener, ReviewsLoader.OnReviewLoadFinish {

    private String TAG = "MovieDetailActivity";
    private static int TRAILER_LOADER_ID = 102;
    private static int REVIEW_LOADER_ID = 103;
    private static int FAVORITE_LOADER_ID = 104;
    public static String TRAILER_URL_EXTRA = "trailer_url_extra";
    public static final String REVIEW_URL_EXTRA = "review_url_extra";
    public static final String FAVORITE_EXTRA = "favorite_extra";
    public static final String WRITE_OPTION_EXTRA = "delete_insert_option_extra";
    public static final Integer OPTION_INSERT = 1;
    public static final Integer OPTION_DELETE = 0;


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

    private Button mBtFavorite;

    private FavoriteLoader mFavoriteLoader;

    private RecyclerView mRecyclerViewTrailer;
    private RecyclerView mRecyclerViewReview;

    private TrailersLoader trailersLoader;
    private TrailerAdapter mTrailerAdapter;

    private ReviewsLoader mReviewsLoader;
    private ReviewAdapter mReviewsAdapter;

    private String lang;

    private Movie currentMovie;

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
        mRecyclerViewTrailer = findViewById(R.id.rv_trailers_list);
        mRecyclerViewReview = findViewById(R.id.rv_review_list);
        mBtFavorite = findViewById(R.id.bt_favorite);
        // endregion

        mBtFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickFavoriteMovie();
            }
        });

        // region Handle the data from Intent and set data
        Intent intent =  getIntent();
        if(!intent.hasExtra(MOVIE_KEY)){
            return;
        }

        currentMovie = (Movie) Objects.requireNonNull(intent.getExtras()).getSerializable(MOVIE_KEY);
        setMovieDetailData(Objects.requireNonNull(currentMovie));
        // endregion

        lang = Locale.getDefault().getLanguage();
        trailersLoader = new TrailersLoader(this, this);
        mReviewsLoader = new ReviewsLoader(this, this);
        mFavoriteLoader = new FavoriteLoader(this);

        getTrailers(currentMovie.getId());
        getReviews(currentMovie.getId());


    }

    public void onClickFavoriteMovie(){

        FavoriteEntity favoriteEntity = new FavoriteEntity(currentMovie);
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Void> loader = loaderManager.getLoader(FAVORITE_LOADER_ID);

        Bundle bundle = new Bundle();
        bundle.putSerializable(FAVORITE_EXTRA, favoriteEntity);

        if(currentMovie.isFavorite() != null && currentMovie.isFavorite()){
            bundle.putInt(WRITE_OPTION_EXTRA, OPTION_DELETE);
        }else{
            bundle.putInt(WRITE_OPTION_EXTRA, OPTION_INSERT);
        }



        if(loader == null){
            loaderManager.initLoader(FAVORITE_LOADER_ID, bundle, mFavoriteLoader);
        } else{
            loaderManager.restartLoader(FAVORITE_LOADER_ID, bundle, mFavoriteLoader);
        }
    }

    /** Set the data retrieved from Intent in UI
     * @param movie to display
     * */
    private void setMovieDetailData(Movie movie){
        verifyIfMovieIsFavorite(movie.getId());
        mSynopsisTextView.setText(movie.getOverview());
        mTitleTextView.setText(movie.getTitle());
        mUserRatingTextView.setText(String.valueOf(movie.getVoteAverage()));
        mReleasedDateTextView.setText(movie.getReleaseDate());
        Picasso.with(this).load(movie.getPosterPathUrl()).into(mPosterDetailImageView);
    }

    private void verifyIfMovieIsFavorite(Long id){
        FavoriteFilterViewModelFactory factory = new FavoriteFilterViewModelFactory(AppDatabase.getInstance(getApplicationContext()), id);
        FavoriteFilterViewModel viewModel = ViewModelProviders.of(this, factory).get(FavoriteFilterViewModel.class);

        viewModel.getFavorite().observe(this, new Observer<FavoriteEntity>() {
            @Override
            public void onChanged(@Nullable FavoriteEntity favoriteEntity) {
                markFavoriteIcon(favoriteEntity != null);
            }
        });
    }

    private void markFavoriteIcon(Boolean isFavorite){
        if(isFavorite){
            mBtFavorite.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_star_black_24dp, 0, 0, 0);
            currentMovie.setFavorite(true);
        }else{
            mBtFavorite.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_star_border_black_24dp, 0, 0, 0);
            currentMovie.setFavorite(false);
        }

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

    private void getReviews(Long trailerId) {
        URL reviewsURL = NetworkUtils.buildUrlMovieReviews(trailerId, lang);
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> loader = loaderManager.getLoader(REVIEW_LOADER_ID);

        Bundle bundle = new Bundle();
        bundle.putSerializable(REVIEW_URL_EXTRA, reviewsURL);

        if(loader == null){
            loaderManager.initLoader(REVIEW_LOADER_ID, bundle, mReviewsLoader);
        } else{
            loaderManager.restartLoader(REVIEW_LOADER_ID, bundle, mReviewsLoader);
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
        setAdapterTrailerData(trailer);
    }

    @Override
    public void reviewLoadFinish(String reviewsJson) {
        if (reviewsJson == null) {
            Log.d(TAG, "Error Loading data, check connection");
            Toast.makeText(getApplicationContext(), "An Error occurred", Toast.LENGTH_LONG).show();
            return;
        }

        Gson gson = new Gson();
        Review review = gson.fromJson(reviewsJson, Review.class);
        Log.d(TAG, reviewsJson);
        setAdapterReviewData(review);
    }

    private void setAdapterReviewData(Review review) {
        Log.d(TAG, "setAdapterReviewData");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        mRecyclerViewReview.setLayoutManager(linearLayoutManager);
        mRecyclerViewReview.setHasFixedSize(true);

        if (mReviewsAdapter == null) {
            mReviewsAdapter = new ReviewAdapter(review.getResults());
            mRecyclerViewReview.setAdapter(mReviewsAdapter);
        } else {
            mReviewsAdapter.setmReviews(review.getResults());
        }
    }

    private void setAdapterTrailerData(Trailer trailer) {
        Log.d(TAG, "setAdapterTrailerData");
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
    public void onTrailerClickListener(String youTubeKey) {
        Intent videoClient = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + youTubeKey));

        startActivity(videoClient);

    }


}
