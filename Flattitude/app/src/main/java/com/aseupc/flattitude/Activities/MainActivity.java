package com.aseupc.flattitude.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.aseupc.flattitude.Activities.ObjectLocation.LocateObjectsActivity;
import com.aseupc.flattitude.InternalDatabase.DAO.FlatDAO;
import com.aseupc.flattitude.InternalDatabase.DAO.NotificationsDAO;
import com.aseupc.flattitude.InternalDatabase.DAO.PlanningDAO;
import com.aseupc.flattitude.InternalDatabase.DAO.UserDAO;
import com.aseupc.flattitude.Models.Flat;
import com.aseupc.flattitude.Models.IDs;
import com.aseupc.flattitude.Models.Notification;
import com.aseupc.flattitude.Models.PlanningTask;
import com.aseupc.flattitude.Models.User;
import com.aseupc.flattitude.R;
import com.aseupc.flattitude.databasefacade.UserFacade;
import com.aseupc.flattitude.synchronization.ChangeUI;
import com.aseupc.flattitude.synchronization.JabberSmackAPI;
import com.aseupc.flattitude.synchronization.SynchzonizationService;
import com.aseupc.flattitude.utility_REST.ArrayAdapterWithIcon;
import com.aseupc.flattitude.utility_REST.CallAPI;
import com.aseupc.flattitude.utility_REST.ParseResults;
import com.aseupc.flattitude.utility_REST.ResultContainer;
import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {
    private User thisUser;
    private Flat thisFlat;

    public static Menu menu;
    public static Context currentContext;
    public static MenuItem mItem;

    private static final String TAG = "BroadcastTest";
    private Intent ui_intent;
    Toolbar mToolbar;
    public PendingIntent pIntent;
    private Context context;

    public Menu getMenu() {
        return menu;
    }

    private JabberSmackAPI chatConnection;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_logo_app);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setSubtitle("A shared experience");
        SpannableString s = new SpannableString("Flattitude");
        s.setSpan(new TypefaceSpan("Montserrat-Bold.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ab.setTitle(s);
        setTitle(s);

        if (IDs.getInstance(this).getNewUser())
        {
           showTutorial();
            IDs.getInstance(this).setConfirmedUser();
        }

        // ---
        NotificationsDAO notDao = new NotificationsDAO(getApplicationContext());

        UserDAO userDAO1 = new UserDAO(context);
        User user1 = userDAO1.getUser();
        if (user1 == null)
        {
            Intent intent = new Intent(context, LandingActivity.class);
            startActivity(intent);
        }
        for(int i=0; i < 5; i++)
        { Notification notif = new Notification();
        Random random = new Random();
        notif.setId(random.nextInt(2000000));

        notif.setType("Add");
        notif.setSeennotification("false");
        notif.setBody("This is a message posted by me");
     //   notDao.save(notif);
        }
        List<Notification> retrieved = notDao.getNotifications(IDs.getInstance(context).getUserId(context));
        for (int i = 0; i< retrieved.size(); i++)
        {
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

        final ImageButton goBudget = (ImageButton) findViewById(R.id.budget_button);
        goBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent BudgetIntent = new Intent(view.getContext(), BudgetActivity.class);
                startActivity(BudgetIntent);
                CallAPI.makeToast(context, "Let's not go there. ");
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

        final ImageButton goChat = (ImageButton) findViewById(R.id.chat_button);
        goChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatIntent = new Intent(view.getContext(), ChatActivity.class);
                startActivity(chatIntent);
            }
        });




        FlatDAO flatDAO = new FlatDAO(context);
        Flat flat = flatDAO.getFlat();
        UserDAO userDAO = new UserDAO(context);
        User user = userDAO.getUser();
       /*TextView mUser = (TextView) findViewById(R.id.user_id);
        TextView mFlat = (TextView) findViewById(R.id.flat_id);*/


        if ((flat != null) && (user != null)) {
            thisFlat = flat;
            thisUser = user;
            setTitle(user.getFirstname() + " " + user.getLastname());
        }
        else {
            Intent returnGroup = new Intent(context, GroupActivity.class);
            startActivity(returnGroup);
        }


    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    };

    private void updateUI(Intent intent) {

        String change = intent.getStringExtra("change");

       /* TextView txtDateTime = (TextView) findViewById(R.id.txtDateTime);
        TextView txtCounter = (TextView) findViewById(R.id.txtCounter);
        txtDateTime.setText(time);
        txtCounter.setText(counter); */
        if (change.equals("yes"))
        {

        }
        updateNotification();

    }

    public void updateNotification()
    {
        NotificationsDAO notificationsDAO = new NotificationsDAO(context);
        IDs ids = IDs.getInstance(context);
        List<Notification> notifs= notificationsDAO.getNotifications(ids.getUserId(context));
        if (notifs.size() > 0)
        {
            menu.getItem(1).setVisible(true);
            menu.getItem(0).setVisible(false);
        }
        else
        {
            menu.getItem(1).setVisible(false);
            menu.getItem(0).setVisible(true);
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


        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.global, menu);
        this.menu = menu;
        this.currentContext =  context;
//        menu.getItem(0).setVisible(true);
        updateNotification();
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
            CharSequence text = "Settings unavailable!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        }
        if ((id == R.id.notif_received) || (id == R.id.no_notif))
        {
            NotificationsDAO notDao = new NotificationsDAO(context);
            List<Notification> nots = (List<Notification>)notDao.getNotifications(IDs.getInstance(context).getUserId(context));

            showNotifications();
        }

        if (id == R.id.logoutinmenu)
        {
            SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Log out")
                    .setContentText("Are you sure you want to log out ?")
                    .setConfirmText("Yes,get me out of here!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            if (thisUser != null) {
                                // UserFacade.logoutUser(thisUser.getServerid(), thisUser.getToken());
                                UserDAO userDAO = new UserDAO(context);
                                userDAO.deleteDept(thisUser);
                                userDAO.deleteAll();
                                FlatDAO flatDAO = new FlatDAO(context);
                                flatDAO.deleteAll();
                                if (thisFlat != null)
                                    flatDAO.deleteDept(thisFlat);
                            }
                            IDs.resetIDs();
                            UserDAO userDAO = new UserDAO(context);
                            userDAO.deleteAll();
                            FlatDAO flatDAO = new FlatDAO(context);
                            flatDAO.deleteAll();
                            NotificationsDAO notDAO = new NotificationsDAO(context);
                            //  notDAO.deleteAll();
                            Intent ReturnHome = new Intent(context, LandingActivity.class);
                            startActivity(ReturnHome);
                            finish();
                            sDialog.dismissWithAnimation();
                    }
                });
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();


        }

        if (id == R.id.help)
        {
            showTutorial();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        FlatDAO flatDAO = new FlatDAO(context);
        Flat flat = flatDAO.getFlat();
     /*   if (flat == null) //|| (thisUser == null))
        {
            Intent returnGroup = new Intent(context, GroupActivity.class);
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
                                FlatDAO flatDAO = new FlatDAO(context);
                                //flatDAO.deleteDept(flatDAO.getFlat());
                                flatDAO.deleteAll();
                                IDs ids = IDs.getInstance(context);
                                CallLeaveFlat call = new CallLeaveFlat();

                                ResultContainer<User> res = null;
                                try {
                                    res = call.execute(ids.getUserId(context), ids.getFlatId(context)).get(50000, TimeUnit.MILLISECONDS);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (TimeoutException e) {
                                    e.printStackTrace();
                                }

                                //UserFacade.quitFlat(ids.getUserId(context), ids.getFlatId(context));
                                if (res.getSucces() == true)
                                {
                                    CallAPI.makeToast(context, "You have just left the flat");
                                }
                                Intent homeIntent = new Intent(context, GroupActivity.class);
                                startActivity(homeIntent);
                            }
                        })
                        .setNeutralButton("Test", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent homeIntent = new Intent(context, GroupActivity.class);
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

    public class CallLeaveFlat extends AsyncTask<String, Void, ResultContainer<User>>
    {

        @Override
        protected ResultContainer<User> doInBackground(String... params) {
            ResultContainer<User> res;
            if (CallAPI.isNetworkAvailable(context) == false)
            {
                CallAPI.makeToast(context, "No internet connection available");
                res =new ResultContainer<User>();
                res.setSuccess(false);
            }
            else {
                res = UserFacade.quitFlat(params[0], params[1]);
            }
            return res;
        }
    }

    public  class ShowNotifications extends DialogFragment {


        @Override
        public void onStart() {
            super.onStart();


        }

        @NonNull
        @Override
        public Dialog onCreateDialog (Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

          //  final String [] items = new String[] {"Invitation received by Anas Helalouch to join appartment", "A new event Red Party has been planned by Red John on December 15th"};
          //  final Integer[] icons = new Integer[] {R.drawable.ic_add, R.drawable.ic_calendar};

            NotificationsDAO notDao = new NotificationsDAO(getContext());
            List<Notification> notifs = notDao.getNotifications(IDs.getInstance(context).getUserId(context));
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
            final List<Notification> notifsF = notifs;
            for (int i = 0; i < a.length; i++)
            {
            }
            final NotificationsDAO notificationsDAO= new NotificationsDAO(context);
            builder.setTitle("Notifications").setAdapter(adapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item ) {
                    Notification notifU =  notifsF.get(item);
                  //  Toast.makeText(getActivity(), "Item Selected: " + notifU.getAuthor(), Toast.LENGTH_SHORT).show();
               notifU.setSeennotification("true");
                  notificationsDAO.update(notifU);
                if (notifU.getType().equals("MAP"))
                {
                    Intent intent = new Intent(context, LocateObjectsActivity.class);
                    startActivity(intent);
                }
                }
            });
            // Create the AlertDialog object and return it
            return builder.create();


        }
    }

    public void showTutorial(){
        Target target_c = new ViewTarget(findViewById(R.id.chat_button));
        ShowcaseView.Builder chat_r = new ShowcaseView.Builder(this, true)
                .setTarget(target_c)
                .setContentTitle("Chat")
                .setContentText("Feel like having a talk with your flatmates ? " +
                        "Flattitude offers you the possibility to chat with them");
        chat_r.setStyle(R.style.CustomShowcaseTheme2);
        chat_r.build();
        final Activity main = this;
        chat_r.setShowcaseEventListener(new OnShowcaseEventListener() {
            @Override
            public void onShowcaseViewHide(ShowcaseView showcaseView) {
            }

            @Override
            public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                CallAPI api = new CallAPI();
                api.tutorial(main);
            }

            @Override
            public void onShowcaseViewShow(ShowcaseView showcaseView) {
            }
        });
    }

}