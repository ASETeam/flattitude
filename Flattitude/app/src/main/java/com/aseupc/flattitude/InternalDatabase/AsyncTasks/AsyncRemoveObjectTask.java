package com.aseupc.flattitude.InternalDatabase.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.aseupc.flattitude.Activities.ObjectLocation.LocateObjectsActivity;
import com.aseupc.flattitude.InternalDatabase.DAO.FlatDAO;
import com.aseupc.flattitude.InternalDatabase.DAO.MapObjectDAO;
import com.aseupc.flattitude.InternalDatabase.DAO.UserDAO;
import com.aseupc.flattitude.Models.Flat;
import com.aseupc.flattitude.Models.IDs;
import com.aseupc.flattitude.Models.MapObject;
import com.aseupc.flattitude.Models.User;
import com.aseupc.flattitude.R;
import com.aseupc.flattitude.database.Map_Web_Services;
import com.aseupc.flattitude.utility_REST.ResultContainer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Jordi on 23/11/2015.
 */

public class AsyncRemoveObjectTask extends AsyncTask<String,Integer,Boolean> {

    private OnObjectRemovedListener listener;
    private MapObject object;
    ResultContainer<MapObject> res;


    public AsyncRemoveObjectTask(MapObject object, OnObjectRemovedListener listener){
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
        res = ws.ws_removeObject(object, ids.getUserId(c), ids.getUserToken(c));
    }

    @Override
    protected Boolean doInBackground(String... params) {

        if(res.getSucces()){
            //Store to database
            object = res.getTemplate();
            MapObjectDAO objectDAO = new MapObjectDAO(((LocateObjectsActivity)listener).getApplicationContext());
            objectDAO.deleteDept(object);
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void onPostExecute(Boolean b) {
        if(b)
            listener.onRemoveSuccess();
        else
            listener.onRemoveFail();
    }

    public interface OnObjectRemovedListener {
        public void onRemoveSuccess();
        public void onRemoveFail();
    }
}
