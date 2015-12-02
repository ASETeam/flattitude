package com.aseupc.flattitude.Models;

import com.aseupc.flattitude.Activities.PlanningActivity;

import java.sql.Date;
import java.text.SimpleDateFormat;
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

    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
    private static final SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    private static final SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
    private static final SimpleDateFormat dayFormat = new SimpleDateFormat("dd");

    public static final String CLEANING_TASK = "Cleaning task";
    public static final String PARTY = "Party";
    public static final String OTHER = "Other";

    public static String [] getTypes(){
        return new String [] {
                CLEANING_TASK, PARTY, OTHER
        };
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

    public String getDateString(){
        return dateFormat.format(PlannedTime.getTime());
    }

    public String getYearString(){
        return yearFormat.format(PlannedTime.getTime());
    }

    public String getMonthString(){
        return monthFormat.format(PlannedTime.getTime());
    }

    public String getDayString(){
        return dayFormat.format(PlannedTime.getTime());
    }

    public String getTimeString(){
        return timeFormat.format(PlannedTime.getTime());
    }

    public String getHourString(){
        return hourFormat.format(PlannedTime.getTime());
    }

    public String getMinuteString(){
        return minuteFormat.format(PlannedTime.getTime());
    }
}
