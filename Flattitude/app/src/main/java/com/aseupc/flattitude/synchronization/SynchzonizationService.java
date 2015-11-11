package com.aseupc.flattitude.synchronization;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.aseupc.flattitude.Models.User;
import com.aseupc.flattitude.database.User_Web_Services;
import com.aseupc.flattitude.utility_REST.ResultContainer;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by MetzoDell on 27-10-15.
 */
public class SynchzonizationService extends Service {
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

    @Override
    public void onStart(Intent intent, int startId) {
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 100000); // 1 second
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 100000); // 1 second
        Log.i("Anas", "Service started here !  onStartCommand");
        return START_STICKY;
    }



    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            DisplayLoggingInfo();
            handler.postDelayed(this, 5000000); // 5 seconds
        }
    };

    private void DisplayLoggingInfo() {
        Log.d(TAG, "entered DisplayLoggingInfo");

        intent.putExtra("time", new Date().toLocaleString());
        intent.putExtra("counter", String.valueOf(++counter));
        User user = new User();
        user.setPassword("works");
        user.setEmail("works@lol.com");
        verifyUser call = new verifyUser();
        ResultContainer<User> response = new ResultContainer<>();
        try {
            response = call.execute(user).get(50000000, TimeUnit.MILLISECONDS);
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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    class verifyUser extends AsyncTask<User, Void, ResultContainer<User>> {

        @Override
        protected ResultContainer<User> doInBackground(User... params) {
            User_Web_Services User_Ws = new User_Web_Services();
            ResultContainer<User> response = User_Ws.ws_verifyCredentials("po2@po.com", "popo");
            if (response.getSucces() == true)
                Log.i("Anas", "Login is successfull");
            Log.i("Anas", "Login is NOT successfull");
            return response;
        }
    }

    class registerUser extends AsyncTask<User, Void, ResultContainer<User>> {

        @Override
        protected ResultContainer<User> doInBackground(User... params) {
            User_Web_Services User_Ws = new User_Web_Services();
            ResultContainer<User> response = User_Ws.ws_registerUser("Anas", "popo", "kokok", "okok","okokok");
            if (response.getSucces() == true)
                Log.i("Anas", "Login is successfull");
           else { Log.i("Anas", "Login is NOT successfull");}
            return response;
        }
    }



}
