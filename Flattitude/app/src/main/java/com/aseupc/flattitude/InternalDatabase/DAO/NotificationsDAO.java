package com.aseupc.flattitude.InternalDatabase.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.aseupc.flattitude.InternalDatabase.DBDAO;
import com.aseupc.flattitude.InternalDatabase.DataBaseHelper;
import com.aseupc.flattitude.Models.MapObject;
import com.aseupc.flattitude.Models.Mate;
import com.aseupc.flattitude.Models.Notification;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by MetzoDell on 10-11-15.
 */
public class NotificationsDAO extends DBDAO {
    private static final String WHERE_ID_EQUALS = DataBaseHelper.NOTIFICATION_ID
            + " =?";



    public NotificationsDAO(Context context) {
        super(context);
    }

    public long save(Notification mo) {
       // String time = mo.getTime().toString();
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.NOTIFICATION_ID, mo.getId());
        values.put(DataBaseHelper.NOTIFICATION_BODY, mo.getBody());
        values.put(DataBaseHelper.NOTIFICATION_HASNOTIFICATION, mo.isSeennotification());
        values.put(DataBaseHelper.NOTIFICATION_TYPE, mo.getType());
        //  values.put(DataBaseHelper.NOTIFICATION_TIME, mo.getTime().toString());
        values.put(DataBaseHelper.NOTIFICATION_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
        values.put(DataBaseHelper.NOTIFICATION_AUTHOR, mo.getAuthor());

        return database
                .insert(DataBaseHelper.NOTIFICATIONS_TABLENAME, null, values);
    }

    public long update(Notification mo) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.NOTIFICATION_ID, mo.getId());
        values.put(DataBaseHelper.NOTIFICATION_BODY, mo.getBody());
        values.put(DataBaseHelper.NOTIFICATION_HASNOTIFICATION, mo.isSeennotification());
        values.put(DataBaseHelper.NOTIFICATION_TYPE, mo.getType());
      //  values.put(DataBaseHelper.NOTIFICATION_TIME, mo.getTime().toString());
        values.put(DataBaseHelper.NOTIFICATION_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
        values.put(DataBaseHelper.NOTIFICATION_AUTHOR, mo.getAuthor());

        long result = database.update(DataBaseHelper.NOTIFICATIONS_TABLENAME,
                values, WHERE_ID_EQUALS,
                new String[]{String.valueOf(mo.getId())});
        return result;
    }

    public int deleteDept(Notification not) {
        return database.delete(DataBaseHelper.NOTIFICATIONS_TABLENAME,
                WHERE_ID_EQUALS, new String[] { String.valueOf(not.getId()) });
    }


    public List<Notification> getNotifications() {
        Cursor cursor;
        cursor = database.query(DataBaseHelper.NOTIFICATIONS_TABLENAME,
                new String[] {
                        DataBaseHelper.NOTIFICATION_ID,
                        DataBaseHelper.NOTIFICATION_AUTHOR,
                        DataBaseHelper.NOTIFICATION_HASNOTIFICATION,
                        DataBaseHelper.NOTIFICATION_BODY,
                        DataBaseHelper.NOTIFICATION_TASKTYPE,
                        DataBaseHelper.NOTIFICATION_TYPE,
                        DataBaseHelper.NOTIFICATION_TIME,
                        DataBaseHelper.NOTIFICATION_OBJECTNAME},
                null, null, DataBaseHelper.NOTIFICATION_TIME, null,null, "5");


        List<Notification> list = new LinkedList<>();
        while(cursor.moveToNext()) {
            boolean seen;
            if (cursor.getString(2) == "true") seen = true; else seen = false;
            Notification mo = new Notification();
            mo.setId(cursor.getInt(0));
            mo.setAuthor(cursor.getString(1));
            mo.setSeennotification(seen);
            mo.setBody(cursor.getString(3));

            mo.setType(cursor.getString(5));
            mo.setTime(parseDate(cursor.getString(6)));
            list.add(mo);
        }
        return list;
    }


}
