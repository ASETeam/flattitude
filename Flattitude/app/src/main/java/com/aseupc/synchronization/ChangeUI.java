package com.aseupc.synchronization;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.aseupc.Models.User;
import com.aseupc.database.User_Web_Services;
import com.aseupc.utility_REST.ResultContainer;

import java.util.Date;
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

    @Override
    public void onStart(Intent intent, int startId) {
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 1000); // 1 second
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 1000); // 1 second
        Log.i("Anas", "Service started here !  onStartCommand");
        return START_STICKY;
    }



    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            DisplayLoggingInfo();
            handler.postDelayed(this, 5000); // 5 seconds
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
            response = call.execute(user).get(50000, TimeUnit.MILLISECONDS);
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

        sendBroadcast(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    class verifyUser extends AsyncTask<User, Void, ResultContainer<User>>{

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


}
