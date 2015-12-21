package com.aseupc.flattitude.InternalDatabase.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.aseupc.flattitude.Activities.ObjectLocation.LocateObjectsActivity;
import com.aseupc.flattitude.InternalDatabase.DAO.MapObjectDAO;
import com.aseupc.flattitude.Models.IDs;
import com.aseupc.flattitude.Models.MapObject;
import com.aseupc.flattitude.database.Map_Web_Services;
import com.aseupc.flattitude.utility_REST.ResultContainer;

/**
 * Created by Jordi on 23/11/2015.
 */
public class AsyncAddObjectTask extends AsyncTask<String,Integer,MapObject> {

    private  OnObjectAddedListener listener;
    private MapObject object;
    ResultContainer<MapObject> res;

    public AsyncAddObjectTask(MapObject object, OnObjectAddedListener listener){
        this.object = object;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //Synchronize with server
        Context c = (LocateObjectsActivity) listener;
        IDs ids = IDs.getInstance(c);
        Map_Web_Services ws = new Map_Web_Services();
        res = ws.ws_addObject(object, ids.getUserId(c), ids.getUserToken(c), ids.getFlatId(c));
    }

    @Override
    protected MapObject doInBackground(String... params) {

        if(res.getSucces()){
            //Store to database
            object = res.getTemplate();
            MapObjectDAO objectDAO = new MapObjectDAO(((LocateObjectsActivity)listener).getApplicationContext());
            object.setId(objectDAO.save(object));
            return object;
        }
        else{
            return null;
        }
    }

    @Override
    public void onPostExecute(MapObject o){
        if(o == null) listener.onAddFail();
        else listener.onAddSuccess(o);
    }

    public interface OnObjectAddedListener {
        public void onAddSuccess(MapObject object);
        public void onAddFail();
    }
}
