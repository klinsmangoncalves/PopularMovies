package com.example.popularmovies.task;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.example.popularmovies.MovieDetailActivity;
import com.example.popularmovies.database.AppDatabase;
import com.example.popularmovies.database.FavoriteEntity;

public class FavoriteLoader implements LoaderManager.LoaderCallbacks<Void> {

    private Context context;

    public FavoriteLoader(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Loader<Void> onCreateLoader(int i, @Nullable final Bundle bundle) {
        return new AsyncTaskLoader<Void>(context) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Nullable
            @Override
            public Void loadInBackground() {

                if(bundle == null){
                    return null;
                }
                FavoriteEntity favoriteEntity = (FavoriteEntity) bundle.getSerializable(MovieDetailActivity.FAVORITE_EXTRA);
                Integer writeOption = bundle.getInt(MovieDetailActivity.WRITE_OPTION_EXTRA);
                if(writeOption == MovieDetailActivity.OPTION_INSERT){
                    AppDatabase.getInstance(context).getFavoriteDao().insertFavorite(favoriteEntity);
                }else{
                    AppDatabase.getInstance(context).getFavoriteDao().deleteFavorite(favoriteEntity);
                }

                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Void> loader, Void aVoid) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Void> loader) {

    }
}
