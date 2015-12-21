package com.aseupc.flattitude.database;
import android.os.AsyncTask;
import android.util.Log;

import com.aseupc.flattitude.Models.PlanningTask;
import com.aseupc.flattitude.utility_REST.CallAPI;
import com.aseupc.flattitude.utility_REST.ParseResults;
import com.aseupc.flattitude.utility_REST.ResultContainer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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

    public void ws_editTask(PlanningTask task, String userID, String token, String flatID, EditPlanningTaskWSListener listener) {
        callEdit call = new callEdit(task,listener);
        call.execute(userID,token,flatID);
    }

    public void ws_deleteTask(PlanningTask task, String userID, String token, String flatID, DeletePlanningTaskWSListener listener) {
        callRemove call = new callRemove(task,listener);
        call.execute(userID,token,flatID);
    }

    public void ws_getTasks(String userID, String token, String flatID, GetTasksWSListener listener) {
        callGet call = new callGet(listener);
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

            String response = "";
            String urlStr = "https://flattiserver-flattitude.rhcloud.com/flattiserver/tasks/create";
            HashMap<String, String> values = new HashMap<>();
            String userID = strings[0];
            String token = strings [1];
            String flatID = strings[2];
            values.put("userid", userID);
            values.put("flatid", flatID);
            values.put("token",token);
            values.put("type", String.valueOf(task.getTypeId()));
            values.put("description", task.getDescription());
            values.put("year", task.getYearString());
            values.put("month", task.getMonthString());
            values.put("day", task.getDayString());
            values.put("hour", task.getHourString());
            values.put("minute", task.getMinuteString());
            values.put("duration","10");
            try {
                response = CallAPI.performPostCall(urlStr, values);
                JSONObject mainObject = new JSONObject(response);
                String success = mainObject.getString("success");
                Log.i("GUILLE RESPONSE", mainObject.toString());
                if (success == "true") {
                    String id = mainObject.getString("idTask");
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
            String urlStr = "https://flattiserver-flattitude.rhcloud.com/flattiserver/tasks/edit";
            HashMap<String, String> values = new HashMap<>();
            String userID = strings[0];
            String token = strings [1];
            values.put("userid", userID);
            values.put("token",token);
            values.put("taskid",task.getID());
            values.put("type", String.valueOf(task.getTypeId()));
            values.put("description", task.getDescription());
            values.put("year", task.getYearString());
            values.put("month", task.getMonthString());
            values.put("day", task.getDayString());
            values.put("hour", task.getHourString());
            values.put("minute", task.getMinuteString());
            values.put("duration","10");
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
            String urlStr = "https://flattiserver-flattitude.rhcloud.com/flattiserver/tasks/delete/";
            urlStr = urlStr + task.getID();

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
                    resultContainer.setTemplate(task);
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
        protected void onPostExecute(ResultContainer<PlanningTask> response) {
            Log.i("Registry has been", " changed in PostExecute");
            listener.onDeleteWSFinished(response);
        }
    }

    class callGet extends AsyncTask<String, Void, ResultContainer<List<PlanningTask>>> {

        private GetTasksWSListener listener;

        public callGet(GetTasksWSListener listener) {
            super();
            this.listener = listener;
        }

        @Override
        protected ResultContainer<List<PlanningTask>> doInBackground(String... strings) {
            ResultContainer<List<PlanningTask>> resultContainer = new ResultContainer<>();
            String urlStr = "https://flattiserver-flattitude.rhcloud.com/flattiserver/tasks/get/";
            String flatId = strings[2];
            urlStr = urlStr + flatId;

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

                if (success == "true") {
                    resultContainer.setSuccess(true);
                    List<PlanningTask> tasks = new LinkedList<>();
                    JSONArray tasksjson = mainObject.getJSONArray("tasks");
                    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    for(int i = 0; i < tasksjson.length(); i++){
                        JSONObject taskjson = tasksjson.getJSONObject(i);
                        PlanningTask task = new PlanningTask();
                        task.setID(taskjson.getString("id"));
                        task.setType(taskjson.getInt("type"));
                        task.setDescription(taskjson.getString("description"));
                        task.setAuthor(taskjson.getString("author"));
                        String datetime = taskjson.getString("date").split("\\.")[0];
                        Calendar c = Calendar.getInstance();
                        c.setTime(f.parse(datetime));
                        task.setPlannedTime(c);
                        tasks.add(task);
                    }

                    resultContainer.setTemplate(tasks);
                } else if (success == "false") {
                    resultContainer.setSuccess(false);
                    String reason = mainObject.getString("reason");
                    resultContainer.addReason(reason);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                resultContainer.setSuccess(false);
                resultContainer.addReason("Internal error");
            }
            catch (ParseException e){
                e.printStackTrace();
                resultContainer.setSuccess(false);
                resultContainer.addReason("Internal error");
            }
            return resultContainer;
        }

        @Override
        protected void onPostExecute(ResultContainer<List<PlanningTask>> response) {
            Log.i("Registry has been", " changed in PostExecute");
            listener.onGetWSFinished(response);
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
    public interface GetTasksWSListener {
        void onGetWSFinished(ResultContainer<List<PlanningTask>> result);
    }
}