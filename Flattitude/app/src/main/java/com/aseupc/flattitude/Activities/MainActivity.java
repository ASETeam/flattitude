package com.aseupc.flattitude.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aseupc.flattitude.InternalDatabase.DAO.FlatDAO;
import com.aseupc.flattitude.InternalDatabase.DAO.UserDAO;
import com.aseupc.flattitude.Models.Flat;
import com.aseupc.flattitude.Models.User;
import com.aseupc.flattitude.R;
import com.aseupc.flattitude.databasefacade.UserFacade;
import com.aseupc.flattitude.synchronization.ChangeUI;
import com.aseupc.flattitude.synchronization.SynchzonizationService;

public class MainActivity extends AppCompatActivity {
    private User thisUser;
    private Flat thisFlat;
    public static Menu menu;
    public static Context currentContext;
    public static MenuItem mItem;

    private static final String TAG = "BroadcastTest";
    private Intent ui_intent;

    public Menu getMenu() {
        return menu;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent serviceIntent = new Intent(this, SynchzonizationService.class);
        startService(serviceIntent);
        ui_intent = new Intent(this, ChangeUI.class);
        Intent changeUIintent = new Intent(this, ChangeUI.class);
        startService(changeUIintent);

        final Button mInvite = (Button) findViewById(R.id.invite_button);
        mInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(mInvite.getContext(), InvitationActivity.class);
                startActivity(homeIntent);
            }

        });

        FlatDAO flatDAO = new FlatDAO(getApplicationContext());
        Flat flat = flatDAO.getFlat();
        UserDAO userDAO = new UserDAO(getApplicationContext());
        User user = userDAO.getUser();
        TextView mUser = (TextView) findViewById(R.id.user_id);
        TextView mFlat = (TextView) findViewById(R.id.flat_id);
        if ((flat != null) && (user != null)) {
            thisFlat = flat;
            thisUser = user;
            mUser.setText(user.getServerid() + " - " + user.getEmail() + " Token : " + user.getToken());
            mFlat.setText(flat.getServerid() + " - " + flat.getName());
        }


    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    };

    private void updateUI(Intent intent) {
        String counter = intent.getStringExtra("counter");
        String time = intent.getStringExtra("time");
        String change = intent.getStringExtra("change");
        Log.d(TAG, counter);
        Log.d(TAG, time);

        TextView txtDateTime = (TextView) findViewById(R.id.txtDateTime);
        TextView txtCounter = (TextView) findViewById(R.id.txtCounter);
        txtDateTime.setText(time);
        txtCounter.setText(counter);
        if (change.equals("yes"))
        {
            //mItem.setIcon(getApplicationContext().getDrawable(R.drawable.ic_alert));
           // menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_alert));
            menu.getItem(1).setVisible(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(ui_intent);
        registerReceiver(broadcastReceiver, new IntentFilter(ChangeUI.BROADCAST_ACTION));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.global, menu);
        this.menu = menu;
        this.mItem = menu.getItem(1);
        this.currentContext =  getApplicationContext();
        menu.getItem(1).setVisible(false);
        menu.getItem(0).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        stopService(ui_intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
                if (id == R.id.action_settings) {
            Context context = getApplicationContext();
            CharSequence text = "Settings unavailable!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        }

        if (id == R.id.action_search) {
            Context context = getApplicationContext();
            CharSequence text = "Search action!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_message));

        }

        if (id == R.id.logout)
        {
            if (thisUser != null)
            {
               // UserFacade.logoutUser(thisUser.getServerid(), thisUser.getToken());
                UserDAO userDAO = new UserDAO(getApplicationContext());
                userDAO.deleteDept(thisUser);
            }
            Intent ReturnHome = new Intent(getApplicationContext(), LandingActivity.class);
            startActivity(ReturnHome);

        }

        return super.onOptionsItemSelected(item);
    }
}
