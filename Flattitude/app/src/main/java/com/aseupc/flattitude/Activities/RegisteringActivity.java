package com.aseupc.flattitude.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
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

import com.aseupc.flattitude.InternalDatabase.DAO.UserDAO;
import com.aseupc.flattitude.Models.User;
import com.aseupc.flattitude.R;
import com.aseupc.flattitude.databasefacade.UserFacade;
import com.aseupc.flattitude.utility_REST.ResultContainer;

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

                ResultContainer<User> result = UserFacade.registerUser(email_address, password, fname, lname, pnumber);
                if (result.getSucces() == true)
                {
                    UserDAO userConn = new UserDAO(mPhonenumberView.getContext());
                    userConn.save(result.getTemplate());
                    Intent valideIntent = new Intent(mPhonenumberView.getContext(), GroupActivity.class);
                    startActivity(valideIntent);
                    Log.i("Registering ", "Correct");
                }
                else
                {
                   if ((result.getReturnDescriptions() != null) && (result.getReturnDescriptions().size() > 0))
                   { String reason = result.getReturnDescriptions().get(0);
                    mFeedback.setText(reason); }

                    Log.i("Registering", "Incorrect");
                }

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
}
