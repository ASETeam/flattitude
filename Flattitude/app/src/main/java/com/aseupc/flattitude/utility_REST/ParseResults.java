package com.aseupc.flattitude.utility_REST;

import android.util.Log;

import com.aseupc.flattitude.Models.Notification;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by AnasHel on 19-10-15.
 * Copyright for this code :
 * http://blog.strikeiron.com/bid/73189/Integrate-a-REST-API-into-Android-Application-in-less-than-15-minutes
 */
public class ParseResults {

    public static Date parseDate(String date){
        DateFormat df = new SimpleDateFormat("YYYY-MM-dd");
        try {
            return df.parse(date); //It have to be verified which format the database returns!
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getStringFromInputStream(InputStream is) throws IOException {
/*
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
                Log.i("Line", line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
*/

        BufferedReader streamReader = new BufferedReader(new InputStreamReader(is));

        StringBuilder responseStrBuilder = new StringBuilder();

        String inputStr;
        while ((inputStr = streamReader.readLine()) != null) {
            responseStrBuilder.append(inputStr);
            Log.i("Anas 5", inputStr);

        }



        return responseStrBuilder.toString();
    }

    public static String makePhrase(Notification notification)
    {
        String response ="";
        String author = notification.getAuthor();
        if (author == null)
            author = "Nobody";
        if (notification.getType() == null)
            return "Wrong info";
        switch (notification.getType().toUpperCase()) {
            case "CHAT":
                response = author + " posted a message in the chat on " + notification.getTime();
                break;
            case "ADD":
                response = author + " posted a message in the chat on " + notification.getTime();
                break;
            case "INVITATION":
                response = author + " invited you to join a flat on " + notification.getTime();
                break;
            case "MAP":
                response = author + " shared an object with you on " + notification.getTime();
                break;
            case "PLANNING":
                response = author + " planed a task on " + notification.getTime();
                break;
            default:
                response = "A new notification has been posted about " + notification.getType();
        }


        return response;
    }
}
