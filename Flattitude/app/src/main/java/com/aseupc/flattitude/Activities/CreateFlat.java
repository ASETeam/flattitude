package com.aseupc.flattitude.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aseupc.flattitude.InternalDatabase.DAO.FlatDAO;
import com.aseupc.flattitude.InternalDatabase.DAO.UserDAO;
import com.aseupc.flattitude.Models.Flat;
import com.aseupc.flattitude.Models.User;
import com.aseupc.flattitude.R;
import com.aseupc.flattitude.databasefacade.FlatFacade;
import com.aseupc.flattitude.utility_REST.CallAPI;
import com.aseupc.flattitude.utility_REST.ResultContainer;

import org.w3c.dom.Text;

public class CreateFlat extends AppCompatActivity {
//nothing here
    // UI references.
    private EditText mName;
    private EditText mAddress;
    private EditText mCity;
    private EditText mPostalCode;
    private EditText mCountry;
    private EditText mIban;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        //customized fonts:
        Typeface customFontButton = Typeface.createFromAsset(getAssets(),"Montserrat-Regular.ttf");
        Typeface customFont = Typeface.createFromAsset(getAssets(),"Quicksand_Book.otf");
        TextView flat_name_label = (TextView)findViewById(R.id.flat_name_label);
//        flat_name_label.setTypeface(customFont);
        EditText flat_name = (EditText)findViewById(R.id.flat_name);
//        flat_name.setTypeface(customFont);
        TextView address_label = (TextView)findViewById(R.id.address_label);
  //      address_label.setTypeface(customFont);
        EditText address = (EditText)findViewById(R.id.address);
    //    address.setTypeface(customFont);
        TextView city_label = (TextView)findViewById(R.id.city_label);
      //  city_label.setTypeface(customFont);
        EditText city = (EditText)findViewById(R.id.city);
       // city.setTypeface(customFont);
        TextView postal_code_label = (TextView)findViewById(R.id.postal_code_label);
       // postal_code_label.setTypeface(customFont);
        EditText postal_code = (EditText)findViewById(R.id.postal_code);
       // postal_code.setTypeface(customFont);
        TextView country_label = (TextView)findViewById(R.id.country_label);
       // country_label.setTypeface(customFont);
        EditText country = (EditText)findViewById(R.id.country);
       // country.setTypeface(customFont);
        TextView iban_label = (TextView)findViewById(R.id.iban_label);
       // iban_label.setTypeface(customFont);
        EditText iban = (EditText)findViewById(R.id.iban);
        Button create_button = (Button)findViewById(R.id.create_button);
       // create_button.setTypeface(customFontButton);




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
                UserDAO userDAO = new UserDAO(getApplicationContext());
                User user = userDAO.getUser();
                ResultContainer<Flat> response;
                if (CallAPI.isNetworkAvailable(context) == false)
                {
                    CallAPI.makeToast(context, "No internet connection available");
                    response =new ResultContainer<Flat>();
                    response.setSuccess(false);
                }
                else {
                   response = FlatFacade.createFlat(flat, user);
                }
                if (response.getSucces() == true)
                {
                    Context context = getApplicationContext();
                    CharSequence text = "Flat created !";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    FlatDAO flatDao = new FlatDAO(getApplicationContext());
                    if (flatDao.getFlat() == null)
                    flatDao.save(flat);
                   else flatDao.update(flat);
                    Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(homeIntent);
                }
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
