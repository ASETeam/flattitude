package com.aseupc.flattitude.InternalDatabase.AsyncTasks;

import android.os.AsyncTask;

import com.aseupc.flattitude.Activities.ObjectLocation.LocateObjectsActivity;
import com.aseupc.flattitude.Activities.PlanningActivity;
import com.aseupc.flattitude.InternalDatabase.DAO.MapObjectDAO;
import com.aseupc.flattitude.InternalDatabase.DAO.PlanningDAO;
import com.aseupc.flattitude.InternalDatabase.DAO.UserDAO;
import com.aseupc.flattitude.Models.MapObject;
import com.aseupc.flattitude.Models.PlanningTask;
import com.aseupc.flattitude.Models.User;
import com.aseupc.flattitude.database.Map_Web_Services;
import com.aseupc.flattitude.utility_REST.ResultContainer;

/**
 * Created by Jordi on 23/11/2015.
 */

public class AsyncRemovePlanningTaskTask extends AsyncTask<String,Integer,Boolean> {

    private OnTaskRemovedListener listener;
    private PlanningTask task;

    public AsyncRemovePlanningTaskTask(PlanningTask task, OnTaskRemovedListener listener){
        this.task = task;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Boolean doInBackground(String... params) {

        PlanningDAO objectDAO = new PlanningDAO(((PlanningActivity)listener).getApplicationContext());
        objectDAO.deleteDept(task);
        return true;
    }

    @Override
    public void onPostExecute(Boolean b) {
        listener.onRemoved(task);
    }

    public interface OnTaskRemovedListener {
        public void onRemoved(PlanningTask task);
    }
}
