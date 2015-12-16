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
import java.util.Date;

/**
 * Created by MetzoDell on 25-11-15.
 */
public class Notifications_Web_Services {

    public ResultContainer<User> ws_getNotifications(String userID) {
        User user = new User();
        String urlString = "https://flattiserver-flattitude.rhcloud.com/flattiserver/notification/getNotifications/" + userID;
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
            Log.i("AFara", resultToDisplay);
            resultToDisplay = ParseResults.getStringFromInputStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i("Json Result string :", resultToDisplay);
        try {
            JSONObject mainObject = new JSONObject(resultToDisplay);
            String success = mainObject.getString("success");

            if (success.equals("true")) {
                JSONArray invites = mainObject.getJSONArray("invitation_notifications"); // Needs control
                Log.i("Booba", invites + " ");
                for (int i = 0; i < invites.length(); i++) {
                    if (invites.get(i) != null) {
                        JSONObject invitation = invites.getJSONObject(i);

                        String sender = invitation.getString("sender");
                        String flatIds = invitation.getString("flat_id");
                        int id = invitation.getInt("id");
                        Notification finalNotif = new Notification();
                        finalNotif.setServerID(id);
                        finalNotif.setId(id);
                        finalNotif.setAuthor(sender);

                        finalNotif.setObjectID(flatIds);
                        finalNotif.setTime(new Date());
                        finalNotif.setType("INVITATION");

                        user.addNotifications(finalNotif);
                    }
                }
                JSONArray invites2 = mainObject.getJSONArray("object_notifications"); // Needs control
                Log.i("Booba2", invites2 + "");
                for (int i = 0; i < invites2.length(); i++) {
                    if (invites2.get(i) != null) {
                        JSONObject invitation2 = invites2.getJSONObject(i);

                        String sender = invitation2.getString("sender");
                        //String flatIds = invitation2.getString("object_id");
                        int id = invitation2.getInt("id");
                        Notification finalNotif = new Notification();
                        finalNotif.setServerID(id);
                        finalNotif.setId(id);
                        finalNotif.setAuthor(sender);
                       // finalNotif.setObjectID(flatIds);
                        finalNotif.setTime(new Date());
                        finalNotif.setType("MAP");

                        user.addNotifications(finalNotif);
                    }
                }

                JSONArray invites3 = mainObject.getJSONArray("task_notifications"); // Needs control
                Log.i("Booba7", invites3 + "");
                for (int i = 0; i < invites3.length(); i++) {
                    if (invites3.get(i) != null) {
                        JSONObject invitation2 = invites2.getJSONObject(i);

                        String sender = invitation2.getString("sender");
                       // String flatIds = invitation2.getString("task_id");
                        int id = invitation2.getInt("id");
                        Notification finalNotif = new Notification();
                        finalNotif.setServerID(id);
                        finalNotif.setId(id);
                        finalNotif.setAuthor(sender);
                       // finalNotif.setObjectID(flatIds);
                        finalNotif.setTime(new Date());
                        finalNotif.setType("PLANNING");

                        user.addNotifications(finalNotif);
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

    public ResultContainer<User> ws_RetrievNotifications(String notifID, String timestamp) {
        User user = new User();
        String urlString = "https://flattiserver-flattitude.rhcloud.com/flattiserver/notification/retrievedNotification/" + notifID;
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
