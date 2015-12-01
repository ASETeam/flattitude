package com.aseupc.flattitude.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aseupc.flattitude.InternalDatabase.DAO.UserDAO;
import com.aseupc.flattitude.Models.Flat;
import com.aseupc.flattitude.Models.User;
import com.aseupc.flattitude.R;
import com.aseupc.flattitude.databasefacade.UserFacade;
import com.aseupc.flattitude.utility_REST.ResultContainer;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RegisteringActivity extends AppCompatActivity {

    private EditText mPasswordView;
    private EditText mFirstnameView;
    private EditText mLastnameView;
    private EditText mPhonenumberView;
    private EditText mEmailView;
    private TextView mFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registering);
        Typeface customFont = Typeface.createFromAsset(getAssets(),"Montserrat-Regular.ttf");
        TextView email_address_label = (TextView)findViewById(R.id.email_address_label);
        email_address_label.setTypeface(customFont);
        EditText email_address = (EditText)findViewById(R.id.email_address);
        email_address.setTypeface(customFont);
        TextView password_label = (TextView)findViewById(R.id.password_label);
        password_label.setTypeface(customFont);
        EditText password = (EditText)findViewById(R.id.password);
        password.setTypeface(customFont);
        TextView first_name_label = (TextView)findViewById(R.id.first_name_label);
        first_name_label.setTypeface(customFont);
        EditText first_name = (EditText)findViewById(R.id.first_name);
        first_name.setTypeface(customFont);
        TextView last_name_label = (TextView)findViewById(R.id.last_name_label);
        last_name_label.setTypeface(customFont);
        EditText last_name  = (EditText)findViewById(R.id.last_name);
        last_name.setTypeface(customFont);
        TextView phone_number_label = (TextView)findViewById(R.id.phone_number_label);
        phone_number_label.setTypeface(customFont);
        EditText phone_number = (EditText)findViewById(R.id.phone_number);
        phone_number.setTypeface(customFont);
        Button register_button = (Button)findViewById(R.id.register_button);
        register_button.setTypeface(customFont);

        mEmailView = (EditText) findViewById(R.id.email_address);
        mPasswordView = (EditText) findViewById(R.id.password);
        mFirstnameView = (EditText) findViewById(R.id.first_name);
        mLastnameView = (EditText) findViewById(R.id.last_name);
        mPhonenumberView = (EditText) findViewById(R.id.phone_number);
        mFeedback = (TextView) findViewById(R.id.feedback);

        final Button mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_address = mEmailView.getText().toString();
                String password = mPasswordView.getText().toString();
                String fname = mFirstnameView.getText().toString();
                String lname = mLastnameView.getText().toString();
                String pnumber = mPhonenumberView.getText().toString();

             /*   User user = new User(email_address, fname, lname);
                UserDAO userDAO = new UserDAO(mRegisterButton.getContext());
                userDAO.save(user);
               // Intent TestIntent = new Intent(mRegisterButton.getContext(), TestActivity.class);
              //  startActivity(TestIntent);
                User retrievedUser = userDAO.getUser();
                Log.i("RETRIEVED USER", retrievedUser.getEmail());*/

                //ResultContainer<User> result = UserFacade.registerUser(email_address, password, fname, lname, pnumber);
                User user = new User();
                user.setEmail(email_address);
                user.setFirstname(fname);
                user.setLastname(lname);
                user.setPassword(password);
                user.setPhonenbr(pnumber);
                registerUser call = new registerUser();
                call.execute(user);
              /*  if (result.getSucces() == true) {
                    UserDAO userConn = new UserDAO(mPhonenumberView.getContext());
                    userConn.save(result.getTemplate());
                    Intent valideIntent = new Intent(mPhonenumberView.getContext(), GroupActivity.class);
                    startActivity(valideIntent);
                    Log.i("Registering ", "Correct");
                } else {
                    if ((result.getReturnDescriptions() != null) && (result.getReturnDescriptions().size() > 0)) {
                        String reason = result.getReturnDescriptions().get(0);
                        mFeedback.setText(reason);
                    }

                    Log.i("Registering", "Incorrect");
                }
*/
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registering, menu);
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
/*
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }*/

    private void callbackRegister(ResultContainer<User> result){
        if (result.getSucces() == true) {
            UserDAO userConn = new UserDAO(mPhonenumberView.getContext());
            userConn.save(result.getTemplate());
            Intent valideIntent = new Intent(mPhonenumberView.getContext(), GroupActivity.class);
            startActivity(valideIntent);
            Log.i("Registering ", "Correct");
        } else {
            if ((result.getReturnDescriptions() != null) && (result.getReturnDescriptions().size() > 0)) {
                String reason = result.getReturnDescriptions().get(0);
                mFeedback.setText(reason);
                CharSequence text = reason;
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                toast.show();
            }

            Log.i("Registering", "Incorrect");
        }

    }

    public class registerUser extends AsyncTask<User, Void, ResultContainer<User>> {

        @Override
        protected ResultContainer<User> doInBackground(User... params) {
            User user = params[0];

            ResultContainer<User> result = UserFacade.registerUser(user.getEmail(), user.getPassword(), user.getFirstname(), user.getLastname(), user.getPhonenbr());


            return result;
        }

        @Override
        protected void onPostExecute(ResultContainer<User> userResultContainer) {
            super.onPostExecute(userResultContainer);
            callbackRegister(userResultContainer);
        }
    }
}