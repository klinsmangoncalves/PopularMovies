package com.example.popularmovies.adapter;

import com.example.popularmovies.model.Movie;

/**
 * Listener used to handle the click on Movie item
 * */
public interface MovieAdapterClickListener {
    void onMovieItemClickListener(Movie movie);
}