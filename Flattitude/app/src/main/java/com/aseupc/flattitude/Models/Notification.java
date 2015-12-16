package com.aseupc.flattitude.Models;

import android.graphics.drawable.Icon;

import com.aseupc.flattitude.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by MetzoDell on 10-11-15.
 */
public class Notification {
    private int id;
    private String type;
    private boolean seennotification;
    private String body;
    private String author;
    private Date time;
    private Integer myIcon;
    private int serverID;
    private String objectID;
    private String user;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public int getServerID() {
        return serverID;
    }

    public void setServerID(int serverID) {
        this.serverID = serverID;
    }

    public Integer getMyIcon() {
        return myIcon;
    }

    public void setMyIcon(Integer myIcon) {
        this.myIcon = myIcon;
    }

    public Notification() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        if (type == null)
            return;
        switch (type.toUpperCase()) {
            case "ADD":
                this.myIcon = R.drawable.ic_add;
                break;
            case "INVITATION":
                this.myIcon = R.drawable.ic_invite;
                break;
            case "CHAT" :
                this.myIcon = R.drawable.ic_chat;
                break;
            case "MAP" :
                this.myIcon = R.drawable.ic_map;
                break;
            case "PLANNING" :
                this.myIcon = R.drawable.ic_calendar;
                break;
            default:
                this.myIcon = R.drawable.ic_message;
                break;
        }

    }

    public boolean isSeennotification() {
        return seennotification;
    }

    public void setSeennotification(boolean seennotification) {
        this.seennotification = seennotification;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        Notification x = (Notification) o;
        if (x.getServerID() == this.getServerID())
            return true;
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
