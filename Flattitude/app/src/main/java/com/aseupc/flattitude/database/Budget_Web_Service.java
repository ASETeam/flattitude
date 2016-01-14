package com.aseupc.flattitude.database;

import android.os.AsyncTask;

import com.aseupc.flattitude.Models.BudgetOperation;
import com.aseupc.flattitude.Models.BudgetOperationDBAdapter;
import com.aseupc.flattitude.utility_REST.CallAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Vavou on 13/01/2016.
 * The web services methods to access the budget data
 */
public class Budget_Web_Service {

    private static final String BASE_SERVER_URL = "https://flattiserver-flattitude.rhcloud.com/flattiserver/";
    private static final String BASE_BUDGET_URL = BASE_SERVER_URL + "budget/";
    private static final String URL_GET_BALANCES = BASE_BUDGET_URL + "balances/get/";
    private static final String URL_PUT_MONEY = BASE_BUDGET_URL + "put/";
    private static final String URL_PAY = BASE_BUDGET_URL + "pay/";
    private static final String URL_HISTORY = BASE_BUDGET_URL + "history/";

    private static final int WAITING_TIME_TO_JOIN_WS = 1000;

    public Double[] getBalances(String flatId, String userId) {
        Double [] balances = new Double[2];

        try {
            CallPostGetBalances cpgb = new CallPostGetBalances();
            String tempResp = cpgb.execute(flatId, userId).get(WAITING_TIME_TO_JOIN_WS, TimeUnit.MILLISECONDS);
            JSONObject response = new JSONObject(tempResp);

            balances[0] = Double.parseDouble(response.getString("flat_balance"));
            balances[1] = Double.parseDouble(response.getString("personal_balance"));
        }
        catch (Exception e) {
            balances = null;
            e.printStackTrace();
        }

        return balances;
    }

    /**
     * Call the WS which ad the budget operation
     * @param bo The budget operation to add
     * @return true if the operation was done, false else
     */
    public boolean addBudgetOperation(BudgetOperation bo) {
        try {
            CallPostAddOperation cppmoc = new CallPostAddOperation();
            JSONObject response = new JSONObject(cppmoc.execute(bo).get(WAITING_TIME_TO_JOIN_WS, TimeUnit.MILLISECONDS));

            return response.getBoolean("success");
        }
        catch (Exception e) {
            return false;
        }
    }

    public ArrayList<BudgetOperationDBAdapter> retrieveBudgetOperations(String flatId) {
        ArrayList<BudgetOperationDBAdapter> operations = new ArrayList<>();
        try {
            CallPostRetrieveOperations cpro = new CallPostRetrieveOperations();
            JSONObject response = new JSONObject(cpro.execute(flatId).get(WAITING_TIME_TO_JOIN_WS, TimeUnit.MILLISECONDS));

            if (response.has("success") && response.getBoolean("success") == false) {
                return null;
            }
            else {
                final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                JSONArray historicArray = response.getJSONArray("historic");
                JSONObject jsonOperation;
                String userName;
                for (int i = 0 ; i < historicArray.length() ; i++) {
                    jsonOperation = historicArray.getJSONObject(i);
                    userName = jsonOperation.getString("userName");
                    operations.add(new BudgetOperationDBAdapter(
                            jsonOperation.getInt("id"),
                            userName.length() > 0 ? userName : null,
                            jsonOperation.getInt("flatid"),
                            jsonOperation.getDouble("amount"),
                            jsonOperation.getString("description"),
                            dateFormat.parse(jsonOperation.getString("date"))));
                }
                return operations;
            }
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Class to thread the getBalances query
     */
    private class CallPostGetBalances extends AsyncTask<Object, Void, String> {

        protected String doInBackground(Object... ids) {
            HashMap<String, String> requestParameters = new HashMap<>();
            requestParameters.put("flatid", (String) ids[0]);
            requestParameters.put("userid", (String) ids[1]);
            return CallAPI.performPostCall(URL_GET_BALANCES, requestParameters);
        }
    }

    /**
     * Class to thread the put money operation
     */
    private class CallPostAddOperation extends AsyncTask<Object, Void, String> {

        protected String doInBackground(Object... ids) {
            BudgetOperation bo = (BudgetOperation) ids[0];
            HashMap<String, String> requestParameters = new HashMap<>();
            requestParameters.put("flatid", String.valueOf(bo.getFlat().getServerid()));
            requestParameters.put("userid", String.valueOf(bo.getUserSource().getServerid()));
            requestParameters.put("amount", String.valueOf(bo.getAmount()));
            requestParameters.put("description", bo.getDescription());

            return CallAPI.performPostCall(bo.getUser() != null ? URL_PUT_MONEY : URL_PAY, requestParameters);
        }
    }

    private class CallPostRetrieveOperations extends AsyncTask<Object, Void, String> {

        protected String doInBackground(Object... ids) {
            HashMap<String, String> requestParameters = new HashMap<>();
            requestParameters.put("flatid", (String) ids[0]);
            return CallAPI.performPostCall(URL_HISTORY, requestParameters);
        }
    }
}
