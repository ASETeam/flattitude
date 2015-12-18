package com.aseupc.flattitude.synchronization;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telecom.Call;
import android.util.Log;

import com.aseupc.flattitude.InternalDatabase.DAO.NotificationsDAO;
import com.aseupc.flattitude.InternalDatabase.DAO.UserDAO;
import com.aseupc.flattitude.Models.IDs;
import com.aseupc.flattitude.Models.Notification;
import com.aseupc.flattitude.Models.User;
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

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
        Log.i("Anas", "ChangeUI Service created here !  onCreate");
    }

   /* @Override
    public void onStart(Intent intent, int startId) {
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 10000); // 1 second
    }*/

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 200000); // 1 second * 1000
       // handler.postDelayed(sendUpdatesToUI, 10000); // 1 second
        Log.i("Anas", "Service started here !  onStartCommand");
        return START_STICKY;
    }



    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
           // DisplayLoggingInfo();
            SycnhronizeNotifications();
            handler.postDelayed(this, 200000); // 5 seconds
        }
    };

    private void DisplayLoggingInfo() {
        Log.d(TAG, "entered DisplayLoggingInfo");

        Log.i("KATT", "Before DAO EXECUTION");
        UserDAO userDAO = new UserDAO(getApplicationContext());
        User user1 = userDAO.getUser();
        Log.i("My User", user1.getEmail());
        Log.i("KATT", "After Execution");

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
        Log.i("AFara1", "We are in the backgroundproces");
        getNotification notifs = new getNotification();
        UserDAO dao = new UserDAO(getApplicationContext());
        User user = dao.getUser();
        try {
            ResultContainer<User> response = notifs.execute(user.getServerid()).get(500000, TimeUnit.MILLISECONDS);
            //ResultContainer<User> response =  UserFacade.getNotifications(user.getServerid());
            ArrayList<Notification> notifications = response.getTemplate().getNotifications();
            Log.i("Booba 2.5", CallAPI.printList(notifications));
            int nothing = 0;
            Log.i("AFara2", "We are in the backgroundproces");
            for (int i = 0; i < notifications.size(); i++)
            {
                Notification thisNotif = notifications.get(i);
                NotificationsDAO not_Dao = new NotificationsDAO(getApplicationContext());

                List<Notification> internal_Notifications = not_Dao.getNotifications(IDs.getInstance(getApplicationContext()).getUserId(getApplicationContext()));
                List<Notification> selectList = not_Dao.selectNotification(thisNotif.getId() + "");
                Log.i("Booba3", CallAPI.printList(selectList));
                if (selectList.size() == 0)

                {
                    thisNotif.setSeennotification("false");
                   // thisNotif.setId(new Random().nextInt(4000000));
                    thisNotif.setUser(IDs.getInstance(getApplicationContext()).getUserId(getApplicationContext()));
                    not_Dao.save(thisNotif);
                    Timestamp tstamp  = new Timestamp(System.currentTimeMillis());
                  //  UserFacade.retrievedNotifications(thisNotif.getServerID() + "", tstamp.toString());
                    nothing = 1;
                }
            }
            Log.i("AFara3", "We are After the backgroundproces");
            UserDAO userDAO = new UserDAO(getApplicationContext());

            if (nothing == 1)
            intent.putExtra("change", "yes");
            else
                intent.putExtra("change", "no");
            sendBroadcast(intent);
        }  catch (Exception e) {
            e.printStackTrace();
        //    CallAPI.makeToast(getApplicationContext(), "Time expired for request, try again");
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
            if (response.getSucces() == true)
                Log.i("Anas", "Login is successfull");
            else
            Log.i("Anas", "Login is NOT successfull");
            return response;
        }
    }
    class registerUser extends AsyncTask<User, Void, ResultContainer<User>> {

        @Override
        protected ResultContainer<User> doInBackground(User... params) {
            User_Web_Services User_Ws = new User_Web_Services();
            ResultContainer<User> response = User_Ws.ws_registerUser("Anajjs", "popo", "kokok", "okok", "okokok");
            if (response.getSucces() == true)
                Log.i("Anas", "Register is successfull");
            else { Log.i("Anas", "Register is NOT successfull");}
            return response;
        }
    }

}
