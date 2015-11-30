package com.aseupc.flattitude.database;

import android.os.AsyncTask;
import android.util.Log;

import com.aseupc.flattitude.Models.Flat;
import com.aseupc.flattitude.Models.User;
import com.aseupc.flattitude.utility_REST.CallAPI;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by MetzoDell on 29-10-15.
 */
public class Invitation_Web_Services {

    public ResultContainer<Flat> ws_respondInvitation(String userID, String flatID, String respond) {
        ResultContainer<Flat> resultContainer = new ResultContainer<Flat>();
        callPostInviteMember call = new callPostInviteMember();
        String ResponseString =null;

        String response = "";
        String urlStr = "https://flattiserver-flattitude.rhcloud.com/flattiserver/invitation/respond";
        HashMap<String, String> values = new HashMap<>();


        values.put("idUser", userID);
        values.put("idFlat", flatID);
        values.put("accepted", respond);

        Log.i("Respond", respond);
        response = CallAPI.performPostCall(urlStr, values);
        try {
            JSONObject mainObject = new JSONObject(response);
            //Log.i("INVITE 3", mainObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("Respond", response);

        ResponseString = response;
        if (ResponseString != null)
        {
            //Log.i("INVITE 1: ", ResponseString);
            try {
                JSONObject mainObject = new JSONObject(ResponseString);
                String success = mainObject.getString("success");
             //   Log.i("INVITE 2 :", success);
                if (success == "true") {
                    resultContainer.setSuccess(true);
                    // Temporary solution : dummy user
                    // resultContainer.setTemplate(CallAPI.getUser(userId));

                }
                else if (success == "false"){
                    resultContainer.setSuccess(false);
                    String reason = mainObject.getString("reason");
                    resultContainer.addReason(reason);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        return resultContainer;
    }

    public ResultContainer<Flat> ws_inviteMember(String userID, String flatID, String email) {
        ResultContainer<Flat> resultContainer = new ResultContainer<Flat>();
        callPostInviteMember call = new callPostInviteMember();
        String ResponseString =null;

        try {
            ResponseString = call.execute(userID, flatID, email).get(50000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        if (ResponseString != null)
        {
            Log.i("INVITE 1: ", ResponseString);
            try {
                JSONObject mainObject = new JSONObject(ResponseString);
                String success = mainObject.getString("success");
                Log.i("INVITE 2 :", success);
                if (success == "true") {
                    resultContainer.setSuccess(true);
                    // Temporary solution : dummy user
                    // resultContainer.setTemplate(CallAPI.getUser(userId));

                }
                else if (success == "false"){
                    resultContainer.setSuccess(false);
                    String reason = mainObject.getString("reason");
                    resultContainer.addReason(reason);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        return resultContainer;
    }

    public ResultContainer<User> ws_consultInvitations(String userID) {
        User user = new User();
        String urlString = "https://flattiserver-flattitude.rhcloud.com/flattiserver/invitation/consult/" + userID;
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


            Log.i("Anas 4", urlConnection.getResponseCode() + " " +  urlConnection.getResponseMessage());

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
                JSONArray invites = mainObject.getJSONArray("invitations");
                for (int i = 0; i < invites.length(); i++)
                {
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
            }
            else
                resultContainer.setSuccess(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultContainer;

    }

    class callPostInviteMember extends AsyncTask<String, Void, String> {

        private Exception exception;


        protected String doInBackground(String... contents) {
            String response = "";
            String urlStr = "https://flattiserver-flattitude.rhcloud.com/flattiserver/invitation/create";
            HashMap<String, String> values = new HashMap<>();
            String userId = contents[0];
            String flatId = contents[1];
            String email = contents[2];

            values.put("idMaster", userId);
            values.put("idFlat", flatId);
            values.put("email", email);

            response = CallAPI.performPostCall(urlStr, values);
            try {
                JSONObject mainObject = new JSONObject(response);
                Log.i("INVITE 3", mainObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("INVITE 4", response);
            return response;
        }

        protected void onPostExecute(String response) {


            Log.i("Registry has been", " changed in PostExecute");

        }
    }

}
