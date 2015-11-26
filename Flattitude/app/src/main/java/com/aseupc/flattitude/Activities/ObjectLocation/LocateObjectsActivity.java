package com.aseupc.flattitude.Activities.ObjectLocation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
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


public class LocateObjectsActivity extends AppCompatActivity
        implements OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener,
        NewObjectFragment.OnNewFragmentInteractionListener,
        EditObjectFragment.OnEditFragmentInteractionListener,
        AsyncAddObjectTask.OnObjectAddedListener,
        AsyncEditObjectTask.OnObjectEditedListener,
        AsyncRemoveObjectTask.OnObjectRemovedListener

{

    private static final float flagXAnchor = 0.2f;
    private static final float flagYAnchor = 1f;
    private static final float focusXAnchor = 0.5f;
    private static final float focusYAnchor = 0.5f;

    private GoogleApiClient mGoogleApiClient;
    private NewObjectFragment newObjectFragment;
    private EditObjectFragment editObjectFragment;
    private DrawerLayout mDrawerLayout;
    private View mProgressView;
    private Dialog mOverlayDialog; //display an invisible overlay dialog to prevent user interaction and pressing back
    private ArrayAdapter<MapObject> adapter;
    private Button addButton;
    private ActionBarDrawerToggle mDrawerToggle;
    private List<MapObject> objects;
    private Map<String,MapObject> markerIdToObject;
    private Map<Long,Marker> objectIdToMarker;
    private GoogleMap map;
    private Marker onWorkingMarker;
    private MapObject editionObject;
    private boolean addObjectMenuDisplayed;
    private boolean editObjectMenuDisplayed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_objects);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        markerIdToObject = new HashMap<>();
        objectIdToMarker = new HashMap<>();
        mOverlayDialog = new Dialog(this, android.R.style.Theme_Panel);
        mOverlayDialog.setCancelable(false);
        mProgressView = findViewById(R.id.progressBar);
        showProgress(true);
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

    private synchronized void setNewObjectFragment(){
        addButton.setVisibility(View.GONE);
        setMarkersDraggable(false);
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        addObjectMenuDisplayed = true;
        // Create a new Fragment to be placed in the activity layout
        if(newObjectFragment == null)
            newObjectFragment = NewObjectFragment.newInstance();
        // Add the fragment to the 'menufragment_container' FrameLayout
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.menufragment_container, newObjectFragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }

    private synchronized void setEditObjectFragment(){
        addButton.setVisibility(View.GONE);
        setMarkersDraggable(false);
        onWorkingMarker = objectIdToMarker.get(editionObject.getId());
        onWorkingMarker.setDraggable(true);
        onWorkingMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.define_location));
        onWorkingMarker.setAnchor(focusXAnchor, focusYAnchor);
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
        //transaction.addToBackStack(null);
        transaction.commit();
    }

    private synchronized void quitNewObjectFragment(){
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
        setMarkersDraggable(true);
    }

    private synchronized void quitEditObjectFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(editObjectFragment);
        transaction.commit();
        if(onWorkingMarker != null) {
            onWorkingMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.flag));
            onWorkingMarker.setAnchor(flagXAnchor, flagYAnchor);
            onWorkingMarker = null;
        }
        editObjectMenuDisplayed = false;
        addButton.setVisibility(View.VISIBLE);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        setMarkersDraggable(true);
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
    public void showObjectNotEdited()
    {
        new AlertDialog.Builder(this)
                .setTitle("Edition failed")
                .setMessage("The object couldn't be edited. Check your internet connection and try again")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void showObjectNotRemoved()
    {
        new AlertDialog.Builder(this)
                .setTitle("Deletion failed")
                .setMessage("The object couldn't be deleted. Check your internet connection and try again")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onAddObjectConfirmed(String name, String description) {
        editionObject = new MapObject(onWorkingMarker.getPosition(),name);
        editionObject.setDescription(description);
        onWorkingMarker.remove();
        onWorkingMarker = null;
        AsyncAddObjectTask async = new AsyncAddObjectTask(editionObject,this);
        async.execute();
        newObjectFragment.reset();
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        quitNewObjectFragment();
        showProgress(true);
    }

    @Override
    public void onEditObjectConfirmed(String name, String description) {
        MapObject object = new MapObject(editionObject);
        LatLng pos = onWorkingMarker.getPosition();
        object.setCoordinates(pos.latitude, pos.longitude);
        object.setName(name);
        object.setDescription(description);
        AsyncEditObjectTask async = new AsyncEditObjectTask(object,this);
        async.execute();
        onWorkingMarker = null;
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        quitEditObjectFragment();
        showProgress(true);
    }


    @Override
    public void onRemoveObjectClicked() {
        new AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Remove object")
            .setMessage("Are you sure you want to remove " + editionObject.getName() + "?")
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    removeObjectConfirmed();
                }

            })
            .setNegativeButton("No", null)
            .show();
    }

    public void removeObjectConfirmed(){
        AsyncRemoveObjectTask async = new AsyncRemoveObjectTask(editionObject,this);
        async.execute();
        onWorkingMarker = null;
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        quitEditObjectFragment();
        showProgress(true);
    }


    public void objectLongClicked(AdapterView<?> parent, View view, int position, long id) {
        mDrawerLayout.closeDrawer(Gravity.LEFT);
        editionObject = adapter.getItem(position);
        fixMapPosition(editionObject.getPosition());
        onWorkingMarker = objectIdToMarker.get(editionObject.getId());
        if(!editObjectMenuDisplayed)
            setEditObjectFragment();
    }

    public void objectClicked(AdapterView<?> parent, View view, int position, long id) {
        mDrawerLayout.closeDrawer(Gravity.LEFT);
        MapObject mo = adapter.getItem(position);
        objectIdToMarker.get(mo.getId()).showInfoWindow();
        fixMapPosition(mo.getPosition());
    }


    public void fixMapPosition(LatLng location){
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                location, 13));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(location)      // Sets the center of the map to location user
                .zoom(17)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to north
                .build();                   // Creates a CameraPosition from the builder
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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
        fixMapPosition(pos);
        onWorkingMarker = map.addMarker(new MarkerOptions()
                .position(pos)
                .draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.define_location))
                .anchor(focusXAnchor,focusYAnchor));
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(Gravity.LEFT)){
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        }else if(addObjectMenuDisplayed) {
            onWorkingMarker.remove();
            quitNewObjectFragment();
        }else if(editObjectMenuDisplayed){
            onWorkingMarker.setPosition(editionObject.getPosition());
            editionObject = null;
            quitEditObjectFragment();
        }
        else{
            super.onBackPressed();
        }
    }

    private void setMarkersDraggable(boolean draggable){
        for(Marker m : objectIdToMarker.values()){
            m.setDraggable(draggable);
        }
    }



    @Override
    public void onAddSuccess(MapObject object) {
        MapObject of = new MapObject(object);
        objects.add(of);

        Marker marker = map.addMarker(new MarkerOptions().draggable(true)
                .position(of.getPosition())
                .title(of.getName())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.flag))
                .anchor(flagXAnchor,flagYAnchor));

        markerIdToObject.put(marker.getId(), of);
        objectIdToMarker.put(of.getId(), marker);
        adapter.notifyDataSetChanged();
        showProgress(false);
        editionObject = null;

    }

    @Override
    public void onAddFail() {
        showProgress(false);
        showObjectNotAdded();
        editionObject = null;
    }

    @Override
    public void onEditSuccess(MapObject newObject) {
        Marker marker = objectIdToMarker.get(newObject.getId());
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.flag));
        marker.setAnchor(flagXAnchor, flagXAnchor);
        editionObject.copyAttributes(newObject);
        marker.setTitle(newObject.getName());
        marker.setPosition(newObject.getPosition());
        adapter.notifyDataSetChanged();
        editionObject = null;
        showProgress(false);
    }

    @Override
    public void onEditFail() {
        Marker marker = objectIdToMarker.get(editionObject.getId());
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.flag));
        marker.setAnchor(flagXAnchor,flagXAnchor);
        marker.setPosition(editionObject.getPosition());
        showProgress(false);
        showObjectNotEdited();
        editionObject = null;
    }

    @Override
    public void onRemoveSuccess() {
        Marker marker = objectIdToMarker.get(editionObject.getId());
        markerIdToObject.remove(marker.getId());
        objectIdToMarker.remove(editionObject.getId());
        adapter.remove(editionObject);
        adapter.notifyDataSetChanged();
        marker.remove();
        editionObject = null;
        showProgress(false);
    }

    @Override
    public void onRemoveFail() {
        Marker marker = objectIdToMarker.get(editionObject.getId());
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.flag));
        marker.setAnchor(flagXAnchor, flagXAnchor);
        marker.setPosition(editionObject.getPosition());
        showProgress(false);
        editionObject = null;
        showObjectNotRemoved();
    }

    private class AsyncLoadingTask extends AsyncTask<Void, Integer, Boolean> {

        private LocateObjectsActivity act;
        private ListView mDrawerList;
        private List<MarkerOptions> markerOptions;
        private MapFragment mapFragment;

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
            markerOptions = new LinkedList<>();


            for(MapObject object: objects) {
                markerOptions.add(new MarkerOptions()
                        .position(object.getPosition())
                        .title(object.getName())
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.flag))
                        .anchor(flagXAnchor,flagYAnchor));
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
                    objectLongClicked(parent, view, position, id);
                    return true;
                }
            });
            mDrawerLayout.setDrawerListener(mDrawerToggle);

            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    map = googleMap;
                    map.setMyLocationEnabled(true);
                    int i = 0;
                    for (MarkerOptions markerOp : markerOptions) {
                        Marker marker = googleMap.addMarker(markerOp);
                        MapObject mo = objects.get(i);
                        objectIdToMarker.put(mo.getId(), marker);
                        markerIdToObject.put(marker.getId(), mo);
                        i++;
                    }
                    map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                        @Override
                        public void onMapLongClick(LatLng latLng) {
                            if (addObjectMenuDisplayed || editObjectMenuDisplayed) {
                                onWorkingMarker.setPosition(latLng);
                            } else {
                                setNewObjectFragment();
                                addMarker(latLng);
                            }
                        }
                    });
                    map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                        @Override
                        public void onMarkerDragStart(Marker marker) {
                            if (!addObjectMenuDisplayed && !editObjectMenuDisplayed) {
                                editionObject = markerIdToObject.get(marker.getId());
                                setEditObjectFragment();
                            }
                        }

                        @Override
                        public void onMarkerDrag(Marker marker) {

                        }

                        @Override
                        public void onMarkerDragEnd(Marker marker) {

                        }
                    });
                    map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                        @Override
                        public boolean onMyLocationButtonClick() {
                            if (addObjectMenuDisplayed || editObjectMenuDisplayed) {
                                Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                                        mGoogleApiClient);
                                LatLng pos = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                                onWorkingMarker.setPosition(pos);
                            }
                            return false;
                        }
                    });
                }
            });
            showProgress(false);
        }
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.

        if(show) mOverlayDialog.show();
        else mOverlayDialog.hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
}
