package com.example.popularmovies.util;

public class ImageUtils {

    /** Base image path in API*/
    static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p";

    /** Default image Size to be used in the path*/
    static final String IMAGE_SIZE_URL = "/w200";

    /**
     * Created the image Url path using the default image size.
     * @param path the image path
     * */
    public static String getFullImageUrl(String path){
        return BASE_IMAGE_URL + IMAGE_SIZE_URL + path;
    }
}
