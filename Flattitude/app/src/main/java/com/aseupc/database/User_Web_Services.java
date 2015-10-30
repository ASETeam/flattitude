package com.aseupc.database;
import android.os.AsyncTask;
import android.util.Log;

import com.aseupc.Models.User;
import com.aseupc.utility_REST.CallAPI;
import com.aseupc.utility_REST.ParseResults;
import com.aseupc.utility_REST.ResultContainer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Normalizer;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * Created by AnasHel on 18-10-15.
 */
public class User_Web_Services {
    private String statusRegister;

    public String getStatusRegister() {
        return statusRegister;
    }

    public void setStatusRegister(String statusRegister) {
        this.statusRegister = statusRegister;
    }

    public  ResultContainer<User> ws_verifyCredentials(String email, String password) {
        ResultContainer<User> resultContainer = new ResultContainer<User>();
        String urlString = "http://flattiserver-flattitude.rhcloud.com/flattiserver/user/login/" + email + "/" + password;
        String resultToDisplay = "";
        ParseResults result = null;
        InputStream in = null;
        // HTTP Get
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            in = new BufferedInputStream(urlConnection.getInputStream());
        } catch (Exception e) {
         //   System.out.println(e.getMessage());
        }
        // resultToDisplay = (String) in.toString();
        resultToDisplay = ParseResults.getStringFromInputStream(in);

        Log.i("Json Result string :", resultToDisplay);
        try {
            JSONObject mainObject = new JSONObject(resultToDisplay);
            String success = mainObject.getString("success");
            String userid = mainObject.getString("id");

            if (success.equals("true")) {
                resultContainer.setSuccess(true);
                resultContainer.setTemplate(CallAPI.getUser(userid));
            }
            else
            resultContainer.setSuccess(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultContainer; /*
        User user = new User();
        user.setPassword(password);
        user.setEmail(email);
        executeLogin call = new executeLogin();
        String resultString = null;
        try {
            resultString = call.execute(user).get(50000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        ResultContainer<User> resultContainer = new ResultContainer<>();
        resultContainer.setTemplate(user);
        try {
            JSONObject mainObject = new JSONObject(resultString);
            String success = mainObject.getString("success");

            if (success.equals("true"))
                resultContainer.setSuccess(true);
            else
                resultContainer.setSuccess(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        return resultContainer;*/
    }




    public ResultContainer<User> ws_registerUser(String email, String password, String firstname, String lastname, String phonenumber) {
        ResultContainer<User> resultContainer = new ResultContainer<User>();
        String urlString = "http://flattiserver-flattitude.rhcloud.com/flattiserver/user/create";

            callPost call = new callPost();


        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setLastname(lastname);
        user.setFirstname(firstname);
        user.setPhonenbr(phonenumber);
        String FinalizeThread ="Call not executed";
        try {
             FinalizeThread = call.execute(user).get(50000, TimeUnit.MILLISECONDS);
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
                    User dummy = new User();
                    dummy.setServerid(userId);
                    dummy.setEmail("kookokk@lol.be");
                    dummy.setPhonenbr("094324");
                    dummy.setIban("32424");
                    dummy.setFirstname("kpokpk");
                    dummy.setLastname("okokok");
                    resultContainer.setTemplate(dummy);
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


    class callPost extends AsyncTask<User, Void, String> {

        private Exception exception;


        protected String doInBackground(User... users) {
            String response = "";
            String urlStr = "http://flattiserver-flattitude.rhcloud.com/flattiserver/user/create";
            HashMap<String, String> values = new HashMap<>();
            User user = users[0];
            values.put("email", user.getEmail());
            values.put("password", user.getPassword());
            values.put("firstname", user.getFirstname());
            values.put("lastname", user.getLastname());
            values.put("phonenbr", user.getPhonenbr());
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
            setStatusRegister(response);
            Log.i("Registry has been", " changed in PostExecute");

        }
    }

    class callGet extends AsyncTask<User, Void, ResultContainer<User>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected ResultContainer<User> doInBackground(User... users) {
            ResultContainer<User> resultContainer = new ResultContainer<User>();
            User user = users[0];
            String urlString = "http://flattiserver-flattitude.rhcloud.com/flattiserver/user/login/" + user.getEmail() + "/" + user.getPassword();
            String resultToDisplay = "";
            ParseResults result = null;
            InputStream in = null;
            // HTTP Get
            Log.i("Anas", "Before HTTP");
            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                in = new BufferedInputStream(urlConnection.getInputStream());
                Log.i("Anas", "After HTTP");

            } catch (Exception e) {
                //   System.out.println(e.getMessage());
            }
            // resultToDisplay = (String) in.toString();
            resultToDisplay = ParseResults.getStringFromInputStream(in);

            Log.i("Json Result string :", resultToDisplay);
            try {
                JSONObject mainObject = new JSONObject(resultToDisplay);
                String success = mainObject.getString("success");

                if (success.equals("true"))
                    resultContainer.setSuccess(true);
                else
                    resultContainer.setSuccess(false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return resultContainer;
        }

        @Override
        protected void onPostExecute(ResultContainer<User> response) {
            // TODO: check this.exception
            // TODO: do something with the feed

            Log.i("Registry has been", " changed in PostExecute");

        }
    }

    class executeLogin extends AsyncTask<User, Void, String>{

        @Override
        protected String doInBackground(User... users) {
            ResultContainer<User> resultContainer = new ResultContainer<User>();
            User user = users[0];
            String urlString = "http://flattiserver-flattitude.rhcloud.com/flattiserver/user/login/" + user.getEmail() + "/" + user.getPassword();
            String resultToDisplay = "";
            ParseResults result = null;
            InputStream in = null;
            // HTTP Get
            Log.i("Anas", "Before HTTP");
            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoOutput(true);
                in = new BufferedInputStream(conn.getInputStream());
                Log.i("Anas", "After HTTP");

            } catch (Exception e) {
                //   System.out.println(e.getMessage());
            }
            // resultToDisplay = (String) in.toString();
            resultToDisplay = ParseResults.getStringFromInputStream(in);


            return resultToDisplay;
        }
    }

}