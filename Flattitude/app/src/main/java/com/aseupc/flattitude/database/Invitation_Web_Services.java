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
 * Created by MetzoDell on 29-10-15.
 */
public class Invitation_Web_Services {

    public ResultContainer<Flat> ws_inviteMember(int userID, int flatID) {
        ResultContainer<Flat> resultContainer = new ResultContainer<Flat>();
        callPostInviteMember call = new callPostInviteMember();
        String ResponseString =null;
        String str_userID = Integer.toString(userID);
        String str_flatID = Integer.toString(flatID);
        try {
            ResponseString = call.execute(str_userID, str_flatID).get(50000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        if (ResponseString != null)
        {
            Log.i("We start with Json: ", ResponseString);
            try {
                JSONObject mainObject = new JSONObject(ResponseString);
                String success = mainObject.getString("success");
                Log.i("When we receive JSON", success);
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


        return null;
    }

    class callPostInviteMember extends AsyncTask<String, Void, String> {

        private Exception exception;


        protected String doInBackground(String... contents) {
            String response = "";
            String urlStr = "http://flattiserver-flattitude.rhcloud.com/flattiserver/invitation/create";
            HashMap<String, String> values = new HashMap<>();
            String userId = contents[0];
            String flatId = contents[1];

            values.put("name", userId);
            values.put("address", flatId);

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
