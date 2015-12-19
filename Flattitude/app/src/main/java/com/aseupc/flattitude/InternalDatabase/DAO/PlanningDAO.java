package com.aseupc.flattitude.InternalDatabase.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.aseupc.flattitude.InternalDatabase.DBDAO;
import com.aseupc.flattitude.InternalDatabase.DataBaseHelper;
import com.aseupc.flattitude.Models.Notification;
import com.aseupc.flattitude.Models.PlanningTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by MetzoDell on 25-11-15.
 */
public class PlanningDAO extends  DBDAO{

        private static final String WHERE_ID_EQUALS = DataBaseHelper.PLANNING_ID
                + " =?";



        public PlanningDAO(Context context) {
            super(context);
        }

        public long save(PlanningTask mo) {
            // String time = mo.getTime().toString();
            ContentValues values = new ContentValues();
            values.put(DataBaseHelper.PLANNING_ID, mo.getID());
            values.put(DataBaseHelper.PLANNING_AUTHOR, mo.getAuthor());
            values.put(DataBaseHelper.PLANNING_DESCRIPTION, mo.getDescription());
            values.put(DataBaseHelper.PLANNING_DESTINATION, mo.getDestination());
            values.put(DataBaseHelper.PLANNING_TIME, mo.getTimeStringWithSec());
            Log.i("XAnas", PlanningTask.getOnlyDate(mo.getPlannedTime()));
            values.put(DataBaseHelper.PLANNING_DATE, mo.getDateString());
        //    values.put(DataBaseHelper.PLANNING_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
            values.put(DataBaseHelper.PLANNING_TYPE, mo.getType());

            return database
                    .insert(DataBaseHelper.PLANNING_TABLENAME, null, values);
        }

        public long update(PlanningTask mo) {
            // String time = mo.getTime().toString();
            ContentValues values = new ContentValues();
            values.put(DataBaseHelper.PLANNING_ID, mo.getID());
            values.put(DataBaseHelper.PLANNING_AUTHOR, mo.getAuthor());
            values.put(DataBaseHelper.PLANNING_DESCRIPTION, mo.getDescription());
            values.put(DataBaseHelper.PLANNING_DESTINATION, mo.getDestination());
            values.put(DataBaseHelper.PLANNING_TIME, mo.getTimeStringWithSec());
            values.put(DataBaseHelper.PLANNING_DATE, mo.getDateString());
            //    values.put(DataBaseHelper.PLANNING_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
            values.put(DataBaseHelper.PLANNING_TYPE, mo.getType());

            long result = database.update(DataBaseHelper.PLANNING_TABLENAME,
                    values, WHERE_ID_EQUALS,
                    new String[]{String.valueOf(mo.getID())});
            return result;
        }

        public int deleteDept(PlanningTask not) {
            return database.delete(DataBaseHelper.PLANNING_TABLENAME,
                    WHERE_ID_EQUALS, new String[] { String.valueOf(not.getID()) });
        }


        public List<PlanningTask> getPlanningTasks() {
            Cursor cursor;
            cursor = database.query(DataBaseHelper.PLANNING_TABLENAME,
                    new String[] {
                            DataBaseHelper.PLANNING_ID,
                            DataBaseHelper.PLANNING_AUTHOR,
                            DataBaseHelper.PLANNING_DESCRIPTION,
                            DataBaseHelper.PLANNING_DESTINATION,
                            DataBaseHelper.PLANNING_TYPE,
                            DataBaseHelper.PLANNING_TIME,
                            DataBaseHelper.PLANNING_DATE},
                    null, null, DataBaseHelper.PLANNING_DATE, null,null);


            List<PlanningTask> list = new LinkedList<>();
            while(cursor.moveToNext()) {
                Log.i("IANAS", cursor.getInt(0) + " - " + cursor.getString(1)
                        + " - " + cursor.getString(2)
                        + " - " + cursor.getString(3)
                        + " - " + cursor.getString(4)
                        + " - " + cursor.getString(5)
                        + " - " + cursor.getString(6));
                PlanningTask mo = new PlanningTask();
                mo.setID(cursor.getInt(0) + "");
                mo.setAuthor(cursor.getString(1));
                mo.setDescription(cursor.getString(2));
                mo.setDestination(cursor.getString(3));
                mo.setType(cursor.getString(4));
                mo.setPlannedTime(cursor.getString(6), cursor.getString(5));
              //  mo.setTime(parseDate(cursor.getString(6)));
                list.add(mo);
            }
            if(cursor != null)
            { cursor.close();} else {}
            return list;
        }


    public List<PlanningTask> getGroupedPlanningTasks() {
        Cursor cursor;
        cursor = database.query(DataBaseHelper.PLANNING_TABLENAME,
                new String[] {
                        DataBaseHelper.PLANNING_ID,
                        DataBaseHelper.PLANNING_AUTHOR,
                        DataBaseHelper.PLANNING_DESCRIPTION,
                        DataBaseHelper.PLANNING_DESTINATION,
                        DataBaseHelper.PLANNING_TYPE,
                        DataBaseHelper.PLANNING_TIME,
                        DataBaseHelper.PLANNING_DATE},
                DataBaseHelper.PLANNING_DATE, null, DataBaseHelper.PLANNING_DATE, null,null);


        List<PlanningTask> list = new LinkedList<>();
        while(cursor.moveToNext()) {
            Log.i("IANAS", cursor.getInt(0) + " - " + cursor.getString(1)
                    + " - " + cursor.getString(2)
                    + " - " + cursor.getString(3)
                    + " - " + cursor.getString(4)
                    + " - " + cursor.getString(5)
                    + " - " + cursor.getString(6));
            PlanningTask mo = new PlanningTask();
            mo.setID(cursor.getInt(0) + "");
            mo.setAuthor(cursor.getString(1));
            mo.setDescription(cursor.getString(2));
            mo.setDestination(cursor.getString(3));
            mo.setType(cursor.getString(4));
            mo.setPlannedTime(cursor.getString(6),cursor.getString(5));
            list.add(mo);
        }
        return list;
    }

    public List<PlanningTask> getFilteredPlanningTasks(Calendar date){
        Cursor cursor;
      // cursor = database.rawQuery("select planningid author destination type description date time from planning where date = ? ", new String[]{date});
     //   rawQuery("select * from todo where _id = ?", new String[] { id });


        // cursor = database.rawQuery("select * from planning where date = " + date, null);

        cursor = database.query(true,  DataBaseHelper.PLANNING_TABLENAME,
                new String[] {
                        DataBaseHelper.PLANNING_ID,
                        DataBaseHelper.PLANNING_AUTHOR,
                        DataBaseHelper.PLANNING_DESCRIPTION,
                        DataBaseHelper.PLANNING_DESTINATION,
                        DataBaseHelper.PLANNING_TYPE,
                        DataBaseHelper.PLANNING_TIME,
                        DataBaseHelper.PLANNING_DATE},
                DataBaseHelper.PLANNING_DATE + " = " + "'" + PlanningTask.getOnlyDate(date) +"'", null, null, null,null, null);

        List<PlanningTask> list = new LinkedList<>();
        while(cursor.moveToNext()) {
            PlanningTask mo = new PlanningTask();
            mo.setID(cursor.getInt(0) + "");
            mo.setAuthor(cursor.getString(1));
            mo.setDescription(cursor.getString(2));
            mo.setDestination(cursor.getString(3));
            mo.setType(cursor.getString(4));
            mo.setPlannedTime(cursor.getString(6),cursor.getString(5));
            list.add(mo);
        }
        return list;

    }



}


