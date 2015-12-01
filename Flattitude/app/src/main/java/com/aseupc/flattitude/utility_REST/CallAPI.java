package com.aseupc.flattitude.utility_REST;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.aseupc.flattitude.Models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AnasHel on 19-10-15.
 * Copyright for this code :
 * http://blog.strikeiron.com/bid/73189/Integrate-a-REST-API-into-Android-Application-in-less-than-15-minutes
 */
public class CallAPI  {



    public static User getUser1(String userID)
    {
        User user = new User();
        user.setEmail("okokk@lol.com");
        user.setIban("okokok");
        user.setFirstname("Jacques");
        user.setLastname("Verges");
        user.setPhonenbr("0489202123");
        user.setBirthdate(new Date());
        user.setServerid(userID);

        return user;
    }


    public static User getUser(String userID)
    {
        User user = new User();
        user.setServerid(userID);
        String resultToDisplay = null;
        String urlString =  "https://flattiserver-flattitude.rhcloud.com/flattiserver/user/getinfo/" + userID;
        ParseResults result = null;
        InputStream in = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        // resultToDisplay = (String) in.toString();
        try {
            resultToDisplay = ParseResults.getStringFromInputStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("The GetUser", resultToDisplay);
        try {
            JSONObject mainObject = new JSONObject(resultToDisplay);
            String success = mainObject.getString("success");
            if (success == "true")
            {
                String firstname = mainObject.getString("firstname");
                String lastname = mainObject.getString("lastname");
                String email = mainObject.getString("email");
                String phonenumber  = mainObject.getString("phonenbr");
               // Date birthdate = ParseResults.parseDate(mainObject.getString("birthdate"));
               // String iban = mainObject.getString("iban");

                user.setFirstname(firstname);
                user.setLastname(lastname);
                user.setEmail(email);
               // user.setBirthdate(birthdate);
                //.setIban(iban);
                user.setPhonenbr(phonenumber);
                user.setServerid(userID);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }



    public static String  performPostCall(String requestURL,
                                   HashMap<String, String> postDataParams) {

        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
           // String token;
            //conn.setRequestProperty("Auth",token);
            //conn.setReadTimeout(15000);
            //conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

public static  void makeToast(Context ctx, String input )
{
    CharSequence text = input;
    int duration = Toast.LENGTH_SHORT;
    Toast toast = Toast.makeText(ctx, text, duration);
    toast.show();
}

    public static boolean isNetworkAvailable(Context context) {

            boolean haveConnectedWifi = false;
            boolean haveConnectedMobile = false;

            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                    if (ni.isConnected())
                        haveConnectedWifi = true;
                if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (ni.isConnected())
                        haveConnectedMobile = true;
            }
            return haveConnectedWifi || haveConnectedMobile;
        }


}