package com.aseupc.flattitude.InternalDatabase.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.aseupc.flattitude.Activities.ObjectLocation.LocateObjectsActivity;
import com.aseupc.flattitude.Activities.PlanningActivity;
import com.aseupc.flattitude.InternalDatabase.DAO.MapObjectDAO;
import com.aseupc.flattitude.InternalDatabase.DAO.PlanningDAO;
import com.aseupc.flattitude.Models.MapObject;
import com.aseupc.flattitude.Models.PlanningTask;

import java.util.List;

/**
 * Created by Jordi on 23/11/2015.
 */
public class AsyncUpdateTasks extends AsyncTask<String,Integer,Void> {

    private OnUpdateListener listener;
    List<PlanningTask> tasks;

    public AsyncUpdateTasks(List<PlanningTask> tasks, OnUpdateListener listener){
        this.listener = listener;
        this.tasks = tasks;
    }


    @Override
    protected Void doInBackground(String... params) {
        PlanningDAO DAO = new PlanningDAO(((Context)listener));
        DAO.insertOrUpdate(tasks);
        return null;
    }

    @Override
    protected void onPostExecute(Void v){
        listener.onUpdateFinished();
    }

    public interface OnUpdateListener {
        void onUpdateFinished();
    }
}
