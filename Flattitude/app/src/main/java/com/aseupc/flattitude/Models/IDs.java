package com.aseupc.flattitude.Models;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.aseupc.flattitude.InternalDatabase.DAO.FlatDAO;
import com.aseupc.flattitude.InternalDatabase.DAO.UserDAO;
import com.aseupc.flattitude.synchronization.JabberSmackAPI;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Jordi on 02/12/2015.
 */
public class IDs {

    private static IDs instance = null;
    private String userId;
    private String userToken;
    private String flatId;
    private JabberSmackAPI smackChat;
    private boolean newUser;
    private double balance;
    private double personalExpense;
    private boolean haveInternet;
    private String password;
    private String flatname;
    private User user;
    private Flat flat;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFlatname() {
        return flatname;
    }

    public void setFlatname(String flatname) {
        this.flatname = flatname;
    }

    public void setFlatId(String flatId) {
        this.flatId = flatId;
    }

    public void setHaveInternet(boolean haveInternet) {
        this.haveInternet = haveInternet;
    }

    public boolean getHaveInternet()
    {
        return haveInternet;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void setPersonalExpense(double personalExpense) {
        this.personalExpense = personalExpense;
    }

    public double getPersonalExpense() {
        return personalExpense;
    }

    private IDs(Context context){
        userId = null;
        userToken = null;
        flatId = null;
        newUser = false;
        personalExpense = -112.23;
        balance = 2343;

        UserDAO uDAO = new UserDAO(context);
        User u = uDAO.getUser();
        FlatDAO fDAO = new FlatDAO(context);
        Flat f = fDAO.getFlat();
        if(u != null) {
            userId = u.getServerid();
            userToken = u.getToken();
            user = u;
        }
        if(f != null) {
            flatId = f.getServerid();
            flat = f;
        }
    }

    public static IDs getInstance(Context context){
        if(instance == null)
            instance = new IDs(context);
        return instance;
    }

    public static void resetIDs(){
        instance = null;
    }

    public String getUserId(Context context){
        if(userId==null){
            UserDAO uDAO = new UserDAO(context);
            User u = uDAO.getUser();
            if(u != null){
                userId = u.getServerid();
                userToken = u.getToken();
            }
        }
        return userId;
    }

    public String getUserToken(Context context){
        if(userToken==null){
            UserDAO uDAO = new UserDAO(context);
            User u = uDAO.getUser();
            if(u != null){
                userId = u.getServerid();
                userToken = u.getToken();
            }
        }
        return userToken == null ? "" : userToken;
    }

    public String getFlatId(Context context){
        if(flatId==null){
            FlatDAO fDAO = new FlatDAO(context);
            Flat f = fDAO.getFlat();
            if(f != null)
                flatId = f.getServerid();
        }
       return flatId;
    }

    public JabberSmackAPI getSmackChat(Context ctx) {
        if (smackChat == null)
        {
           connectChat call = new connectChat();

            JabberSmackAPI obj= null;
            try {
                obj = call.execute(ctx).get(50000, TimeUnit.MILLISECONDS);
                Log.i("Give", "up");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            return obj;
        }
        else
        return smackChat;
    }

    public User getUser(Context context) {
        if(flat==null){
            UserDAO userDAO = new UserDAO(context);
            User u = userDAO.getUser();
            if(u != null)
                user = u;
        }
        return user;
    }

    public Flat getFlat(Context context) {
        if(flat==null){
            FlatDAO fDAO = new FlatDAO(context);
            Flat f = fDAO.getFlat();
            if(f != null)
                flat = f;
        }
        return flat;
    }

    public void setSmackChat(JabberSmackAPI smackChat) {
        this.smackChat = smackChat;
    }

    public void setNewUser() {
        this.newUser = true;
    }
    public void setConfirmedUser() {
        this.newUser = false;
    }
    public boolean getNewUser(){
        return newUser;
    }

    public class connectChat extends AsyncTask<Context, Void, JabberSmackAPI>
    {
        @Override
        protected void onPostExecute(JabberSmackAPI aVoid) {
            super.onPostExecute(aVoid);
            // dialog.hide();
        }

        @Override
        protected JabberSmackAPI doInBackground(Context... params) {
            try {
                JabberSmackAPI smackChat = new JabberSmackAPI();
                Context context = params[0];
                //Login to Chat.
                smackChat.login(getUserId(context), getPassword());


                //Join to room.
                if (getFlat(context).getName() != null)
                    smackChat.joinMUC(flatname, getUser(context).getFirstname());

                IDs.getInstance(context).setSmackChat(smackChat);
                return smackChat;

            } catch (Exception ex ) {
                Log.e("CHAT ERROR", ex.getMessage());
                ex.printStackTrace();
            }
            return null;

        }
    }



}
