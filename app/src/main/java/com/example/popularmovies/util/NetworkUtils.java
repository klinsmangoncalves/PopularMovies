/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.popularmovies.util;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the movie servers.
 */
public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String  END_POINT_MOVIE_NAME = "/movie";
    private static final String  PATH_TOP_RATED = "/top_rated";
    private static final String  PATH_POPULAR = "/popular";

    private final static String LANGUAGE_PARAM = "language";
    private final static String PAGE_PARAM = "page";
    private final static String API_KEY_PARAM = "api_key";
    private final static String API_KEY_VALUE = "";


    /**
     * Builds the builder with base params.
     * @param endPoint The end-point to be accessed
     * @param path The path to query
     * @return builder
     */
    private static Uri.Builder getBaseUriBuilder(String endPoint, String path){
        return Uri.parse(BASE_URL + endPoint + path).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY_VALUE);
    }

    /**
     * Builds the URL used to talk to the movies server.
     * @param path The path desired to query.
     * @param language The app language, default to English (en-US).
     * @param page The required page to open, default page is 1.
     * @return The URL to use to query the weather server.
     */
    private static URL buildUrlMoviesList(String path, String language, Integer page) {
        Uri.Builder builder = getBaseUriBuilder(END_POINT_MOVIE_NAME, path);

        if(language != null){
            builder.appendQueryParameter(LANGUAGE_PARAM, language);
        }

        if(page != null){
            builder.appendQueryParameter(PAGE_PARAM, Integer.toString(page));
        }

        Uri builtUri = builder.build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    /**
     * Get the top rated movies list using default values.
     * */
    public static URL buildUrlMoviesTopRated() {
        return buildUrlMoviesList(PATH_TOP_RATED, null, null);
    }

    /**
     * Get the popular movies list using default language values.
     * */
    public static URL buildUrlMoviesPopular() {
        return buildUrlMoviesList(PATH_POPULAR, null, null);
    }

    /**
     * Get the top rated movies list using custom value
     * */
    public static URL buildUrlMoviesTopRated(String lang, Integer page) {
        return buildUrlMoviesList(PATH_TOP_RATED, lang, page);
    }

    /**
     * Get the popular movies list using default language values.
     * */
    public static URL buildUrlMoviesPopular(String lang, Integer page) {
        return buildUrlMoviesList(PATH_POPULAR, lang, page);
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}