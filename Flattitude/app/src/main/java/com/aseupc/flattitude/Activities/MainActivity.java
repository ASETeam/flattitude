package com.aseupc.flattitude.Activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.aseupc.flattitude.Activities.ObjectLocation.LocateObjectsActivity;
import com.aseupc.flattitude.InternalDatabase.DAO.FlatDAO;
import com.aseupc.flattitude.InternalDatabase.DAO.NotificationsDAO;
import com.aseupc.flattitude.InternalDatabase.DAO.PlanningDAO;
import com.aseupc.flattitude.InternalDatabase.DAO.UserDAO;
import com.aseupc.flattitude.Models.Flat;
import com.aseupc.flattitude.Models.Notification;
import com.aseupc.flattitude.Models.PlanningTask;
import com.aseupc.flattitude.Models.User;
import com.aseupc.flattitude.R;
import com.aseupc.flattitude.databasefacade.UserFacade;
import com.aseupc.flattitude.synchronization.ChangeUI;
import com.aseupc.flattitude.synchronization.SynchzonizationService;
import com.aseupc.flattitude.utility_REST.ArrayAdapterWithIcon;
import com.aseupc.flattitude.utility_REST.ParseResults;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
        // ---
        NotificationsDAO notDao = new NotificationsDAO(getApplicationContext());

        for(int i=0; i < 5; i++)
        { Notification notif = new Notification();
        Random random = new Random();
        notif.setId(random.nextInt(2000000));

        notif.setType("Add");
        notif.setSeennotification(false);
        notif.setBody("This is a message posted by me");
        notDao.save(notif);
        }
        List<Notification> retrieved = notDao.getNotifications();
        for (int i = 0; i< retrieved.size(); i++)
        {
            Log.i("NOTIFICATION ", retrieved.get(i).getId() + " " + retrieved.get(i).getTime().toString());
        }
        //--

        // Random Planning activites created
        PlanningDAO plDao = new PlanningDAO(getApplicationContext());

        for (int i = 0; i < 10; i++ ) {
           Calendar Dday = Calendar.getInstance();
           Dday.set(2015, new Random().nextInt(12) + 1, new Random().nextInt(28), 12, 12, 12);
            final Calendar  Ddayx = Dday;
            PlanningTask task1 = new PlanningTask(new Random().nextInt(200000) + "", "Anas", "Jordi", "Clean kitchen", "Please do it", Ddayx);
            PlanningTask task2 = new PlanningTask(new Random().nextInt(2000000) + "", "Anas", "Jordi", "Clean kitchen", "Please do it", Ddayx);

            plDao.save(task1);
          //  plDao.save(task2);
       }
        List<PlanningTask> tasks = plDao.getPlanningTasks();
        List<PlanningTask> Gtasks = plDao.getGroupedPlanningTasks();
        Log.i("GAnas Tasks", plDao.getPlanningTasks().size() + "");
        for (PlanningTask task:tasks
             ) {
            Calendar date = task.getPlannedTime();
            String theDay = PlanningTask.getCleanDate(date);
            Log.i("GAnas indiv", task.getID() + " " + task.getDescription() + " " + theDay);

        }
        Log.i("GAnas GTasks", plDao.getGroupedPlanningTasks().size() + "");
        for (PlanningTask task:Gtasks
                ) {
            Log.i("GAnas indiv", task.getID() + " " + task.getDescription() + " " + task.getPlannedTime().getTime().toString());

        }


        Intent serviceIntent = new Intent(this, SynchzonizationService.class);
        //startService(serviceIntent);
        ui_intent = new Intent(this, ChangeUI.class);
       Intent changeUIintent = new Intent(this, ChangeUI.class);
        startService(changeUIintent);
        final ImageButton sendInvite = (ImageButton) findViewById(R.id.add_button);
        sendInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(view.getContext(), InvitationActivity.class);
                startActivity(homeIntent);
            }
        });

        final ImageButton goMap = (ImageButton) findViewById(R.id.map_button);
        goMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent MapIntent = new Intent(view.getContext(), LocateObjectsActivity.class);
                startActivity(MapIntent);
            }
        });

        final ImageButton goCalendar = (ImageButton) findViewById(R.id.calendar_button);
        goCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent CalendarIntent = new Intent(view.getContext(), PlanningActivity.class);
                startActivity(CalendarIntent);
            }
        });
        final ImageButton goGroup = (ImageButton) findViewById(R.id.leave_button);
        goGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmFireMissiles();


            }
        });




        FlatDAO flatDAO = new FlatDAO(getApplicationContext());
        Flat flat = flatDAO.getFlat();
        UserDAO userDAO = new UserDAO(getApplicationContext());
        User user = userDAO.getUser();
       /*TextView mUser = (TextView) findViewById(R.id.user_id);
        TextView mFlat = (TextView) findViewById(R.id.flat_id);*/
        getSupportActionBar().setIcon(R.drawable.logoflattitude);
        if ((flat != null) && (user != null)) {
            thisFlat = flat;
            thisUser = user;
            setTitle(user.getFirstname() + " " + user.getLastname());
          /*  mUser.setText(user.getServerid() + " - " + user.getEmail() + " Token : " + user.getToken());
            mFlat.setText(flat.getServerid() + " - " + flat.getName());*/
        }


    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
         //   updateUI(intent);
        }
    };

    private void updateUI(Intent intent) {
        String counter = intent.getStringExtra("counter");
        String time = intent.getStringExtra("time");
        String change = intent.getStringExtra("change");
        Log.d(TAG, counter);
        Log.d(TAG, time);

       /* TextView txtDateTime = (TextView) findViewById(R.id.txtDateTime);
        TextView txtCounter = (TextView) findViewById(R.id.txtCounter);
        txtDateTime.setText(time);
        txtCounter.setText(counter); */
        if (change.equals("yes"))
        {
            //mItem.setIcon(getApplicationContext().getDrawable(R.drawable.ic_alert));
           // menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_alert));
            menu.getItem(1).setVisible(true);
        }
    }

    public void confirmFireMissiles() {
        DialogFragment newFragment = new LeaveConfirmationFragment();
        newFragment.show(getSupportFragmentManager(), "confirmation");
    }
    public void showNotifications() {
        DialogFragment newFragment = new ShowNotifications();
        newFragment.show(getSupportFragmentManager(), "notifs");
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
        menu.add("User Anas");

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
        if (id == R.id.notif_received)
        {
            showNotifications();
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
                userDAO.deleteAll();
             //   Log.i("AfterDeletion", userDAO.getUser().getEmail());
                FlatDAO flatDAO = new FlatDAO(getApplicationContext());
                flatDAO.deleteAll();
                if (thisFlat != null)
                flatDAO.deleteDept(thisFlat);
            }
            UserDAO userDAO = new UserDAO(getApplicationContext());
            userDAO.deleteAll();
            FlatDAO flatDAO = new FlatDAO(getApplicationContext());
            flatDAO.deleteAll();
            Intent ReturnHome = new Intent(getApplicationContext(), LandingActivity.class);
            startActivity(ReturnHome);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        FlatDAO flatDAO = new FlatDAO(getApplicationContext());
        Flat flat = flatDAO.getFlat();
     /*   if (flat == null) //|| (thisUser == null))
        {
            Intent returnGroup = new Intent(getApplicationContext(), GroupActivity.class);
            startActivity(returnGroup);
        }
        */





    }

    public class LeaveConfirmationFragment extends DialogFragment {
        @NonNull
        @Override
            public Dialog onCreateDialog (Bundle savedInstanceState) {
                // Use the Builder class for convenient dialog construction
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to leave this flat ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FlatDAO flatDAO = new FlatDAO(getApplicationContext());
                                flatDAO.deleteDept(flatDAO.getFlat());
                                Intent homeIntent = new Intent(getApplicationContext(), GroupActivity.class);
                                startActivity(homeIntent);
                            }
                        })
                        .setNeutralButton("Test", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent homeIntent = new Intent(getApplicationContext(), GroupActivity.class);
                                startActivity(homeIntent);
                            }
                        })

                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                // Create the AlertDialog object and return it
                return builder.create();


        }
    }

    public class ShowNotifications extends DialogFragment {


        @NonNull
        @Override
        public Dialog onCreateDialog (Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            final String [] items = new String[] {"Invitation received by Anas Helalouch to join appartment", "A new event Red Party has been planned by Red John on December 15th"};
            final Integer[] icons = new Integer[] {R.drawable.ic_add, R.drawable.ic_calendar};

            NotificationsDAO notDao = new NotificationsDAO(getApplicationContext());
            List<Notification> notifs = notDao.getNotifications();
            ArrayList<String> notifbody = new ArrayList<String>();
            ArrayList<Integer> notificon = new ArrayList<Integer>();
            for(int i= 0; i < notifs.size(); i++)
            {
                notifbody.add(ParseResults.makePhrase(notifs.get(i)));
            }
            for(int i= 0; i < notifs.size(); i++)
            {
                notificon.add(notifs.get(i).getMyIcon());
            }
            int x = notifbody.size();
            String []notifArray = new String[x];
            notifArray =notifbody.toArray(notifArray);

            int y = notificon.size();
            Integer []notifIconArray = new Integer[y];
            notifIconArray =notificon.toArray(notifIconArray);
            final Integer[] a = notifIconArray;
            final String[] b = notifArray;
            ListAdapter adapter = new ArrayAdapterWithIcon(getActivity(), b, a);

            for (int i = 0; i < a.length; i++)
            {
                Log.i("Notif type", a[i].toString());
            }
            builder.setTitle("Notifications").setAdapter(adapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item ) {
                    Toast.makeText(getActivity(), "Item Selected: " + item, Toast.LENGTH_SHORT).show();
                }
            });
            // Create the AlertDialog object and return it
            return builder.create();


        }
    }

}