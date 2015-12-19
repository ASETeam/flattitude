package com.aseupc.flattitude.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aseupc.flattitude.InternalDatabase.DAO.FlatDAO;
import com.aseupc.flattitude.InternalDatabase.DAO.UserDAO;
import com.aseupc.flattitude.Models.Flat;
import com.aseupc.flattitude.Models.User;
import com.aseupc.flattitude.databasefacade.FlatFacade;
import com.aseupc.flattitude.R;
import com.aseupc.flattitude.utility_REST.ResultContainer;

public class InvitationActivity extends AppCompatActivity {
    private EditText mInvitee;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_logo_app);
        setTitle("Invite people to your flat ");
        mInvitee = (EditText) findViewById(R.id.invitee);
        mInvitee.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
            mInvitee.setText("");
            }
        });
        Button mSendInvite = (Button) findViewById(R.id.send_invitation);
        mSendInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean processed = false;


                String toInvite = (String) mInvitee.getText().toString();
                String userID;
                String flatID;


                UserDAO userDAO = new UserDAO(getApplicationContext());
                User me = userDAO.getUser();
                if (me != null) {
                 userID = me.getServerid();}
                else{
                     userID = "2";
                }


                FlatDAO flatDAO = new FlatDAO(getApplicationContext());
                Flat flat = flatDAO.getFlat();
                if (flat != null)
                { flatID = flat.getServerid();
                }else {
                 flatID = "0";}


                ResultContainer<Flat> result = FlatFacade.inviteMember(userID, flatID, toInvite);
                processed = result.getSucces();


                if (processed)
                {
                    CharSequence text = "Invitation sent !";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                     toast.show();
                }
                else {
                    // CharSequence text = result.getReason.... ;
                    CharSequence text = "This user is not yet using Flattitude !";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                    toast.show();
                }

            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_invitation, menu);
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
