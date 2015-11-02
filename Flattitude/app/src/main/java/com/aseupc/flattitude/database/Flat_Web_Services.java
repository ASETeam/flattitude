package com.aseupc.flattitude.database;

import android.os.AsyncTask;
import android.util.Log;

import com.aseupc.flattitude.Models.Flat;
import com.aseupc.flattitude.utility_REST.CallAPI;
import com.aseupc.flattitude.utility_REST.ResultContainer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by MetzoDell on 28-10-15.
 */
public class Flat_Web_Services {


    public ResultContainer<Flat> ws_createFlat(Flat flat) {
        ResultContainer<Flat> resultContainer = new ResultContainer<Flat>();
        String urlString = "http://flattiserver-flattitude.rhcloud.com/flattiserver/flat/create";

        callPost call = new callPost();



        String FinalizeThread ="Call not executed";
        try {
            FinalizeThread = call.execute(flat).get(50000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }


        Log.i("We Before with Json: ", FinalizeThread);
        if (FinalizeThread != null)
        {
            Log.i("We start with Json: ", FinalizeThread);
            try {
                JSONObject mainObject = new JSONObject(FinalizeThread);
                String success = mainObject.getString("success");
                Log.i("When we receive JSON", success);
                if (success == "true") {
                    String userId = mainObject.getString("id");
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

    class callPost extends AsyncTask<Flat, Void, String> {

        private Exception exception;


        protected String doInBackground(Flat... flats) {
            String response = "";
            String urlStr = "http://flattiserver-flattitude.rhcloud.com/flattiserver/flat/create";
            HashMap<String, String> values = new HashMap<>();
            Flat flat = flats[0];
            values.put("name", flat.getName());
            values.put("address", flat.getAddress());
            values.put("city", flat.getCity());
            values.put("country", flat.getCountry());
            values.put("postcode", flat.getPostcode());
            values.put("IBAN", flat.getIban());
            values.put("masterid", "2");
            response = CallAPI.performPostCall(urlStr, values);
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

            Log.i("Registry has been", " changed in PostExecute");

        }
    }
}
