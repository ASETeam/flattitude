package com.aseupc.flattitude.InternalDatabase.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.aseupc.flattitude.InternalDatabase.DBDAO;
import com.aseupc.flattitude.InternalDatabase.DataBaseHelper;
import com.aseupc.flattitude.Models.User;

import java.util.Date;

/**
 * Created by Jordi on 19/10/2015.
 */
public class UserDAO extends DBDAO {
    private static final String WHERE_ID_EQUALS = DataBaseHelper.USER_ID
            + " =?";

    public UserDAO(Context context) {
        super(context);
    }

    public long save(User user) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.USER_ID, user.getId());
    //     values.put("serverid", user.getServerid());
        values.put(DataBaseHelper.USER_EMAIL, user.getEmail());
        values.put(DataBaseHelper.USER_FIRSTNAME, user.getFirstname());
        values.put(DataBaseHelper.USER_LASTNAME, user.getLastname());
        values.put(DataBaseHelper.USER_PHONENBR, user.getPhonenbr());
//        values.put(DataBaseHelper.USER_BIRTHDATE, formatDate(user.getBirthdate()));
        values.put(DataBaseHelper.USER_BIRTHDATE, "01/06/1990"); // !!!!!---- NEEDS TO BE CORRECTED
        values.put(DataBaseHelper.USER_IBAN, user.getIban());
        values.put(DataBaseHelper.USER_LOGGEDIN, user.isLoggedin());

        return database
                .insert(DataBaseHelper.USER_TABLENAME, null, values);
    }

    public long update(User user) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.USER_SERVERID,user.getServerid());
        values.put(DataBaseHelper.USER_EMAIL, user.getEmail());
        values.put(DataBaseHelper.USER_FIRSTNAME, user.getFirstname());
        values.put(DataBaseHelper.USER_LASTNAME, user.getLastname());
        values.put(DataBaseHelper.USER_PHONENBR, user.getPhonenbr());
        values.put(DataBaseHelper.USER_BIRTHDATE, formatDate(user.getBirthdate()));
        values.put(DataBaseHelper.USER_IBAN, user.getIban());
        values.put(DataBaseHelper.USER_LOGGEDIN, formatBoolean(user.isLoggedin()));

        long result = database.update(DataBaseHelper.USER_TABLENAME,
                values, WHERE_ID_EQUALS,
                new String[] { String.valueOf(user.getId()) });
        return result;
    }

    public int deleteDept(User user) {
        return database.delete(DataBaseHelper.USER_TABLENAME,
                WHERE_ID_EQUALS, new String[] { String.valueOf(user.getId()) });
    }

    public User getUser() {
        Cursor cursor = database.query(DataBaseHelper.USER_TABLENAME,
                new String[] { DataBaseHelper.USER_ID,
                        DataBaseHelper.USER_ID, // !!!!!!------ NEEDS TO BE REPLACED TO SERVER_ID AGAIN
                        DataBaseHelper.USER_EMAIL,
                        DataBaseHelper.USER_FIRSTNAME,
                        DataBaseHelper.USER_LASTNAME,
                        DataBaseHelper.USER_PHONENBR,
                        DataBaseHelper.USER_BIRTHDATE,
                        DataBaseHelper.USER_IBAN,
                        DataBaseHelper.USER_LOGGEDIN},
                null, null, null, null, null);

        if(cursor.moveToNext()) {
            User user = new User();
            user.setServerid(cursor.getString(0));
            user.setEmail(cursor.getString(2));
            user.setFirstname(cursor.getString(3));
            user.setLastname(cursor.getString(4));
            user.setPhonenbr(cursor.getString(5));
//            user.setBirthdate(parseDate(cursor.getString(6)));
            user.setBirthdate(new Date());
//            user.setIban(cursor.getString(7));
            user.setLoggedin(parseBoolean(cursor.getInt(8)));
            return user;
        }
        else return null;
    }
}