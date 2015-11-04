package com.aseupc.flattitude.Activities.ObjectLocation;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.DragEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.aseupc.flattitude.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class LocateObjectsActivity extends AppCompatActivity
        implements OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener,
        NewObjectFragment.OnFragmentInteractionListener
{

    private GoogleApiClient mGoogleApiClient;
    private Marker addedMarker;
    private NewObjectFragment newObjectFragment;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private FloatingActionButton addButton;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_objects);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String [] items = new String []{"Arros bullit","Arros fregit", "Pastis d'arros"};
        mDrawerLayout = (DrawerLayout) findViewById(R.id.side_drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.objects_left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, items));
        mDrawerList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                objectClicked(parent, view, position, id);
            }
        });
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);



        addButton= (FloatingActionButton) findViewById(R.id.fab);
        addButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addButtonClicked();
            }
        });

        buildGoogleApiClient();
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        //boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
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
        addButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAddObjectPressed(String name){
        addedMarker.setTitle(name);
        addedMarker.setDraggable(false);

        //Store to database

        //Synchronize with server

        newObjectFragment.reset();
        quitNewObjectFragment();
    }

    @Override
    public void onFragmentDetached(){
        quitNewObjectFragment();
    }

    @Override
    public void onCurrentLocationPressed(){
        final Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        addedMarker.setPosition(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
    }

    public void objectClicked(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int a = 0;
        a = 1;
    }

    public synchronized void addButtonClicked() {
        if(addButton.getVisibility()==View.VISIBLE) {
            setNewObjectFragment();
            MapFragment mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    final Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                            mGoogleApiClient);
                    addedMarker = googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                            .title("New object")
                            .draggable(true));
                    googleMap.setMyLocationEnabled(true);
                }
            });
        }
    }
}
