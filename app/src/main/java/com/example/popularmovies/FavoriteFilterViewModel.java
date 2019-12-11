package com.example.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.popularmovies.database.AppDatabase;
import com.example.popularmovies.database.FavoriteEntity;

public class FavoriteFilterViewModel extends ViewModel {

    LiveData<FavoriteEntity> favorite;

    public FavoriteFilterViewModel(AppDatabase appDatabase, Long id) {
        favorite = appDatabase.getFavoriteDao().getTaskById(id);
    }

    public LiveData<FavoriteEntity> getFavorite(){
        return favorite;
    }
}
