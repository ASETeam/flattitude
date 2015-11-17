package com.aseupc.flattitude.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.aseupc.flattitude.Models.Flat;
import com.aseupc.flattitude.R;
import com.aseupc.flattitude.databasefacade.FlatFacade;
import com.aseupc.flattitude.utility_REST.ResultContainer;

public class InvitationDetails extends AppCompatActivity {
    private TextView mTitle;
    private TextView mAddress;
    private TextView mCountry;
    private TextView mCity;
    private TextView mPostcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_details);
        String Flatname = "";
        String FlatID = "";
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                Flatname= null;
                FlatID = null;
            } else {
                Flatname= extras.getString("Flatname");
                FlatID= extras.getString("FlatId");
            }
        } else {
            Flatname= (String) savedInstanceState.getSerializable("Flatname");
            FlatID= (String) savedInstanceState.getSerializable("FlatId");
        }
        mTitle = (TextView) findViewById(R.id.flatname);
        mAddress = (TextView) findViewById(R.id.address);
        mCountry= (TextView) findViewById(R.id.country);
        mCity = (TextView) findViewById(R.id.city);
        mPostcode = (TextView) findViewById(R.id.postcode);

        ResultContainer<Flat> response =  FlatFacade.getInfo(FlatID);
        if (response.getSucces() == true) {
            Flat flat = response.getTemplate();
            mTitle.setText(Flatname);
            mAddress.setText(flat.getAddress());
            mCity.setText(flat.getCity());
            mCountry.setText(flat.getCountry());
            mPostcode.setText(flat.getPostcode());
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
}
