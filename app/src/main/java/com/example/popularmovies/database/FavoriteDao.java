package com.example.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Insert
    void insertFavorite(FavoriteEntity favorite);

    @Query("SELECT * FROM favorite ORDER BY title")
    LiveData<List<FavoriteEntity>> getAllFavorites();

    @Delete
    void deleteFavorite(FavoriteEntity favorite);

    @Query("SELECT * FROM favorite WHERE id = :id")
    LiveData<FavoriteEntity> getTaskById(Long id);
}
