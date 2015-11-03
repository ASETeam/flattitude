package com.aseupc.flattitude.synchronization;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by MetzoDell on 27-10-15.
 */
public class SynchzonizationService extends Service {

    private static Timer timer = new Timer();
    private Context ctx;
    Intent intent;
    public static final String BROADCAST_ACTION = "com.websmithing.broadcasttest.displayevent";
    private static final String TAG = "BroadcastService";

    @Override
    public void onCreate() {
        super.onCreate();
        ctx = this;
        Log.i("Anas", "SynchoService onCreate is ok ");
        intent = new Intent(BROADCAST_ACTION);
        startService();

    }

    private void startService() {
        timer.scheduleAtFixedRate(new mainTask(), 0, 5000);
        
    }
    private class mainTask extends TimerTask
    {
        public void run()
        {
            toastHandler.sendEmptyMessage(0);
        }
    }

    public void onDestroy()
    {
        super.onDestroy();
        Toast.makeText(this, "Service Stopped ...", Toast.LENGTH_SHORT).show();
    }

    private final Handler toastHandler = new Handler()
    {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void handleMessage(Message msg)
        {
          //  Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
            CharSequence text = "Background action EXECUTED !";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(ctx, text, duration);
           // toast.show();
           // MainActivity.mItem.setIcon(getResources().getDrawable(R.drawable.ic_alert));

        }



    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //startBackgroundTask(intent, startId);


        Log.i("call", "The background task has been called from service");


        return Service.START_STICKY;
    }



    private void startBackgroundTask() {
        Log.i("starBckgtask", "The background task has been started");
        Context context = getApplicationContext();
        CharSequence text = "Background action EXECUTED !";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        /*
        Handler mHandler = new Handler();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                MainActivity.mItem.setIcon(getResources().
                        getDrawable(R.drawable.ic_alert));

                /*Context context = MainActivity.currentContext;
                CharSequence text = "Background action EXECUTED !";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }
        });   */
        Log.i("In background", "Activity in the background thread");


    }


}
