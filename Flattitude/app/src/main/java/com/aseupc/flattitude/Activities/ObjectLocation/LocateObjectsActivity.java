package com.aseupc.flattitude.Activities.ObjectLocation;

import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.aseupc.flattitude.InternalDatabase.DAO.MapObjectDAO;
import com.aseupc.flattitude.Models.MapObject;
import com.aseupc.flattitude.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class LocateObjectsActivity extends AppCompatActivity
        implements OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener,
        NewObjectFragment.OnNewFragmentInteractionListener,
        EditObjectFragment.OnEditFragmentInteractionListener
{

    private GoogleApiClient mGoogleApiClient;
    private NewObjectFragment newObjectFragment;
    private EditObjectFragment editObjectFragment;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ArrayAdapter<MapObject> adapter;
    private Button addButton;
    private ActionBarDrawerToggle mDrawerToggle;
    private List<MapObject> objects;
    private Map<Long,Marker> markersMap;
    private Map<String,MapObject> mapObjectsMap;
    private Marker onWorkingMarker;
    private MapObject editionObject;
    private MapFragment mapFragment;
    private boolean addObjectMenuDisplayed;
    private boolean editObjectMenuDisplayed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_objects);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mapObjectsMap = new HashMap<>();
        markersMap = new HashMap<>();
        AsyncLoadingTask load = new AsyncLoadingTask(this);
        load.execute((Void) null);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public void onConnected(Bundle bundle) {


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public synchronized void setNewObjectFragment(){
        addButton.setVisibility(View.GONE);
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        addObjectMenuDisplayed = true;
        // Create a new Fragment to be placed in the activity layout
        if(newObjectFragment == null)
            newObjectFragment = NewObjectFragment.newInstance();
        // Add the fragment to the 'menufragment_container' FrameLayout
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.menufragment_container, newObjectFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public synchronized void setEditObjectFragment(){
        addButton.setVisibility(View.GONE);
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        editObjectMenuDisplayed = true;
        // Create a new Fragment to be placed in the activity layout
        if(editObjectFragment == null)
            editObjectFragment = EditObjectFragment.newInstance();
        // Add the fragment to the 'menufragment_container' FrameLayout
        editObjectFragment.setEditedObject(editionObject);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.menufragment_container, editObjectFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public synchronized void quitNewObjectFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(newObjectFragment);
        transaction.commit();
        if(onWorkingMarker != null) {
            onWorkingMarker.remove();
            onWorkingMarker = null;
        }
        addObjectMenuDisplayed = false;
        addButton.setVisibility(View.VISIBLE);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
    }

    public synchronized void quitEditObjectFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(editObjectFragment);
        transaction.commit();
        if(onWorkingMarker != null) {
            onWorkingMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.flag));
            onWorkingMarker = null;
        }
        editObjectMenuDisplayed = false;
        addButton.setVisibility(View.VISIBLE);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
    }

    public void showObjectNotAdded()
    {
        new AlertDialog.Builder(this)
                .setTitle("Addition failed")
                .setMessage("The object couldn't be added. Check your internet connection and try again")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onAddObjectConfirmed(String name, String description) {
        MapObject object = new MapObject(onWorkingMarker.getPosition(),name);
        object.setDescription(description);
        onWorkingMarker.remove();
        onWorkingMarker = null;
        AsyncAddObjectTask async = new AsyncAddObjectTask(object);
        async.execute();
        newObjectFragment.reset();
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        quitNewObjectFragment();
    }

    @Override
    public void onEditObjectConfirmed(String name, String description) {
        MapObject object = new MapObject(onWorkingMarker.getPosition(),name);
        object.setDescription(description);
        AsyncAddObjectTask async = new AsyncAddObjectTask(object);
        async.execute();
        onWorkingMarker.setTitle(name);
        onWorkingMarker = null;
        editionObject.copyAttributes(object);
        adapter.notifyDataSetChanged();
        editObjectFragment.reset();
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        quitEditObjectFragment();
    }

    @Override
    public void onEditCurrentLocationPressed(){
        currentLocationPressed();
    }

    @Override
    public void onCurrentLocationPressed(){
        currentLocationPressed();;
    }

    public void currentLocationPressed(){
        final Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        onWorkingMarker.setPosition(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
    }

    public void objectClicked(AdapterView<?> parent, View view, int position, long id) {
        mDrawerLayout.closeDrawer(Gravity.LEFT);
        editionObject = objects.get(position);
        LatLng location = new LatLng(editionObject.getLatitude(),editionObject.getLongitude());
        fixMapPosition(location);
        onWorkingMarker = markersMap.get(id);
        if(!editObjectMenuDisplayed) {
            setEditObjectFragment();
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            final LatLng pos = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            addMarker(pos);
        }

    }

    public void fixMapPosition(final LatLng location){
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                fixMapPosition(location, googleMap);
            }
        });
    }

    public void fixMapPosition(final LatLng location, GoogleMap googleMap){
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                location, 13));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(location)      // Sets the center of the map to location user
                .zoom(17)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to north
                .build();                   // Creates a CameraPosition from the builder
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public synchronized void addButtonClicked() {
        if(!addObjectMenuDisplayed) {
            setNewObjectFragment();
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            final LatLng pos = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            addMarker(pos);
        }
    }

    public void addMarker(final LatLng pos){
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                fixMapPosition(pos, googleMap);
                onWorkingMarker = googleMap.addMarker(new MarkerOptions()
                        .position(pos)
                        .draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.define_location)));
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(Gravity.LEFT)){
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        }else if(addObjectMenuDisplayed) {
            quitNewObjectFragment();
        } if(editObjectMenuDisplayed){
            quitEditObjectFragment();
        }
        else{
            super.onBackPressed();
        }
    }

    private class AsyncLoadingTask extends AsyncTask<Void, Integer, Boolean> {

        private LocateObjectsActivity act;
        private List<MarkerOptions> markerOptions;

        public AsyncLoadingTask(LocateObjectsActivity act){
            super();
            this.act = act;
        }

        @Override
        protected  void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.side_drawer_layout);
            mDrawerList = (ListView) findViewById(R.id.objects_left_drawer);
            mDrawerToggle = new ActionBarDrawerToggle(act, mDrawerLayout,
                    toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {};
            addObjectMenuDisplayed = false;
            editObjectMenuDisplayed = false;

            mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map);

            MapObjectDAO objectDAO = new MapObjectDAO(getApplicationContext());
            objects = objectDAO.getMapObjects();
            objects.addAll(debugObjects());
            markerOptions = new LinkedList<>();


            for(MapObject object: objects) {
                markerOptions.add(new MarkerOptions()
                        .position(new LatLng(object.getLatitude(), object.getLongitude()))
                        .title(object.getName())
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.flag)));
            }

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);

            buildGoogleApiClient();

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            addButton = (Button) findViewById(R.id.add_button);
            addButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    addButtonClicked();
                }
            });

            adapter = new ArrayAdapter<>(act,
                    R.layout.drawer_list_item, objects);
            mDrawerList.setAdapter(adapter);
            mDrawerList.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    objectClicked(parent, view, position, id);
                }
            });
            mDrawerList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    return false;
                }
            });
            mDrawerLayout.setDrawerListener(mDrawerToggle);



            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    googleMap.setMyLocationEnabled(true);
                    int i = 0;
                    for (MarkerOptions markerOp : markerOptions) {
                        Marker marker = googleMap.addMarker(markerOp);
                        markersMap.put(adapter.getItemId(i),marker);
                        mapObjectsMap.put(marker.getId(),objects.get(i));
                        i++;
                    }
                    googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                        @Override
                        public void onMapLongClick(LatLng latLng) {
                            final LatLng pos = new LatLng(latLng.latitude, latLng.longitude);
                            if(addObjectMenuDisplayed){
                                onWorkingMarker.setPosition(pos);
                            }
                            else{
                                setNewObjectFragment();
                                addMarker(pos);
                            }
                        }
                    });
                }
            });
        }

        List<MapObject> debugObjects(){
            int nobjects = 5;
            Random r = new Random();
            List<MapObject> objects = new LinkedList<>();
            for(int i = 0; i < nobjects; i++){
                MapObject object = new MapObject();
                object.setId(i);
                object.setName("Object " + (i + 1));
                object.setDescription("Lorem ipsum dolor sit amet, " +
                        "consectetur adipiscing elit, sed do eiusmod tempor " +
                        "incididunt ut labore et dolore magna aliqua");
                double lat = r.nextDouble()*170 - 85;
                double lon = r.nextDouble()*360 - 180;
                object.setCoordinates(lat,lon);
                objects.add(object);
            }
            return objects;
        }
    }

    private class AsyncAddObjectTask extends AsyncTask<String,Integer,MapObject>{

        private MapObject o;

        public AsyncAddObjectTask(MapObject object){
            this.o = object;
        }

        @Override
        protected MapObject doInBackground(String... params) {

            //-----Test-------
            o.setServerId("123");
            MapObjectDAO objectDAO = new MapObjectDAO(getApplicationContext());
            objectDAO.save(o);
            return o;
            //----------------

/*
            //Synchronize with server
            MapObject o = params[0];
            UserDAO uDAO = new UserDAO(getApplicationContext());
            User u = uDAO.getUser();
            FlatDAO fDAO = new FlatDAO(getApplicationContext());
            Flat f = fDAO.getFlat();
            Map_Web_Services ws = new Map_Web_Services();
            ResultContainer<MapObject> res;
            res = ws.ws_addObject(o, u.getServerid(), u.getToken(), f.getServerid());

            if(res.getSucces()){
                //Store to database
                o = res.getTemplate();
                MapObjectDAO objectDAO = new MapObjectDAO(getApplicationContext());
                objectDAO.save(o);
                return o;
            }
            else{
                return null;
            }*/
        }

        @Override
        public void onPostExecute(MapObject o){
            if(o == null){
                //Notify that object couldn't be added
                showObjectNotAdded();
            }
            else{
                //Add to the map
                final MapObject of = new MapObject(o);
                objects.add(of);

                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        Marker marker = googleMap.addMarker(new MarkerOptions().draggable(true)
                                .position(new LatLng(of.getLatitude(), of.getLongitude()))
                                .title(of.getName())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.flag)));
                        mapObjectsMap.put(marker.getId(),of);
                        markersMap.put(adapter.getItemId(adapter.getCount()-1),marker);
                    }
                });
            }
        }
    }
}
