package com.aseupc.flattitude.database;

import android.util.Log;

import com.aseupc.flattitude.Models.Flat;
import com.aseupc.flattitude.Models.Notification;
import com.aseupc.flattitude.Models.User;
import com.aseupc.flattitude.utility_REST.ParseResults;
import com.aseupc.flattitude.utility_REST.ResultContainer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by MetzoDell on 25-11-15.
 */
public class Notifications_Web_Services {

    public ResultContainer<User> ws_getNotifications(String userID) {
        User user = new User();
        String urlString = "https://flattiserver-flattitude.rhcloud.com/flattiserver/Notification/getNotifications/" + userID;
        ResultContainer<User> resultContainer = new ResultContainer<User>();
        String resultToDisplay = "";
        ParseResults result = null;
        InputStream in = null;
        // HTTP Get
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
           /* urlConnection.setRequestMethod("GET");
            urlConnection.setInstanceFollowRedirects(true);
            HttpURLConnection.setFollowRedirects(true);*/


            in = new BufferedInputStream(urlConnection.getInputStream());

            URLConnection con = url.openConnection();
            System.out.println("Orignal URL: " + con.getURL());
            con.connect();
            System.out.println("Connected URL: " + con.getURL());
            InputStream is = con.getInputStream();
            System.out.println("Redirected URL: " + con.getURL());
            is.close();


            Log.i("Anas 4", urlConnection.getResponseCode() + " " + urlConnection.getResponseMessage());

        } catch (Exception e) {
            //   System.out.println(e.getMessage());
        }
        // resultToDisplay = (String) in.toString();
        try {
            resultToDisplay = ParseResults.getStringFromInputStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i("Json Result string :", resultToDisplay);
        try {
            JSONObject mainObject = new JSONObject(resultToDisplay);
            String success = mainObject.getString("success");

            if (success.equals("true")) {
                JSONArray invites = mainObject.getJSONArray("notifications"); // Needs control
                for (int i = 0; i < invites.length(); i++) {
                    if (invites.get(i) != null) {
                        JSONObject flat = invites.getJSONObject(i);
                        //  int id = new Random().nextInt(200000);
                        String address = flat.getString("address");
                        String name = flat.getString("name");
                        String postcode = flat.getString("postcode");
                        String iban = flat.getString("iban");
                        String city = flat.getString("city");
                        String country = flat.getString("country");
                        String id = flat.getString("flatid");
                        Flat finalFlat = new Flat();
                        finalFlat.setServerid(id);
                        finalFlat.setName(name);

                        finalFlat.setAddress(address);
                        finalFlat.setPostcode(postcode);
                        finalFlat.setIban(iban);
                        finalFlat.setCity(city);
                        finalFlat.setCountry(country);
                        user.addInvite(finalFlat);
                    }
                }
                resultContainer.setTemplate(user);
            } else
                resultContainer.setSuccess(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultContainer;
    }

    public ResultContainer<User> ws_RetrievNotifications(String userID, String timestamp) {
        User user = new User();
        String urlString = "https://flattiserver-flattitude.rhcloud.com/flattiserver/Notification/retrieved/" + userID + "/" + timestamp;
        ResultContainer<User> resultContainer = new ResultContainer<User>();
        String resultToDisplay = "";
        ParseResults result = null;
        InputStream in = null;
        // HTTP Get
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            in = new BufferedInputStream(urlConnection.getInputStream());
            URLConnection con = url.openConnection();
            System.out.println("Orignal URL: " + con.getURL());
            con.connect();
            System.out.println("Connected URL: " + con.getURL());
            InputStream is = con.getInputStream();
            System.out.println("Redirected URL: " + con.getURL());
            is.close();
            Log.i("Anas 4", urlConnection.getResponseCode() + " " + urlConnection.getResponseMessage());
        } catch (Exception e) {
        }
        try {
            resultToDisplay = ParseResults.getStringFromInputStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i("Json Result string :", resultToDisplay);
        try {
            JSONObject mainObject = new JSONObject(resultToDisplay);
            String success = mainObject.getString("success");

            if (success.equals("true")) {
                resultContainer.setSuccess(true);
            } else
                resultContainer.setSuccess(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultContainer;
    }

}
