package com.aseupc.flattitude;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aseupc.Models.Flat;
import com.aseupc.databasefacade.FlatFacade;
import com.aseupc.utility_REST.ResultContainer;

public class CreateFlat extends AppCompatActivity {

    // UI references.
    private EditText mName;
    private EditText mAddress;
    private EditText mCity;
    private EditText mPostalCode;
    private EditText mCountry;
    private EditText mIban;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_flat);
        mName = (EditText) findViewById(R.id.flat_name);
        mAddress = (EditText) findViewById(R.id.address);
        mCity = (EditText) findViewById(R.id.city);
        mPostalCode = (EditText) findViewById(R.id.postal_code);
        mCountry = (EditText) findViewById(R.id.country);
        mIban = (EditText) findViewById(R.id.iban);

        final Button mCreateFlat = (Button) findViewById(R.id.create_button);
        mCreateFlat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mName.getText().toString();
                String address = mAddress.getText().toString();
                String city = mCity.getText().toString();
                String postal_code = mPostalCode.getText().toString();
                String country  = mCountry.getText().toString();
                String iban  = mIban.getText().toString();
                Flat flat = new Flat();
                flat.setName(name);
                flat.setIban(iban);
                flat.setAddress(address);
                flat.setCity(city);
                flat.setCountry(country);
                flat.setPostcode(postal_code);
                ResultContainer<Flat> response = FlatFacade.createFlat(flat);
                if (response.getSucces() == true)
                {Context context = getApplicationContext();
                CharSequence text = "Flat created !";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();}
        }

    }); }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_flat, menu);
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
