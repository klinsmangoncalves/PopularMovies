package com.example.popularmovies.task;

import android.os.AsyncTask;
import com.example.popularmovies.util.NetworkUtils;
import java.io.IOException;
import java.net.URL;

/**
 * Task to get the data from API. In the end the task call the listener to handle the data.
 * */
public class MoviesAsyncTask extends AsyncTask<URL, Void, String> {

    /**
     * Listener to handle the data in the end of the task.
     * */
    public interface OnTaskEndListener {
        void onEndListener(String result);
    }

    /**
     * Listener instance to be called when finish the task.
     * */
    private final OnTaskEndListener mListener;

    /**
     * Task constructor the the listener parameter.
     * @param listener - handles onFinish
     * */
    public MoviesAsyncTask(OnTaskEndListener listener){
        mListener = listener;
    }

    @Override
    protected String doInBackground(URL... urls) {
        URL url = urls[0];

        if(url == null){
            return null;
        }

        try {
            return NetworkUtils.getResponseFromHttpUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        if(mListener != null){
            mListener.onEndListener(s);
        }
    }
}
