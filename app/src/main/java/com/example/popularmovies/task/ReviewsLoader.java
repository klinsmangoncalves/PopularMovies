package com.example.popularmovies.task;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.example.popularmovies.MovieDetailActivity;
import com.example.popularmovies.util.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class ReviewsLoader implements LoaderManager.LoaderCallbacks<String> {

    final Context mContext;
    final ReviewsLoader.OnReviewLoadFinish mListener;

    public interface OnReviewLoadFinish{
        void reviewLoadFinish(String ReviewsJson);
    }

    public ReviewsLoader(Context mContext, ReviewsLoader.OnReviewLoadFinish mListener) {
        this.mContext = mContext;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public Loader onCreateLoader(int i, @Nullable final Bundle bundle) {
        return new AsyncTaskLoader(mContext) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Nullable
            @Override
            public String loadInBackground() {
                URL url =  (URL) bundle.getSerializable(MovieDetailActivity.REVIEW_URL_EXTRA);

                if(url == null){
                    return null;
                }

                try {
                    return NetworkUtils.getResponseFromHttpUrl(url);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String s) {
        mListener.reviewLoadFinish(s);
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {

    }
}
