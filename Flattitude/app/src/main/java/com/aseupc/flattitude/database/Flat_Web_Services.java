package com.aseupc.flattitude.database;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.aseupc.flattitude.Models.Flat;
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
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by MetzoDell on 28-10-15.
 */
public class Flat_Web_Services {

    public ResultContainer<Flat> ws_getInfo(String flatID) {
        ResultContainer<Flat> response = new ResultContainer<>();
        callGetFlatInfo call = new callGetFlatInfo();
        try {
          response =   call.execute(flatID).get(50000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        return response;
    }

    public ResultContainer<Flat> ws_createFlat(Flat flat) {
        ResultContainer<Flat> resultContainer = new ResultContainer<Flat>();
        String urlString = "https://flattiserver-flattitude.rhcloud.com/flattiserver/flat/create";

        callPostCreateFlat call = new callPostCreateFlat();


        String FinalizeThread = "Call not executed";
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
        if (FinalizeThread != null) {
            Log.i("FLAT JSON: ", FinalizeThread);
            try {
                JSONObject mainObject = new JSONObject(FinalizeThread);
                String success = mainObject.getString("success");
                Log.i("When we receive JSON", success);
                if (success == "true") {
                    //String userId = mainObject.getString("id");
                    String flatID = mainObject.getString("id");
                    flat.setServerid(flatID);
                    resultContainer.setSuccess(true);
                    // Temporary solution : dummy user
                    // resultContainer.setTemplate(CallAPI.getUser(userId));

                } else if (success == "false") {
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

    class callGetFlatInfo extends AsyncTask<String, Void, ResultContainer<Flat>> {



        @Override
        protected ResultContainer<Flat> doInBackground(String... params) {
            ResultContainer<Flat> response = new ResultContainer<>();
            Flat ThisFlat = new Flat();
            String flatID = params[0];
            ThisFlat.setServerid(flatID);
            String resultToDisplay = null;
            String urlString = "https://flattiserver-flattitude.rhcloud.com/flattiserver/flat/info/" + flatID;
            Log.i("GETFLAT URL", urlString);
            ParseResults result = null;
            InputStream in = null;


            URL url = null;
            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                in = new BufferedInputStream(urlConnection.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            // resultToDisplay = (String) in.toString();
            try {
                resultToDisplay = ParseResults.getStringFromInputStream(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("The GetFlat", resultToDisplay);
            try {
                JSONObject mainObject = new JSONObject(resultToDisplay);
                String success = mainObject.getString("success");

                if (success == "true") {
                    JSONObject flat = mainObject.getJSONObject("flat");
                    String address = flat.getString("address");
                    String name = flat.getString("name");
                    String postcode = flat.getString("postcode");
                    String iban = flat.getString("iban");
                    String city = flat.getString("city");
                    String country = flat.getString("country");

                    ThisFlat.setAddress(address);
                    ThisFlat.setServerid(flatID);
                    ThisFlat.setCountry(country);
                    ThisFlat.setCity(city);
                    ThisFlat.setIban(iban);
                    ThisFlat.setName(name);
                    ThisFlat.setPostcode(postcode);


                    response.setSuccess(true);
                    response.setTemplate(ThisFlat);

                } else {
                    response.setSuccess(false);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return response;
        }
    }

    class callPostCreateFlat extends AsyncTask<Flat, Void, String> {

        private Exception exception;


        protected String doInBackground(Flat... flats) {
            String response = "";
            String urlStr = "https://flattiserver-flattitude.rhcloud.com/flattiserver/flat/create";
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