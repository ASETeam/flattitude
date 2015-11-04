package com.aseupc.flattitude.Activities.ObjectLocation;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class LocateObjectsActivity extends AppCompatActivity
        implements OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener,
        NewObjectFragment.OnFragmentInteractionListener
{

    private GoogleApiClient mGoogleApiClient;
    private NewObjectFragment newObjectFragment;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    ArrayAdapter<MapObject> adapter;
    private FloatingActionButton addButton;
    private ActionBarDrawerToggle mDrawerToggle;
    private List<MapObject> objects;
    private Marker addedMarker;
    private MapFragment mapFragment;
    private boolean addObjectMenuDisplayed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_objects);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

    public synchronized void quitNewObjectFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(newObjectFragment);
        transaction.commit();
        if(addedMarker != null)
            addedMarker.remove();
        addObjectMenuDisplayed = false;
        addButton.setVisibility(View.VISIBLE);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
    }

    public void addObject(LatLng coords,String name){
        MapObject object = new MapObject();
        object.setId(0);
        object.setName(name);
        object.setCoordinates(coords.latitude, coords.longitude);
        adapter.add(object);
    }

    @Override
    public void onAddObjectConfirmed(String name) {
        addedMarker.setTitle(name);
        addObject(addedMarker.getPosition(),name);
        AsyncAddObjectTask async = new AsyncAddObjectTask();
        addedMarker = null;
        newObjectFragment.reset();
        quitNewObjectFragment();
    }

    @Override
    public void onCurrentLocationPressed(){
        final Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        addedMarker.setPosition(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
    }

    public void objectClicked(AdapterView<?> parent, View view, int position, long id) {
        mDrawerLayout.closeDrawer(Gravity.LEFT);
        MapObject o = objects.get(position);
        LatLng location = new LatLng(o.getLatitude(),o.getLongitude());
        fixMapPosition(location);
    }

    public void fixMapPosition(final LatLng location){
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        location, 13));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(location)      // Sets the center of the map to location user
                        .zoom(17)                   // Sets the zoom
                        .bearing(0)                // Sets the orientation of the camera to north
                        .build();                   // Creates a CameraPosition from the builder
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });
    }

    public synchronized void addButtonClicked() {
        if(addButton.getVisibility()==View.VISIBLE) {
            setNewObjectFragment();
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    final Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
                    addedMarker = googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                        .draggable(true));
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(Gravity.LEFT)){
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        }else if(addObjectMenuDisplayed) {
            quitNewObjectFragment();
        }
        else{
            super.onBackPressed();
        }
    }

    private class AsyncLoadingTask extends AsyncTask<Void, Integer, Boolean> {

        private LocateObjectsActivity act;
        private List<MarkerOptions> markers;

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

            mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map);

            //objects = MapObjectDAO.getObjects();
            objects = debugObjects();
            markers = new LinkedList<>();

            for(MapObject object: objects) {
                markers.add(new MarkerOptions()
                        .position(new LatLng(object.getLatitude(), object.getLongitude()))
                        .title(object.getName())
                        .draggable(true));
            }

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);

            buildGoogleApiClient();

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            addButton= (FloatingActionButton) findViewById(R.id.fab);
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
            mDrawerLayout.setDrawerListener(mDrawerToggle);

            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    googleMap.setMyLocationEnabled(true);
                    for (MarkerOptions marker : markers) {
                        googleMap.addMarker(marker);
                    }
                }
            });

        }

        List<MapObject> debugObjects(){
            int nobjects = 10;
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

    private class AsyncAddObjectTask extends AsyncTask<MapObject,Integer,Boolean>{

        @Override
        protected Boolean doInBackground(MapObject... params) {
            //Store to database

            //Synchronize with server
            return true;
        }
    }
}
