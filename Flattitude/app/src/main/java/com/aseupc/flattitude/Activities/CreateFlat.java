package com.aseupc.flattitude.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
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
import com.aseupc.flattitude.Models.IDs;
import com.aseupc.flattitude.Models.User;
import com.aseupc.flattitude.R;
import com.aseupc.flattitude.databasefacade.FlatFacade;
import com.aseupc.flattitude.utility_REST.CallAPI;
import com.aseupc.flattitude.synchronization.JabberSmackAPI;
import com.aseupc.flattitude.utility_REST.ResultContainer;

import org.w3c.dom.Text;

import dmax.dialog.SpotsDialog;

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

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_logo_app);
        SpannableString s = new SpannableString("Flattitude");
        s.setSpan(new ForegroundColorSpan(Color.rgb(33, 33, 33)),0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new TypefaceSpan("Montserrat-Bold.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        setTitle(s);




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
                UserDAO userDAO = new UserDAO(context);
                User user = userDAO.getUser();
                ResultContainer<Flat> response;
                if (CallAPI.isNetworkAvailable(context) == false)
                {
                    CallAPI.makeToast(context, "No internet connection available");
                    response =new ResultContainer<Flat>();
                    response.setSuccess(false);
                }
                else {
                    AlertDialog dialog = new SpotsDialog(context);
                    dialog.show();
                   response = FlatFacade.createFlat(flat, user);
                    dialog.hide();
                }
                if (response.getSucces() == true)
                {

                    CharSequence text = "Flat created !";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    FlatDAO flatDao = new FlatDAO(context);
                    if (flatDao.getFlat() == null)
                    flatDao.save(flat);
                   else flatDao.update(flat);
                    Intent homeIntent = new Intent(context, MainActivity.class);
                    startActivity(homeIntent);

                    connectChat call = new connectChat();
                    call.execute(flat.getName(), user.getFirstname());
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


    public class connectChat extends AsyncTask<String, Void, Void>
    {

        @Override
        protected Void doInBackground(String... params) {
            try {
                JabberSmackAPI smackChat = IDs.getInstance(context).getSmackChat(context);

                //Join to room after flat creation.
                smackChat.joinMUC(params[0], params[1]);

            } catch (Exception ex ) {
                //Log.e("CHAT ERROR", ex.getMessage());
                ex.printStackTrace();
            }
            return null;

        }
    }
}
