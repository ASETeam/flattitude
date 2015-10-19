package com.aseupc.Models;

import java.util.Date;

/**
 * Created by Jordi on 19/10/2015.
 */
public class User {

    private final int id = 0; //This is an special case for this model, since we will have only one user
    private String email;
    private String firstname;
    private String lastname;
    private String phonenbr;
    private Date birthdate;
    private String iban;
    //private String picture;
    private boolean loggedin;


    public User() {
    }

    public User(String email, String firstname, String lastname) {
        this.setEmail(new String(email));
        this.setFirstname(new String(firstname));
        this.setLastname(new String(lastname));
        this.setPhonenbr("");
        this.setBirthdate(null);
        this.setIban("");
        //this.picture = "";
        this.setLoggedin(false);
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
        this.iban = new String(iban);
    }

    public boolean isLoggedin() {
        return loggedin;
    }

    public void setLoggedin(boolean loggedin) {
        this.loggedin = loggedin;
    }
}
