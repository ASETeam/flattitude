package com.aseupc.flattitude.Activities.ObjectLocation;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

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
        MainActionsFragment.OnFragmentInteractionListener,
        NewObjectFragment.OnFragmentInteractionListener
{

    private GoogleApiClient mGoogleApiClient;
    private Marker addedMarker;
    private MainActionsFragment  mainActionsFragment;
    private NewObjectFragment newObjectFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_objects);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

        // However, if we're being restored from a previous state,
        // then we don't need to do anything and should return or else
        // we could end up with overlapping fragments.
        if (savedInstanceState == null) {
            // Create a new Fragment to be placed in the activity layout
            mainActionsFragment = MainActionsFragment.newInstance();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            mainActionsFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.menufragment_container, mainActionsFragment).commit();
        }
    }

    protected synchronized void buildGoogleApiClient() {

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


    public void setMenuFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.menufragment_container, mainActionsFragment);
        transaction.commit();
    }


    public void setNewObjectFragment(){
        // Create a new Fragment to be placed in the activity layout
        if(newObjectFragment == null)
            newObjectFragment = NewObjectFragment.newInstance();
        // Add the fragment to the 'menufragment_container' FrameLayout
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.menufragment_container, newObjectFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onNewObjectPressed(){

        setNewObjectFragment();

        final Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                addedMarker = googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                        .title("New object")
                        .draggable(true));
                googleMap.setMyLocationEnabled(true);
            }
        });
    }

    @Override
    public void onAddObjectPressed(String name){
        addedMarker.setTitle(name);
        addedMarker.setDraggable(false);

        //Store to database

        //Synchronize with server

        newObjectFragment.reset();
        setMenuFragment();
    }

    @Override
    public void onCurrentLocationPressed(){
        final Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        addedMarker.setPosition(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
    }
}
