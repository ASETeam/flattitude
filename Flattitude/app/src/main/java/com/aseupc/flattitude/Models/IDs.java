package com.aseupc.flattitude.Models;

import android.content.Context;

import com.aseupc.flattitude.InternalDatabase.DAO.FlatDAO;
import com.aseupc.flattitude.InternalDatabase.DAO.UserDAO;
import com.aseupc.flattitude.synchronization.JabberSmackAPI;

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
        }
        if(f != null)
            flatId = f.getServerid();
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
        return userToken;
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

    public JabberSmackAPI getSmackChat() {
        return smackChat;
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

}
