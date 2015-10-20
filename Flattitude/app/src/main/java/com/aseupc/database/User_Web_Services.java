package com.aseupc.database;
import android.os.AsyncTask;
import android.util.Log;

import com.aseupc.utility_REST.CallAPI;
import com.aseupc.utility_REST.ParseResults;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Normalizer;
import java.util.AbstractMap;
import java.util.HashMap;


/**
 * Created by AnasHel on 18-10-15.
 */
public class User_Web_Services {
    private String statusRegister;

    public String getStatusRegister() {
        return statusRegister;
    }

    public void setStatusRegister(String statusRegister) {
        this.statusRegister = statusRegister;
    }

    public  boolean ws_verifyCredentials(String email, String password) {
               String urlString = "http://ec2-52-27-170-102.us-west-2.compute.amazonaws.com:8080/FlattitudeServer/flattitude/user/login/" + email + "/" + password;

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
        }
        // resultToDisplay = (String) in.toString();
        resultToDisplay = ParseResults.getStringFromInputStream(in);

        Log.i("Json Result string :", resultToDisplay);
        try {
            JSONObject mainObject = new JSONObject(resultToDisplay);
            String success = mainObject.getString("success");

            if (success.equals("true"))
            return true;
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }




    public boolean ws_registerUser(String email, String password, String firstname, String lastname, String phonenumber) {
        String urlString = "http://ec2-52-27-170-102.us-west-2.compute.amazonaws.com:8080/FlattitudeServer/flattitude/user/create";
        setStatusRegister("False");
        callPost call = new callPost();
        call.execute(urlString);


         String response = "";
        return true;
    }


    class callPost extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urlString) {
            String response = "";
            HashMap<String, String> values = new HashMap<>();
            values.put("email", "anas@toid.com");
            values.put("password", "anass");
            values.put("firstname", "fname");
            values.put("lastname", "lname");
            values.put("phonenumber", "0094949");
            response = CallAPI.performPostCall(urlString[0], values);
            System.out.println("GUILLE -------  " + response);
            try {
                JSONObject mainObject = new JSONObject(response);
                Log.i("GUILLE RESPONSE", mainObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("GUILLE RESPONSE", response);
            return response;
        }

        protected void onPostExecute(String response) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }

}