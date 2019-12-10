package com.example.popularmovies.model;

import com.example.popularmovies.util.ImageUtils;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Movie implements Serializable {

    @SerializedName("id")
    private Long id;
    @SerializedName("video")
    private Boolean video;
    @SerializedName("adult")
    private Boolean adult;
    @SerializedName("vote_count")
    private Integer voteCount;
    @SerializedName("vote_average")
    private Float voteAverage;
    @SerializedName("popularity")
    private Float popularity;
    @SerializedName("title")
    private String title;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("original_language")
    private String originalLanguage;
    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("overview")
    private String overview;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("genre_ids")
    private List<Integer> genreIds;


    public Float getVoteAverage() {
        return voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPathUrl(){
        return ImageUtils.getFullImageUrl(posterPath);
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public Long getId() {
        return id;
    }
}
