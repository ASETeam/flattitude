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

/**
 * Created by Jordi on 23/11/2015.
 */
public class AsyncEditObjectTask extends AsyncTask<String,Integer,MapObject> {

    private OnObjectEditedListener listener;
    private MapObject newObject;
    ResultContainer<MapObject> res;


    public AsyncEditObjectTask(MapObject newObject, OnObjectEditedListener listener){
        this.newObject = newObject;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //Synchronize with server
        UserDAO uDAO = new UserDAO(((LocateObjectsActivity) listener).getApplicationContext());
        User u = uDAO.getUser();
        Map_Web_Services ws = new Map_Web_Services();
        res = ws.ws_editObject(newObject, u.getServerid(), u.getToken());
    }


    @Override
    protected MapObject doInBackground(String... params) {
        if(res.getSucces()){
            //Store to database
            newObject = res.getTemplate();
            MapObjectDAO objectDAO = new MapObjectDAO(((LocateObjectsActivity)listener).getApplicationContext());
            objectDAO.update(newObject);
            return newObject;
        }
        else{
            return null;
        }
    }

    @Override
    public void onPostExecute(MapObject o) {
        if(o != null)
            listener.onEditSuccess(o);
        else
            listener.onEditFail();
    }
    public interface OnObjectEditedListener {
        public void onEditSuccess(MapObject newObject);
        public void onEditFail();
    }
}
