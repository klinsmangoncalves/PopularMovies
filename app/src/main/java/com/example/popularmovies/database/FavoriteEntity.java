package com.example.popularmovies.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.example.popularmovies.model.Movie;

import java.io.Serializable;

@Entity(tableName = "favorite")
public class FavoriteEntity implements Serializable {

    @PrimaryKey
    private Long id;
    private String title;
    private String localPosterPath;
    private String overview;
    private String releaseDate;
    private Float voteAverage;

    public FavoriteEntity(Long id, String title, String localPosterPath, String overview, String releaseDate, Float voteAverage) {
        this.id = id;
        this.title = title;
        this.localPosterPath = localPosterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
    }

    @Ignore
    public FavoriteEntity(Movie currentMovie) {
        this.id = currentMovie.getId();
        this.title = currentMovie.getTitle();
        this.localPosterPath = currentMovie.getPosterPath();
        this.overview = currentMovie.getOverview();
        this.releaseDate = currentMovie.getReleaseDate();
        this.voteAverage = currentMovie.getVoteAverage();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocalPosterPath() {
        return localPosterPath;
    }

    public void setLocalPosterPath(String localPosterPath) {
        this.localPosterPath = localPosterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Float voteAverage) {
        this.voteAverage = voteAverage;
    }
}
