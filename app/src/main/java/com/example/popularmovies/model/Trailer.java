package com.example.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Trailer {

    @SerializedName("id")
    private String movieId;
    private List<TrailerDetail> results;

    public class TrailerDetail {
        String id;
        String iso_639_1;
        String iso_3166_1;
        String key;
        String name;
        String site;
        Integer size;
        String type;

        public String getKey() {
            return key;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public List<TrailerDetail> getResults() {
        return results;
    }

    public void setResults(List<TrailerDetail> results) {
        this.results = results;
    }
}


