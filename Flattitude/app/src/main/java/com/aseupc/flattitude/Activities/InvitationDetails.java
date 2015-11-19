package com.aseupc.flattitude.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.aseupc.flattitude.Models.Flat;
import com.aseupc.flattitude.R;
import com.aseupc.flattitude.databasefacade.FlatFacade;
import com.aseupc.flattitude.utility_REST.ResultContainer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.xml.transform.Result;

public class InvitationDetails extends AppCompatActivity {
    private TextView mTitle;
    private TextView mAddress;
    private TextView mCountry;
    private TextView mCity;
    private TextView mPostcode;

    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_details);
        mProgressView = findViewById(R.id.progress_invitationdetails);
        String Flatname = "";
        String FlatID = "";
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                Flatname = null;
                FlatID = null;
            } else {
                Flatname = extras.getString("Flatname");
                FlatID = extras.getString("FlatId");
            }
        } else {
            Flatname = (String) savedInstanceState.getSerializable("Flatname");
            FlatID = (String) savedInstanceState.getSerializable("FlatId");
        }
        mTitle = (TextView) findViewById(R.id.flatname);
        mAddress = (TextView) findViewById(R.id.address);
        mCountry = (TextView) findViewById(R.id.country);
        mCity = (TextView) findViewById(R.id.city);
        mPostcode = (TextView) findViewById(R.id.postcode);
        showProgress(true);
        //  ResultContainer<Flat> response =  FlatFacade.getInfo(FlatID);
        callGetFlatInfo call = new callGetFlatInfo();
        ResultContainer<Flat> response = null;

        call.execute(FlatID);

       /* if (response.getSucces() == true) {
            Flat flat = response.getTemplate();
            mTitle.setText(Flatname);
            mAddress.setText(flat.getAddress());
            mCity.setText(flat.getCity());
            mCountry.setText(flat.getCountry());
            mPostcode.setText(flat.getPostcode());
        }
    */

        //  showProgress(false);
    }

    private void populateTextFields(ResultContainer<Flat> flatContainer)
    {
        if (flatContainer.getSucces() == true) {
            Flat flat = flatContainer.getTemplate();
            showProgress(false);
            mTitle.setText(flat.getName());
            mAddress.setText(flat.getAddress());
            mCity.setText(flat.getCity());
            mCountry.setText(flat.getCountry());
            mPostcode.setText(flat.getPostcode());
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_invitation_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    class callGetFlatInfo extends AsyncTask<String, Void, ResultContainer<Flat>> {

        @Override
        protected ResultContainer<Flat> doInBackground(String... params) {
            String flatID = params[0];
            ResultContainer<Flat> response = FlatFacade.getInfo(flatID);
            return response;
        }

        @Override
        protected void onPostExecute(ResultContainer<Flat> flatResultContainer) {
            super.onPostExecute(flatResultContainer);
            populateTextFields(flatResultContainer);
        }

    }
}