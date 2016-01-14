package com.aseupc.flattitude.database;

import android.os.AsyncTask;

import com.aseupc.flattitude.utility_REST.CallAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Vavou on 13/01/2016.
 * The web services methods to access the budget data
 */
public class Budget_Web_Service {

    private static final String BASE_SERVER_URL = "https://flattiserver-flattitude.rhcloud.com/flattiserver/";
    private static final String BASE_BUDGET_URL = BASE_SERVER_URL + "budget/";
    private static final String URL_GET_BALANCES = BASE_BUDGET_URL + "get/";
    private static final String URL_PUT_MONEY = BASE_BUDGET_URL + "put/";

    public Double[] getBalances(String flatId, String userId) {
        Double [] balances = new Double[2];

        try {
            CallPostGetBalances cpgb = new CallPostGetBalances();
            String tempResp = cpgb.execute(flatId, userId).get(1000, TimeUnit.MILLISECONDS);
            JSONObject response = new JSONObject(tempResp);

            balances[0] = Double.parseDouble(response.getString("flat_balance"));
            balances[1] = Double.parseDouble(response.getString("personal_balance"));
        }
        catch (JSONException | TimeoutException | InterruptedException | ExecutionException e) {
            balances = null;
            e.printStackTrace();
        }

        return balances;
    }

    public boolean putMoneyOnCommon(String flatId, String userId, String amount, String description) {
        return true;
    }

    /**
     * Class to thread the getBalances query
     */
    private class CallPostGetBalances extends AsyncTask<Object, Void, String> {

        protected String doInBackground(Object... ids) {
            String response = "";
            HashMap<String, String> requestParameters = new HashMap<>();
            requestParameters.put("flatid", (String) ids[0]);
            requestParameters.put("userid", (String) ids[1]);

            response = CallAPI.performPostCall(URL_GET_BALANCES, requestParameters);

            return response;
        }
    }

    /**
     * Class to thread the put money operation
     */
    private class CallPostPutMoneyOnCommon extends AsyncTask<Object, Void, String> {

        protected String doInBackground(Object... ids) {
            String response = "";
            HashMap<String, String> requestParameters = new HashMap<>();
            requestParameters.put("flatid", (String) ids[0]);
            requestParameters.put("userid", (String) ids[1]);
            requestParameters.put("amount", (String) ids[1]);
            requestParameters.put("description", (String) ids[1]);

            response = CallAPI.performPostCall(URL_PUT_MONEY, requestParameters);

            return response;
        }
    }
}
