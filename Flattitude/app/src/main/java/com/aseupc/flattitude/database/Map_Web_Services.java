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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
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
            response = call.execute(userID,token,flatID).get(30000, TimeUnit.MILLISECONDS);
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

    public  ResultContainer<MapObject> ws_editObject(MapObject object, String userID, String token) {
        callEdit call = new callEdit(object);
        ResultContainer<MapObject> response = null;
        try {
            response = call.execute(userID,token).get(30000, TimeUnit.MILLISECONDS);
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

    public  ResultContainer<MapObject> ws_removeObject(MapObject object, String userID, String token) {
        callRemove call = new callRemove(object);
        ResultContainer<MapObject> response = null;
        try {
            response = call.execute(userID,token).get(30000, TimeUnit.MILLISECONDS);
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
            super();
            this.object = object;
        }

        @Override
        protected ResultContainer<MapObject> doInBackground(String... strings) {
            ResultContainer<MapObject> resultContainer = new ResultContainer<MapObject>();
            String response = "";
            String urlStr = "https://flattiserver-flattitude.rhcloud.com/flattiserver/sharedobjects/create";
            HashMap<String, String> values = new HashMap<>();
            String userID = strings[0];
            String token = strings [1];
            String flatID = strings[2];
            flatID = "50"; //TODO: remove this line once the flat of the user is correctly retrieved at login
            values.put("userid", userID);
            values.put("flatid", flatID);
            values.put("token",token);
            values.put("objectname", object.getName());
            values.put("objectdescription", object.getDescription());
            values.put("lat", String.valueOf(object.getLatitude()));
            values.put("long", String.valueOf(object.getLongitude()));
            response = CallAPI.performPostCall(urlStr, values);

            try {
                JSONObject mainObject = new JSONObject(response);
                String success = mainObject.getString("success");
                Log.i("GUILLE RESPONSE", mainObject.toString());
                if (success == "true") {
                    String objectId = mainObject.getString("idObject");
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
            Log.i("Registry has been", " changed in PostExecute");
        }
    }

    class callEdit extends AsyncTask<String, Void, ResultContainer<MapObject>> {

        private MapObject object;

        public callEdit(MapObject object){
            super();
            this.object = object;
        }

        @Override
        protected ResultContainer<MapObject> doInBackground(String... strings) {
            ResultContainer<MapObject> resultContainer = new ResultContainer<MapObject>();
            String response = "";
            String urlStr = "https://flattiserver-flattitude.rhcloud.com/flattiserver/sharedobjects/edit";
            HashMap<String, String> values = new HashMap<>();
            String userID = strings[0];
            String token = strings [1];
            values.put("userid", userID);
            values.put("token",token);
            values.put("objectid", object.getServerId());
            values.put("objectname", object.getName());
            values.put("objectdescription", object.getDescription());
            values.put("lat", String.valueOf(object.getLatitude()));
            values.put("long", String.valueOf(object.getLongitude()));
            response = CallAPI.performPostCall(urlStr, values);

            try {
                JSONObject mainObject = new JSONObject(response);
                String success = mainObject.getString("success");
                Log.i("GUILLE RESPONSE", mainObject.toString());
                if (success == "true") {
                    resultContainer.setSuccess(true);
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

    class callRemove extends AsyncTask<String, Void, ResultContainer<MapObject>> {

        private MapObject object;

        public callRemove(MapObject object){
            super();
            this.object = object;
        }

        @Override
        protected ResultContainer<MapObject> doInBackground(String... strings) {
            ResultContainer<MapObject> resultContainer = new ResultContainer<MapObject>();
            String urlStr = "https://flattiserver-flattitude.rhcloud.com/flattiserver/sharedobjects/delete/";
            urlStr = urlStr + object.getServerId();

            InputStream in = null;
            String resultToDisplay = null;
            URL url = null;
            try {
                url = new URL(urlStr);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
                resultContainer.setSuccess(false);
                resultContainer.addReason("Internal error");
            }
            try {
                in = new BufferedInputStream(urlConnection.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
                resultContainer.setSuccess(false);
                resultContainer.addReason("Internal error");
            }

            // resultToDisplay = (String) in.toString();
            try {
                resultToDisplay = ParseResults.getStringFromInputStream(in);
            } catch (IOException e) {
                e.printStackTrace();
                resultContainer.setSuccess(false);
                resultContainer.addReason("Internal error");
            }
            try {
                JSONObject mainObject = new JSONObject(resultToDisplay);
                String success = mainObject.getString("success");

                if (success == "true") {resultContainer.setSuccess(true);
                    resultContainer.setTemplate(object);
                }
                else if (success == "false") {
                    resultContainer.setSuccess(false);
                    String reason = mainObject.getString("reason");
                    resultContainer.addReason(reason);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                resultContainer.setSuccess(false);
                resultContainer.addReason("Internal error");
            }
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