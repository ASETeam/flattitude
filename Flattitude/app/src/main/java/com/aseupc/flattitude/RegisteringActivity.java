package com.aseupc.flattitude;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.aseupc.InternalDatabase.DAO.UserDAO;
import com.aseupc.Models.User;

public class RegisteringActivity extends AppCompatActivity {

    private EditText mPasswordView;
    private EditText mFirstnameView;
    private EditText mLastnameView;
    private EditText mPhonenumberView;
    private EditText mEmailView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registering);

        mEmailView = (EditText) findViewById(R.id.email_address);
        mPasswordView = (EditText) findViewById(R.id.password);
        mFirstnameView = (EditText) findViewById(R.id.first_name);
        mLastnameView = (EditText) findViewById(R.id.last_name);
        mPhonenumberView = (EditText) findViewById(R.id.phone_number);

        final Button mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_address = mEmailView.getText().toString();
                String password = mPasswordView.getText().toString();
                String fname = mFirstnameView.getText().toString();
                String lname = mLastnameView.getText().toString();
                String pnumber = mPhonenumberView.getText().toString();

                User user = new User(email_address, fname, lname);
                UserDAO userDAO = new UserDAO(mRegisterButton.getContext());
                userDAO.save(user);
               // Intent TestIntent = new Intent(mRegisterButton.getContext(), TestActivity.class);
              //  startActivity(TestIntent);
                User retrievedUser = userDAO.getUser();
                Log.i("RETRIEVED USER", retrievedUser.getEmail());



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
}
