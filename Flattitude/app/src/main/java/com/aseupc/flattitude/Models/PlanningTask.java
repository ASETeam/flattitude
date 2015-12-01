package com.aseupc.flattitude.Models;

import com.aseupc.flattitude.Activities.PlanningActivity;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by MetzoDell on 25-11-15.
 */
public class PlanningTask {
    private String ID;
    private String Author;
    private String Destination;
    private String Type;
    private String Description;
    private Calendar PlannedTime;

    public static final String CLEANING_TASK = "Cleaning task";


    public static String [] getTypes(){
        return new String [] {CLEANING_TASK};
    }

 //   private Date PlannedDate;


    public String getID()
    {
        return ID;
    }

    public static String getCleanDate(Calendar date)
    {
        String theDay = date.get(Calendar.DATE) + "/" + date.get(Calendar.MONTH) + "/" + date.get(Calendar.YEAR)+ " " + date.get(Calendar.HOUR_OF_DAY) + ":" + date.get(Calendar.MINUTE) + ":" + date.get(Calendar.SECOND);
        return theDay;
    }

    public static String getOnlyDate(Calendar date)
    {
        String theDay = date.get(Calendar.DATE) + "/" + date.get(Calendar.MONTH) + "/" + date.get(Calendar.YEAR);
        return theDay;
    }
    public static String getOnlyTime(Calendar date)
    {
        String theDay = date.get(Calendar.HOUR_OF_DAY) + ":" + date.get(Calendar.MINUTE) + ":" + date.get(Calendar.SECOND);
        return theDay;
    }
    //TODO : Test of todo
    public void setID(String ID) {
        this.ID = ID;
    }

    public PlanningTask(String id, String author, String destination, String type, String description, Calendar plannedTime) {
        Author = author;
        Destination = destination;
        Type = type;
        Description = description;
        PlannedTime = plannedTime;
        ID = id;
    }

    public PlanningTask()
    {

    }

    public String getAuthor() {
        return Author;
    }

    public String getDestination() {
        return Destination;
    }

    public String getType() {
        return Type;
    }

    public String getDescription() {
        return Description;
    }

    public Calendar getPlannedTime() {
        return PlannedTime;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setPlannedTime(Calendar plannedTime) {
        PlannedTime = plannedTime;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
