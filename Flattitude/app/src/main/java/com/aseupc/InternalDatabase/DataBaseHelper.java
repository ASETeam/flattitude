package com.aseupc.InternalDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jordi on 19/10/2015.
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "db";


    public static final String USER_TABLENAME = "userprofile";
    public static final String USER_EMAIL = "email";
    public static final String USER_ID = "id";
    public static final String USER_FIRSTNAME = "firstname";
    public static final String USER_LASTNAME = "lastname";
    public static final String USER_PHONENBR = "phonenbr";
    public static final String USER_BIRTHDATE = "birthdate";
    public static final String USER_IBAN = "iban";
    //public static final String USER_PICTURE = "picture";
    public static final String USER_LOGGEDIN = "loggedin";

    private static final String USER_TABLE_CREATE =
            "CREATE TABLE " + USER_TABLENAME + " (" +
                    USER_ID + " INT PRIMARY KEY NOT NULL, " +
                    USER_FIRSTNAME + " TEXT NOT NULL," +
                    USER_LASTNAME + " TEXT NOT NULL," +
                    USER_PHONENBR + " TEXT NOT NULL," +
                    USER_BIRTHDATE + " DATETIME," +
                    USER_IBAN + " TEXT," +
//                    USER_PICTURE + " TEXT," +
                    USER_LOGGEDIN + " INT NOT NULL" +
                    ");";


    private static DataBaseHelper instance;

    public static synchronized DataBaseHelper getHelper(Context context) {
        if (instance == null)
            instance = new DataBaseHelper(context);
        return instance;
    }

    private DataBaseHelper(Context context){
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
