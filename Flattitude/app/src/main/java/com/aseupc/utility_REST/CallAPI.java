package com.aseupc.utility_REST;

import android.app.Activity;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by AnasHel on 19-10-15.
 * Copyright for this code :
 * http://blog.strikeiron.com/bid/73189/Integrate-a-REST-API-into-Android-Application-in-less-than-15-minutes
 */
public class CallAPI extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... params) {
        String urlString=params[1]; // URL to call
        String resultToDisplay = "";
        ParseResults result = null;
        InputStream in = null;

        // HTTP Get
        try {

            URL url = new URL(urlString);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            in = new BufferedInputStream(urlConnection.getInputStream());

        } catch (Exception e ) {

            System.out.println(e.getMessage());

            return e.getMessage();

        }
        resultToDisplay = (String) in.toString();
        return resultToDisplay;

    }

    protected void onPostExecute(String result) {

    }

}