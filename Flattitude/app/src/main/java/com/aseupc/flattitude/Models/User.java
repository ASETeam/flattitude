package com.aseupc.flattitude.Models;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Jordi on 19/10/2015.
 */
public class User {

    private final int id = 0; //This is an special case for this model, since we will have only one user
    private String serverid;
    private String email;
    private String firstname;
    private String lastname;
    private String phonenbr;
    private Date birthdate;
    private String iban;
    private String password;
    //private String picture;
    private boolean loggedin;
    private String token;
    private ArrayList<Flat> invitations;
    private ArrayList<Notification> notifications;

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<Notification> notifications) {
        this.notifications = notifications;
    }

    public void addNotifications(Notification notification)
    {
        this.notifications.add(notification);
    }

    public User() {
        invitations = new ArrayList<Flat>();
    }

    public User( String email, String firstname, String lastname) {
        this.setServerid(new String(serverid));
        this.setEmail(new String(email));
        this.setFirstname(new String(firstname));
        this.setLastname(new String(lastname));
        this.setPhonenbr("");
        this.setBirthdate(null);
        this.setIban("");
        //this.picture = "";
        this.setLoggedin(false);
        invitations = new ArrayList<Flat>();
    }

    public void addInvite(Flat invite)
    {
        invitations.add(invite);
    }
    public void setInvitations(ArrayList<Flat> invitation) {
        this.invitations = invitation;
    }

    public ArrayList<Flat> getInvitations() {
        return invitations;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = new String(email);
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = new String(firstname);
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = new String(lastname);
    }

    public String getPhonenbr() {
        return phonenbr;
    }

    public void setPhonenbr(String phonenbr) {
        this.phonenbr = new String(phonenbr);
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
     if (iban != null)   this.iban = new String(iban);
    }

    public boolean isLoggedin() {
        return loggedin;
    }

    public void setLoggedin(boolean loggedin) {
        this.loggedin = loggedin;
    }

    public String getServerid() {
        return serverid;
    }

    public void setServerid(String serverid) {
        this.serverid = new String(serverid);
    }
}
