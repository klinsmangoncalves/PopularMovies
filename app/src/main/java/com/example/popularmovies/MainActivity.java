package com.example.popularmovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.popularmovies.adapter.MovieAdapter;
import com.example.popularmovies.adapter.MovieAdapterClickListener;
import com.example.popularmovies.model.Movie;
import com.example.popularmovies.model.MoviesResponse;
import com.example.popularmovies.task.MoviesLoader;
import com.example.popularmovies.util.NetworkUtils;
import com.google.gson.Gson;

import java.net.URL;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements MoviesLoader.OnTaskEndListener, MovieAdapterClickListener {

    private static final String TAG = MainActivity.class.getName();

    private final int PAGE_QUERY_POPULAR = 0;
    private final int PAGE_QUERY_TOP_RATED = 1;

    public static final String URL_MOVIE_EXTRA = "url_extra";

    private final int LOAD_MOVIE_LOADER_ID = 101;
    private MoviesLoader mMoviesLoader;

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private TextView mErrorMessage;
    private MovieAdapter mAdapter;
    private String mCurrentLanguage;
    private Integer mCurrentQuery;

    private String CURRENT_QUERY_EXTRA = "current_query_extra";
    private String CURRENT_PAGE_EXTRA = "current_page_extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.rv_movies_list);
        mProgressBar = findViewById(R.id.pb_loading_movies);
        mErrorMessage = findViewById(R.id.tv_loading_error_message);
        mCurrentLanguage = Locale.getDefault().getLanguage();

        mMoviesLoader = new MoviesLoader(this, this);
        getSupportLoaderManager().initLoader(LOAD_MOVIE_LOADER_ID, null, mMoviesLoader);

        //restore the user state
        if(savedInstanceState != null && savedInstanceState.containsKey(CURRENT_QUERY_EXTRA) && savedInstanceState.containsKey(CURRENT_PAGE_EXTRA)){
            int currentPage = savedInstanceState.getInt(CURRENT_PAGE_EXTRA);
            int currentQuery = savedInstanceState.getInt(CURRENT_QUERY_EXTRA);
            goToPage(currentQuery, currentPage);
        } else {
            //default page when app starts
            requestTopRatedMovies(null);

        }
    }

    @Override
    public void onEndListener(String result) {
        if (result == null) {
            Log.d(TAG, "Error Loading data, check connection");
            showError();
            return;
        }

        Gson gson = new Gson();
        MoviesResponse moviesResponse = gson.fromJson(result, MoviesResponse.class);
        Log.d(TAG, result);
        setAdapterData(moviesResponse);
    }

    private void showError() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);

    }

    private void showList() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);

    }

    private void setAdapterData(MoviesResponse response) {
        Log.d(TAG, "Setting adapter");
        int orientation = getResources().getConfiguration().orientation;

        GridLayoutManager gridLayoutManager;

        //In landscape more movie posters can be set in activity, so, it checks the
        //  orientation before set the grid.
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            int GRID_LENGTH_PORTRAIT = 2;
            gridLayoutManager = new GridLayoutManager(this, GRID_LENGTH_PORTRAIT);
        } else {
            int GRID_LENGTH_LANDSCAPE = 3;
            gridLayoutManager = new GridLayoutManager(this, GRID_LENGTH_LANDSCAPE);
        }

        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        if (mAdapter == null) {
            mAdapter = new MovieAdapter(response.getResults(), this);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setMovies(response.getResults());
        }
        mAdapter.setCurrentPage(response.getPage());
        mAdapter.setTotalPages(response.getTotal_pages());
        showList();
    }


    private void runNetworkTaskInBackground(URL moviesUrl){
        Bundle bundle = new Bundle();
        bundle.putSerializable(URL_MOVIE_EXTRA, moviesUrl);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> stringLoader = loaderManager.getLoader(LOAD_MOVIE_LOADER_ID);
        if(stringLoader == null){
            Log.d("LOADER_MOVIE", "initLoader");
            loaderManager.initLoader(LOAD_MOVIE_LOADER_ID, bundle, mMoviesLoader);
        } else {
            Log.d("LOADER_MOVIE", "restartLoader");
            loaderManager.restartLoader(LOAD_MOVIE_LOADER_ID, bundle, mMoviesLoader);
        }
    }

    private void requestPopularMovies(Integer page) {
        showLoading();
        mCurrentQuery = PAGE_QUERY_POPULAR;
        URL popularMovies = NetworkUtils.buildUrlMoviesPopular(mCurrentLanguage, page);
        runNetworkTaskInBackground(popularMovies);
    }

    private void requestTopRatedMovies(Integer page) {
        showLoading();
        mCurrentQuery = PAGE_QUERY_TOP_RATED;
        URL topRatedMovies = NetworkUtils.buildUrlMoviesTopRated(mCurrentLanguage, page);
        runNetworkTaskInBackground(topRatedMovies);
    }

    private void goToPage(Integer queryPath, int page) {
        switch (queryPath) {
            case PAGE_QUERY_POPULAR:
                requestPopularMovies(page);
                break;

            case PAGE_QUERY_TOP_RATED:
                requestTopRatedMovies(page);
                break;
        }

    }

    private void goToNextPage() {
        if (mCurrentQuery == null) {
            return;
        }

        if (!mAdapter.hasNextPage()) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_pages_to_show), Toast.LENGTH_LONG).show();
            return;
        }

        int nextPage = mAdapter.getCurrentPage() + 1;

        switch (mCurrentQuery) {
            case PAGE_QUERY_POPULAR:
                requestPopularMovies(nextPage);
                break;

            case PAGE_QUERY_TOP_RATED:
                requestTopRatedMovies(nextPage);
                break;
        }

    }

    private void goToLastPage() {
        if (mCurrentQuery == null) {
            return;
        }

        if (!mAdapter.hasBackPage()) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_pages_to_show), Toast.LENGTH_LONG).show();
            return;
        }

        int lastPage = mAdapter.getCurrentPage() - 1;

        switch (mCurrentQuery) {
            case PAGE_QUERY_POPULAR:
                requestPopularMovies(lastPage);
                break;

            case PAGE_QUERY_TOP_RATED:
                requestTopRatedMovies(lastPage);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sort_popular:
                requestPopularMovies(null);
                return true;
            case R.id.menu_sort_top_rated:
                requestTopRatedMovies(null);
                return true;
            case R.id.menu_next:
                goToNextPage();
                return true;
            case R.id.menu_back:
                goToLastPage();
                return true;
            default:
                return false;
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_QUERY_EXTRA, mCurrentQuery);
        outState.putInt(CURRENT_PAGE_EXTRA, mAdapter.getCurrentPage());
    }

    @Override
    public void onMovieItemClickListener(Movie movie) {
        Class nextActivity = MovieDetailActivity.class;
        Intent intent = new Intent(this, nextActivity);
        intent.putExtra(MovieDetailActivity.MOVIE_KEY, movie);
        startActivity(intent);
    }
}
