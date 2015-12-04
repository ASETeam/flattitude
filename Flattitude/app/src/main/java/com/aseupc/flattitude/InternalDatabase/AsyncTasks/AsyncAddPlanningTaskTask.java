package com.aseupc.flattitude.InternalDatabase.AsyncTasks;

import android.os.AsyncTask;

import com.aseupc.flattitude.Activities.NewTaskActivity;
import com.aseupc.flattitude.InternalDatabase.DAO.PlanningDAO;
import com.aseupc.flattitude.Models.PlanningTask;

/**
 * Created by Jordi on 23/11/2015.
 */
public class AsyncAddPlanningTaskTask extends AsyncTask<String,Integer,Void> {

    private  OnTaskAddedListener listener;
    private PlanningTask task;

    public AsyncAddPlanningTaskTask(PlanningTask task, OnTaskAddedListener listener){
        this.task = task;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... params) {
        //Store to database
        PlanningDAO objectDAO = new PlanningDAO(((NewTaskActivity)listener).getApplicationContext());
        objectDAO.save(task);
        return null;
    }

    @Override
    public void onPostExecute(Void v){
        listener.OnAddedToDatabase(task);
    }

    public interface OnTaskAddedListener {
        public void OnAddedToDatabase(PlanningTask task);
    }
}
