package com.example.popularmovies.task;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.example.popularmovies.MainActivity;
import com.example.popularmovies.util.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MoviesLoader implements LoaderManager.LoaderCallbacks<String> {

    public interface OnTaskEndListener {
        void onEndListener(String result);
    }

    final Context mContext;
    final OnTaskEndListener mOnTaskEndListener;

    public MoviesLoader(Context mContext, OnTaskEndListener mOnTaskEndListener) {
        this.mContext = mContext;
        this.mOnTaskEndListener = mOnTaskEndListener;
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable final Bundle bundle) {
        return new AsyncTaskLoader<String>(mContext) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                Log.d("LOADER_MOVIE", "onStartLoading");
                forceLoad();
            }

            @Nullable
            @Override
            public String loadInBackground() {
                Log.d("LOADER_MOVIE", "loadInBackground");
                URL url =  (URL) bundle.getSerializable(MainActivity.URL_MOVIE_EXTRA);

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
        Log.d("LOADER_MOVIE", "loadFinished");
        if(mOnTaskEndListener != null){
            mOnTaskEndListener.onEndListener(s);
        }


    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}
