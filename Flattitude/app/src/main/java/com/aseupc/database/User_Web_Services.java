package com.aseupc.database;
import android.os.AsyncTask;
import android.util.Log;

import com.aseupc.utility_REST.CallAPI;
import com.aseupc.utility_REST.ParseResults;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by AnasHel on 18-10-15.
 */
public class User_Web_Services {


    public  boolean ws_verifyCredentials(String email, String password) {
        String urlString = "http://ec2-52-27-170-102.us-west-2.compute.amazonaws.com:8080/FlattitudeServer/flattitude/user/login/" + email + "/" + password;
        new CallLogin().execute(urlString);


        return true;
    }

    public static boolean ws_registerUser(String email, String password, String firstname, String lastname, String phonenumber) {

        return true;
    }


    private class CallLogin extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String urlString = params[0]; // URL to call
            String resultToDisplay = "";
            ParseResults result = null;
            InputStream in = null;

            // HTTP Get
            try {

                URL url = new URL(urlString);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                in = new BufferedInputStream(urlConnection.getInputStream());

            } catch (Exception e) {

                System.out.println(e.getMessage());

                return e.getMessage();

            }
            resultToDisplay = (String) in.toString();

            return resultToDisplay;

        }

        protected void onPostExecute(String result) {
            Log.i("Smth", result);
        }

    }

}