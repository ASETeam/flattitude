package com.aseupc.database;
import android.os.AsyncTask;
import android.util.Log;

import com.aseupc.utility_REST.CallAPI;
import com.aseupc.utility_REST.ParseResults;

import org.json.JSONException;
import org.json.JSONObject;

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
            Log.i("If it works this: ", success);
            if (success.equals("true"))
            return true;
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }




    public static boolean ws_registerUser(String email, String password, String firstname, String lastname, String phonenumber) {

        return true;
    }


}