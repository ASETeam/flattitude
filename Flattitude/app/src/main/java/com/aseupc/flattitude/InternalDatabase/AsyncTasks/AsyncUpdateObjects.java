package com.aseupc.flattitude.InternalDatabase.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.aseupc.flattitude.Activities.ObjectLocation.LocateObjectsActivity;
import com.aseupc.flattitude.InternalDatabase.DAO.MapObjectDAO;
import com.aseupc.flattitude.Models.IDs;
import com.aseupc.flattitude.Models.MapObject;
import com.aseupc.flattitude.database.Map_Web_Services;
import com.aseupc.flattitude.utility_REST.ResultContainer;

import java.util.List;

/**
 * Created by Jordi on 23/11/2015.
 */
public class AsyncUpdateObjects extends AsyncTask<String,Integer,Void> {

    private OnUpdateListener listener;
    List<MapObject> objects;

    public AsyncUpdateObjects(List<MapObject> objects, OnUpdateListener listener){
        this.listener = listener;
        this.objects = objects;
    }


    @Override
    protected Void doInBackground(String... params) {
        MapObjectDAO objectDAO = new MapObjectDAO(((Context)listener));
        objectDAO.insertOrUpdate(objects);
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
