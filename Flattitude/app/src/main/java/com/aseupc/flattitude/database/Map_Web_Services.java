package com.aseupc.flattitude.database;
import android.os.AsyncTask;
import android.util.Log;

import com.aseupc.flattitude.Models.MapObject;
import com.aseupc.flattitude.Models.User;
import com.aseupc.flattitude.utility_REST.CallAPI;
import com.aseupc.flattitude.utility_REST.ParseResults;
import com.aseupc.flattitude.utility_REST.ResultContainer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * Created by JordiColl on 10-11-15.
 */
public class Map_Web_Services {
    private String statusRegister;

    public String getStatusRegister() {
        return statusRegister;
    }

    public void setStatusRegister(String statusRegister) {
        this.statusRegister = statusRegister;
    }

    public  ResultContainer<MapObject> ws_addObject(MapObject object, String userID, String token, String flatID) {
        callCreate call = new callCreate(object);
        ResultContainer<MapObject> response = null;
        try {
            response = call.execute(userID,token,flatID).get(50000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            response = new ResultContainer<>();
            response.setSuccess(false);
            response.addReason("Internal error");
        } catch (ExecutionException e) {
            e.printStackTrace();
            response = new ResultContainer<>();
            response.setSuccess(false);
            response.addReason("Internal error");
        } catch (TimeoutException e) {
            e.printStackTrace();
            response = new ResultContainer<>();
            response.setSuccess(false);
            response.addReason("Internal error");
        }
        return response;
    }

    class callCreate extends AsyncTask<String, Void, ResultContainer<MapObject>> {

        private MapObject object;

        public callCreate(MapObject object){
            this.object = object;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ResultContainer<MapObject> doInBackground(String... strings) {
            ResultContainer<MapObject> resultContainer = new ResultContainer<MapObject>();
            String response = "";
            String urlStr = "https://flattiserver-flattitude.rhcloud.com/flattiserver/sharingobject/create";
            HashMap<String, String> values = new HashMap<>();
            String userID = strings[0];
            String token = strings [1];
            String flatID = strings[2];
            values.put("userid", userID);
            values.put("flatid", flatID);
            values.put("objectname", object.getName());
            values.put("objectdescription", object.getDescription());
            values.put("lat", String.valueOf(object.getLatitude()));
            values.put("lng", String.valueOf(object.getLongitude()));
            response = CallAPI.performPostCall(urlStr, values);

            try {
                JSONObject mainObject = new JSONObject(response);
                String success = mainObject.getString("success");
                Log.i("GUILLE RESPONSE", mainObject.toString());
                if (success == "true") {
                    String objectId = mainObject.getString("id");
                    resultContainer.setSuccess(true);
                    object.setServerId(objectId);
                    resultContainer.setTemplate(object);
                }
                else if (success == "false"){
                    resultContainer.setSuccess(false);
                    String reason = mainObject.getString("reason");
                    resultContainer.addReason(reason);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                resultContainer.setSuccess(false);
                resultContainer.addReason("Internal error");
            }
            Log.i("GUILLE RESPONSE", response);
            return resultContainer;
        }

        @Override
        protected void onPostExecute(ResultContainer<MapObject> response) {
            // TODO: check this.exception
            // TODO: do something with the feed

            Log.i("Registry has been", " changed in PostExecute");
        }
    }
}