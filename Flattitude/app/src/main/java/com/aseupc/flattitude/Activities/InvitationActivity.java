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

import com.aseupc.flattitude.InternalDatabase.DAO.UserDAO;
import com.aseupc.flattitude.Models.Flat;
import com.aseupc.flattitude.Models.User;
import com.aseupc.flattitude.R;
import com.aseupc.flattitude.databasefacade.FlatFacade;
import com.aseupc.flattitude.utility_REST.ResultContainer;

public class InvitationActivity extends AppCompatActivity {
    private EditText mInvitee;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);
        Button mSendInvite = (Button) findViewById(R.id.send_invitation);
        mSendInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInvitee = (EditText) findViewById(R.id.invitee);
                String toInvite = (String) mInvitee.getText().toString();
                int toInviteID = 0; // CREATE WS TO GET USER INFO VIA MAIL (SEE LOWER)
                int meID = 0; // Call USERDAO to get currentUSER
                /*
                UserDAO userDAO = new UserDAO(getApplicationContext());
                User me = userDAO.getUser();
                int meID = me.getId();
                ResultContainer<Flat> result = FlatFacade.inviteMember(toInviteID, meID);
                boolean processed = result.getSucces(); */

                boolean processed = false;
                if (processed)
                {
                    CharSequence text = "Invitation sent !";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                     toast.show();
                }
                else {
                    // CharSequence text = result.getReason.... ;
                    CharSequence text = "Invitation could not be sent !";
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
