package com.aseupc.flattitude.database;
import android.os.AsyncTask;
import android.util.Log;

import com.aseupc.flattitude.Models.PlanningTask;
import com.aseupc.flattitude.utility_REST.CallAPI;
import com.aseupc.flattitude.utility_REST.ResultContainer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * Created by JordiColl on 10-11-15.
 */
public class PlanningTask_Web_Services {
    private String statusRegister;

    public String getStatusRegister() {
        return statusRegister;
    }

    public void setStatusRegister(String statusRegister) {
        this.statusRegister = statusRegister;
    }

    public void ws_addTask(PlanningTask task, String userID, String token, String flatID, AddPlanningTaskWSListener listener) {
        callCreate call = new callCreate(task,listener);
        call.execute(userID,token,flatID);
    }

    public void ws_EditTask(PlanningTask task, String userID, String token, String flatID, EditPlanningTaskWSListener listener) {
        callEdit call = new callEdit(task,listener);
        call.execute(userID,token,flatID);
    }

    public void ws_DeleteTask(PlanningTask task, String userID, String token, String flatID, DeletePlanningTaskWSListener listener) {
        callRemove call = new callRemove(task,listener);
        call.execute(userID,token,flatID);
    }

    class callCreate extends AsyncTask<String, Void, ResultContainer<PlanningTask>> {

        private PlanningTask task;
        private AddPlanningTaskWSListener listener;

        public callCreate(PlanningTask task, AddPlanningTaskWSListener listener){
            super();
            this.task = task;
            this.listener = listener;
        }

        @Override
        protected ResultContainer<PlanningTask> doInBackground(String... strings) {
            ResultContainer<PlanningTask> resultContainer = new ResultContainer<>();

            //Only for debug
            if(true) {
                resultContainer.setSuccess(true);
                Random r = new Random();
                task.setID(String.valueOf(r.nextInt(200000)));
                resultContainer.setTemplate(task);
                return resultContainer;
            }

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
            values.put("type", task.getType());
            values.put("description", task.getDescription());
            values.put("year", task.getYearString());
            values.put("month", task.getMonthString());
            values.put("day", task.getDayString());
            values.put("hour", task.getHourString());
            values.put("minute", task.getMinuteString());
            try {
                response = CallAPI.performPostCall(urlStr, values);
                JSONObject mainObject = new JSONObject(response);
                String success = mainObject.getString("success");
                Log.i("GUILLE RESPONSE", mainObject.toString());
                if (success == "true") {
                    String id = mainObject.getString("idObject");
                    resultContainer.setSuccess(true);
                    task.setID(id);
                    resultContainer.setTemplate(task);
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
             catch (Exception e) {
                e.printStackTrace();
                 resultContainer = new ResultContainer<>();
                 resultContainer.setSuccess(false);
                 resultContainer.addReason("Internal error");
             }
            Log.i("GUILLE RESPONSE", response);
            return resultContainer;
        }

        @Override
        protected void onPostExecute(ResultContainer<PlanningTask> response) {
            Log.i("Registry has been", " changed in PostExecute");
            listener.onAddWSFinished(response);
        }
    }

    class callEdit extends AsyncTask<String, Void, ResultContainer<PlanningTask>> {

        private PlanningTask task;
        private EditPlanningTaskWSListener listener;

        public callEdit(PlanningTask task, EditPlanningTaskWSListener listener){
            super();
            this.task = task;
            this.listener = listener;
        }

        @Override
        protected ResultContainer<PlanningTask> doInBackground(String... strings) {
            ResultContainer<PlanningTask> resultContainer = new ResultContainer<PlanningTask>();
            String response = "";
            String urlStr = "https://flattiserver-flattitude.rhcloud.com/flattiserver/sharedobjects/edit";
            HashMap<String, String> values = new HashMap<>();
            String userID = strings[0];
            String token = strings [1];
            values.put("userid", userID);
            values.put("token",token);
            //values.put("objectid", task.getServerId());
            //values.put("objectname", task.getName());
            //values.put("objectdescription", task.getDescription());
            //values.put("lat", String.valueOf(task.getLatitude()));
            //values.put("long", String.valueOf(task.getLongitude()));
            response = CallAPI.performPostCall(urlStr, values);

            try {
                JSONObject mainObject = new JSONObject(response);
                String success = mainObject.getString("success");
                Log.i("GUILLE RESPONSE", mainObject.toString());
                if (success == "true") {
                    resultContainer.setSuccess(true);
                    resultContainer.setTemplate(task);
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
        protected void onPostExecute(ResultContainer<PlanningTask> response) {
            Log.i("Registry has been", " changed in PostExecute");
            listener.onEditWSFinished(response);
        }
    }

    class callRemove extends AsyncTask<String, Void, ResultContainer<PlanningTask>> {

        private PlanningTask task;
        private DeletePlanningTaskWSListener listener;

        public callRemove(PlanningTask task, DeletePlanningTaskWSListener listener){
            super();
            this.task = task;
            this.listener = listener;
        }

        @Override
        protected ResultContainer<PlanningTask> doInBackground(String... strings) {
            ResultContainer<PlanningTask> resultContainer = new ResultContainer<PlanningTask>();
            String response = "";
            String urlStr = "https://flattiserver-flattitude.rhcloud.com/flattiserver/sharedobjects/delete";
            HashMap<String, String> values = new HashMap<>();
            String userID = strings[0];
            String token = strings [1];
            values.put("userid", userID);
            values.put("token",token);
            values.put("objectid", task.getID());
            response = CallAPI.performPostCall(urlStr, values);

            try {
                JSONObject mainObject = new JSONObject(response);
                String success = mainObject.getString("success");
                Log.i("GUILLE RESPONSE", mainObject.toString());
                if (success == "true") {
                    resultContainer.setSuccess(true);
                    resultContainer.setTemplate(task);
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
        protected void onPostExecute(ResultContainer<PlanningTask> response) {
            Log.i("Registry has been", " changed in PostExecute");
            listener.onDeleteWSFinished(response);
        }
    }

    public interface AddPlanningTaskWSListener {
        public void onAddWSFinished(ResultContainer<PlanningTask> result);
    }
    public interface EditPlanningTaskWSListener {
        public void onEditWSFinished(ResultContainer<PlanningTask> result);
    }
    public interface DeletePlanningTaskWSListener {
        public void onDeleteWSFinished(ResultContainer<PlanningTask> result);
    }
}