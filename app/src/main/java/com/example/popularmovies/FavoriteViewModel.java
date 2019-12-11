package com.example.popularmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.popularmovies.database.AppDatabase;
import com.example.popularmovies.database.FavoriteEntity;

import java.util.List;

public class FavoriteViewModel extends AndroidViewModel {

    private LiveData<List<FavoriteEntity>> favorites;

    public FavoriteViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        favorites = database.getFavoriteDao().getAllFavorites();
    }

    public LiveData<List<FavoriteEntity>> getFavorites() {
        return favorites;
    }
}
