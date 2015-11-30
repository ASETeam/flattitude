package com.aseupc.flattitude.Activities.ObjectLocation;

import android.os.AsyncTask;

import com.aseupc.flattitude.InternalDatabase.DAO.FlatDAO;
import com.aseupc.flattitude.InternalDatabase.DAO.MapObjectDAO;
import com.aseupc.flattitude.InternalDatabase.DAO.UserDAO;
import com.aseupc.flattitude.Models.Flat;
import com.aseupc.flattitude.Models.MapObject;
import com.aseupc.flattitude.Models.User;
import com.aseupc.flattitude.R;
import com.aseupc.flattitude.database.Map_Web_Services;
import com.aseupc.flattitude.utility_REST.ResultContainer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;

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
        UserDAO uDAO = new UserDAO(((LocateObjectsActivity) listener).getApplicationContext());
        User u = uDAO.getUser();
        FlatDAO fDAO = new FlatDAO(((LocateObjectsActivity) listener).getApplicationContext());
        Flat f = fDAO.getFlat();
        Map_Web_Services ws = new Map_Web_Services();
        res = ws.ws_addObject(object, u.getServerid(), u.getToken(), f.getServerid());
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
