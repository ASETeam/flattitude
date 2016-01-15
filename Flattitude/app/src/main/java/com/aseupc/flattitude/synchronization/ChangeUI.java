package com.aseupc.flattitude.synchronization;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telecom.Call;
import android.util.Log;

import com.aseupc.flattitude.Activities.MainActivity;
import com.aseupc.flattitude.InternalDatabase.DAO.NotificationsDAO;
import com.aseupc.flattitude.InternalDatabase.DAO.UserDAO;
import com.aseupc.flattitude.Models.IDs;
import com.aseupc.flattitude.Models.Notification;
import com.aseupc.flattitude.Models.User;
import com.aseupc.flattitude.R;
import com.aseupc.flattitude.database.Notifications_Web_Services;
import com.aseupc.flattitude.database.User_Web_Services;
import com.aseupc.flattitude.databasefacade.UserFacade;
import com.aseupc.flattitude.utility_REST.CallAPI;
import com.aseupc.flattitude.utility_REST.ResultContainer;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.xml.transform.Result;

/**
 * Created by MetzoDell on 28-10-15.
 */
public class ChangeUI extends Service {
    private static final String TAG = "BroadcastService";
    public static final String BROADCAST_ACTION = "com.websmithing.broadcasttest.displayevent";
    private final Handler handler = new Handler();
    Intent intent;

    int counter = 0;
    int timeout = 35000;

    @Override
    public void onCreate() {
        super.onCreate();
        if (CallAPI.isNetworkAvailable(this) == false)
        {
            CallAPI.makeToast(this, "No internet connection available");
            timeout = 999999999;
        }
        else
        {
            if (CallAPI.haveMobile(this))
            {timeout = 60000;}
            else
            {
                timeout = 35000;
            }
        }


        intent = new Intent(BROADCAST_ACTION);
    }

   /* @Override
    public void onStart(Intent intent, int startId) {
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 10000); // 1 second
    }*/

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, timeout); // 1 second * 1000
        return START_STICKY;
    }



    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
           // DisplayLoggingInfo();
            SycnhronizeNotifications();
            handler.postDelayed(this, timeout); // 5 seconds
        }
    };

    private void DisplayLoggingInfo() {
        Log.d(TAG, "entered DisplayLoggingInfo");

        UserDAO userDAO = new UserDAO(getApplicationContext());
        User user1 = userDAO.getUser();

        intent.putExtra("time", new Date().toLocaleString());
        intent.putExtra("counter", String.valueOf(++counter));
        User user = new User();
        user.setPassword("works");
        user.setEmail("works@lol.com");
        verifyUser call = new verifyUser();
        registerUser callReg = new registerUser();
        ResultContainer<User> response = new ResultContainer<>();
        ResultContainer<User> responseReg = new ResultContainer<>();
        try {
            response = call.execute(user).get(500000, TimeUnit.MILLISECONDS);
            responseReg = callReg.execute(user).get(5000000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        if (response.getSucces() == true) {
            intent.putExtra("change", "yes");}
        else {
            intent.putExtra("change", "no");
        }
        intent.putExtra("change", "yes");

        sendBroadcast(intent);
    }

    public void SycnhronizeNotifications(){
        if (CallAPI.isNetworkAvailable(this) == false)
        {
           CallAPI.makeToast(this, "No internet connection available");
            return;
        }


        getNotification notifs = new getNotification();
        UserDAO dao = new UserDAO(getApplicationContext());
        User user = dao.getUser();
        try {
            ResultContainer<User> response = notifs.execute(user.getServerid()).get(500000, TimeUnit.MILLISECONDS);
            //ResultContainer<User> response =  UserFacade.getNotifications(user.getServerid());
            ArrayList<Notification> notifications = response.getTemplate().getNotifications();

            int nothing = 0;

            for (int i = 0; i < notifications.size(); i++)
            {
                Notification thisNotif = notifications.get(i);
                NotificationsDAO not_Dao = new NotificationsDAO(getApplicationContext());

                List<Notification> internal_Notifications = not_Dao.getNotifications(IDs.getInstance(getApplicationContext()).getUserId(getApplicationContext()));
                List<Notification> selectList = not_Dao.selectNotification(thisNotif.getId() + "");

                if (selectList.size() == 0)

                {
                    thisNotif.setSeennotification("false");
                    thisNotif.setUser(IDs.getInstance(getApplicationContext()).getUserId(getApplicationContext()));
                    not_Dao.save(thisNotif);
                    Timestamp tstamp  = new Timestamp(System.currentTimeMillis());
                    nothing = 1;
                }
            }

            UserDAO userDAO = new UserDAO(this);


            if (nothing == 1) {
                intent.putExtra("change", "yes");

                Intent intenti = new Intent(this, MainActivity.class);
                PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intenti, 0);
                NotificationManager notificationManager = (NotificationManager)
                        getSystemService(NOTIFICATION_SERVICE);

                android.app.Notification n  = new android.app.Notification.Builder(this)
                        .setContentTitle("Flatittude")
                        .setContentText("You have unseen notifications !")
                        .setSmallIcon(R.drawable.ic_logo_app)
                        .setContentIntent(pIntent)
                        .setAutoCancel(true)
                        .setPriority(android.app.Notification.PRIORITY_HIGH)
                        .build();


                notificationManager.notify(0, n);

            }
            else
                intent.putExtra("change", "no");
            sendBroadcast(intent);
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class getNotification extends  AsyncTask<String, Void, ResultContainer<User>> {

        @Override
        protected ResultContainer<User> doInBackground(String... params) {
            ResultContainer<User> result = new Notifications_Web_Services().ws_getNotifications(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(ResultContainer<User> userResultContainer) {
            super.onPostExecute(userResultContainer);
        }
    }

    class verifyUser extends AsyncTask<User, Void, ResultContainer<User>>{

        @Override
        protected ResultContainer<User> doInBackground(User... params) {
            User_Web_Services User_Ws = new User_Web_Services();
            ResultContainer<User> response = User_Ws.ws_verifyCredentials("test@test.com", "test");
            return response;
        }
    }
    class registerUser extends AsyncTask<User, Void, ResultContainer<User>> {

        @Override
        protected ResultContainer<User> doInBackground(User... params) {
            User_Web_Services User_Ws = new User_Web_Services();
            ResultContainer<User> response = User_Ws.ws_registerUser("Anajjs", "popo", "kokok", "okok", "okokok");
            return response;
        }
    }

}
