package com.example.popularmovies.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {FavoriteEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "favorite_database.db";
    private static volatile AppDatabase instance;


    public static AppDatabase getInstance(Context context){
        if (instance == null){
            instance = create(context);
        }
        return instance;
    }

    private static AppDatabase create(Context context){
        return Room.databaseBuilder(context, AppDatabase.class, DB_NAME).build();
    }

    public abstract FavoriteDao getFavoriteDao();
}
