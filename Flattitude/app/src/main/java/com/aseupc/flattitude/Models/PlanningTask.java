package com.aseupc.flattitude.Models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by MetzoDell on 25-11-15.
 */
public class PlanningTask implements Serializable{

    private String ID;
    private String Author;
    private String Destination;
    private String Type;
    private String Description;
    private Calendar PlannedTime;
    private Calendar AlarmTime;

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

    public int getTypeId(){
        if(Type.equals(CLEANING_TASK))
            return 0;
        else if(Type.equals(PARTY))
            return 1;
        else if(Type.equals(OTHER))
            return 2;
        return -1;
    }

    public PlanningTask(){
        this.Destination = "111";
        this.AlarmTime = null;
    }

    public PlanningTask(String id, String author, String destination, String type, String description, Calendar plannedTime) {
        Author = author;
        Destination = destination;
        Type = type;
        Description = description;
        PlannedTime = plannedTime;
        ID = id;
        AlarmTime = null;
    }

    public String getID()
    {
        return ID;
    }

    public static String getCleanDate(Calendar date)
    {
        String theDay = date.get(Calendar.DATE) + "/" + (date.get(Calendar.MONTH)+1) + "/" + date.get(Calendar.YEAR)+ " " + date.get(Calendar.HOUR_OF_DAY) + ":" + date.get(Calendar.MINUTE) + ":" + date.get(Calendar.SECOND);
        return theDay;
    }

    public static String getOnlyDate(Calendar date)
    {
        //String theDay = date.get(Calendar.DATE) + "/" + (date.get(Calendar.MONTH)+1) + "/" + date.get(Calendar.YEAR);
        return dateFormat.format(date.getTime());
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

    public void setType(int id){
        this.Type = getTypes()[id];
    }

    public void setPlannedTime(Calendar plannedTime) {
        PlannedTime = plannedTime;
    }

    public void setPlannedTime(String date, String time){
        try{
            String [] decomposedDate = date.split("/");
            String [] decomposedTime = time.split(":");
            PlannedTime = Calendar.getInstance();
            PlannedTime.set(Calendar.YEAR,Integer.parseInt(decomposedDate[2]));
            PlannedTime.set(Calendar.MONTH, Integer.parseInt(decomposedDate[1])-1);
            PlannedTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(decomposedDate[0]));
            PlannedTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(decomposedTime[0]));
            PlannedTime.set(Calendar.MINUTE, Integer.parseInt(decomposedTime[1]));
            PlannedTime.set(Calendar.SECOND, Integer.parseInt(decomposedTime[2]));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setAlarmTime(String date, String time){
        if(date == null)
            AlarmTime=null;
        else {
            try {
                String[] decomposedDate = date.split("/");
                String[] decomposedTime = time.split(":");
                AlarmTime = Calendar.getInstance();
                AlarmTime.set(Calendar.YEAR, Integer.parseInt(decomposedDate[2]));
                AlarmTime.set(Calendar.MONTH, Integer.parseInt(decomposedDate[1]) - 1);
                AlarmTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(decomposedDate[0]));
                AlarmTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(decomposedTime[0]));
                AlarmTime.set(Calendar.MINUTE, Integer.parseInt(decomposedTime[1]));
                AlarmTime.set(Calendar.SECOND, Integer.parseInt(decomposedTime[2]));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getAlarmDateString(){
        if(AlarmTime==null)
            return null;
        else
            return dateFormat.format(AlarmTime.getTime());
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

    public String getTimeStringWithSec(){
        return timeFormat.format(PlannedTime.getTime())+":00";
    }

    public String getAlarmTimeStringWithSec(){
        if(AlarmTime == null)
            return null;
        else
            return timeFormat.format(AlarmTime.getTime())+":00";
    }

    public String getHourString(){
        return hourFormat.format(PlannedTime.getTime());
    }

    public String getMinuteString(){
        return minuteFormat.format(PlannedTime.getTime());
    }

    public Calendar getAlarmTime() {
        return AlarmTime;
    }

    public void setAlarmTime(Calendar alarmTime) {
        AlarmTime = alarmTime;
    }
}
