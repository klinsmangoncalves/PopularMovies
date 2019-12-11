package com.example.popularmovies;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.popularmovies.database.AppDatabase;

public class FavoriteFilterViewModelFactory extends ViewModelProvider.NewInstanceFactory{

    private final AppDatabase mDb;
    private final Long mFavoriteId;

    public FavoriteFilterViewModelFactory(AppDatabase mDb, Long mFavoriteId) {
        this.mDb = mDb;
        this.mFavoriteId = mFavoriteId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FavoriteFilterViewModel(mDb, mFavoriteId);
    }
}
