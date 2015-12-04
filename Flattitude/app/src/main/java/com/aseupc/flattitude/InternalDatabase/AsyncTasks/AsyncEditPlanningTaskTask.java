package com.aseupc.flattitude.InternalDatabase.AsyncTasks;

import android.os.AsyncTask;

import com.aseupc.flattitude.Activities.EditTaskActivity;
import com.aseupc.flattitude.Activities.NewTaskActivity;
import com.aseupc.flattitude.InternalDatabase.DAO.PlanningDAO;
import com.aseupc.flattitude.Models.PlanningTask;

/**
 * Created by Jordi on 23/11/2015.
 */
public class AsyncEditPlanningTaskTask extends AsyncTask<String,Integer,Void> {

    private  OnTaskEditedListener listener;
    private PlanningTask task;

    public AsyncEditPlanningTaskTask(PlanningTask task, OnTaskEditedListener listener){
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
        PlanningDAO objectDAO = new PlanningDAO(((EditTaskActivity)listener).getApplicationContext());
        long res = objectDAO.update(task);
        return null;
    }

    @Override
    public void onPostExecute(Void v){
        listener.OnEditedOnDatabase(task);
    }

    public interface OnTaskEditedListener {
        public void OnEditedOnDatabase(PlanningTask task);
    }
}
