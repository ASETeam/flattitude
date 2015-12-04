package com.aseupc.flattitude.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aseupc.flattitude.Activities.ObjectLocation.LocateObjectsActivity;
import com.aseupc.flattitude.InternalDatabase.DAO.UserDAO;
import com.aseupc.flattitude.Models.User;
import com.aseupc.flattitude.R;
import com.aseupc.flattitude.databasefacade.UserFacade;
import com.aseupc.flattitude.utility_REST.CallAPI;
import com.aseupc.flattitude.utility_REST.ResultContainer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        Typeface customFont = Typeface.createFromAsset(getAssets(),"Montserrat-Regular.ttf");
        Button Login = (Button)findViewById(R.id.login_button);
        Login.setTypeface(customFont);
        Button Register = (Button)findViewById(R.id.register_button);
        Register.setTypeface(customFont);

        Context context = getApplicationContext();
        final UserDAO userDAO = new UserDAO(context);
        User user = userDAO.getUser();
        //User user = null;

        if (user == null) {
            Log.i("Anas", "The user has not been saved to localDB");
            Button mLoginButton = (Button) findViewById(R.id.login_button);
            Button mRegisterButton = (Button) findViewById(R.id.register_button);
            Button mSkipButton = (Button) findViewById(R.id.skip_button);
            //Button mMapButton = (Button) findViewById(R.id.map_button);
            mLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentLogin = new Intent(view.getContext(), LoginActivity.class);
                    startActivity(intentLogin);
                }
            });
            mRegisterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentRegister = new Intent(view.getContext(), RegisteringActivity.class);
                    startActivity(intentRegister);
                }
            });
            mSkipButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callGetUser call = new callGetUser();
                    ResultContainer<User> result = new ResultContainer<User>();
                    try {
                        result = call.execute("2").get(50000, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                    // User user = CallAPI.getUser("2");
                    userDAO.save(result.getTemplate());
                    Intent intentSkip = new Intent(view.getContext(), MainActivity.class);
                    startActivity(intentSkip);

                }
            });


        }
        else {
           Intent goHome = new Intent(context, MainActivity.class);
            startActivity(goHome);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_landing, menu);
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Context context = getApplicationContext();
        UserDAO userDAO = new UserDAO(context);
        User user = userDAO.getUser();
        //User user = null;
        if (user != null) {
            Intent goHome = new Intent(context, MainActivity.class);
            startActivity(goHome);
        }
    }

    public class callGetUser extends AsyncTask<String, Void, ResultContainer<User>>{

        @Override
        protected ResultContainer<User> doInBackground(String... params) {
            User user = CallAPI.getUser("2");
            ResultContainer<User> resultContainer = new ResultContainer<>();
            resultContainer.setTemplate(user);
            return resultContainer;
        }
    }

}
