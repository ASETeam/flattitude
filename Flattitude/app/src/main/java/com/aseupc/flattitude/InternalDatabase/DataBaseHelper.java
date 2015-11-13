package com.aseupc.flattitude.InternalDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jordi on 19/10/2015.
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "db";
    private Context context;


    //----------------------USER TABLE--------------------------
    public static final String USER_TABLENAME = "userprofile";

    public static final String USER_ID = "id";
    public static final String USER_SERVERID = "serverid";
    public static final String USER_EMAIL = "email";
    public static final String USER_FIRSTNAME = "firstname";
    public static final String USER_LASTNAME = "lastname";
    public static final String USER_PHONENBR = "phonenbr";
    public static final String USER_BIRTHDATE = "birthdate";
    public static final String USER_IBAN = "iban";
    //public static final String USER_PICTURE = "picture";
    public static final String USER_LOGGEDIN = "loggedin";
    public static final String USER_TOKEN = "token";

    private static final String USER_TABLE_CREATE =
            "CREATE TABLE " + USER_TABLENAME + " (" +
                    USER_ID + " INT PRIMARY KEY NOT NULL, " +
                    USER_SERVERID + " STRING, " +  // NOT NULL !!!
                    USER_EMAIL + " TEXT NOT NULL," +
                    USER_FIRSTNAME + " TEXT NOT NULL," +
                    USER_LASTNAME + " TEXT NOT NULL," +
                    USER_PHONENBR + " TEXT," +
                    USER_BIRTHDATE + " DATETIME," +
                    USER_IBAN + " TEXT," +
//                    USER_PICTURE + " TEXT," +
                    USER_LOGGEDIN + " INT, " +
                    USER_TOKEN + " TEXT" +
                    ");";


    //----------------------FLAT TABLE--------------------------
    public static final String FLAT_TABLENAME = "flatprofile";

    public static final String FLAT_ID = "id";
    public static final String FLAT_SERVERID = "serverid";
    public static final String FLAT_NAME = "name";
    public static final String FLAT_COUNTRY = "country";
    public static final String FLAT_CITY = "city";
    public static final String FLAT_POSTCODE = "postcode";
    public static final String FLAT_ADDRESS = "address";
    public static final String FLAT_IBAN = "iban";

    public static final String FLAT_TABLE_CREATE =
            "CREATE TABLE " + FLAT_TABLENAME + " (" +
                    FLAT_ID + " INT PRIMARY KEY NOT NULL, " +
                    FLAT_SERVERID + " STRING NOT NULL, " +
                    FLAT_NAME + " STRING NOT NULL, " +
                    FLAT_COUNTRY + " STRING NOT NULL, " +
                    FLAT_CITY + " STRING NOT NULL, " +
                    FLAT_POSTCODE + " STRING NOT NULL, " +
                    FLAT_ADDRESS + " STRING NOT NULL, " +
                    FLAT_IBAN + " STRING" +
                    ");";


    //----------------------MATE TABLE--------------------------
    public static final String MATE_TABLENAME = "mate";

    public static final String MATE_ID = "id";
    public static final String MATE_SERVERID = "serverid";
    public static final String MATE_EMAIL = "email";
    public static final String MATE_FIRSTNAME = "firstname";
    public static final String MATE_LASTNAME = "lastname";
    public static final String MATE_PHONENBR = "phonenbr";
    public static final String MATE_BIRTHDATE = "birthdate";
    //public static final String MATE_PICTURE = "picture";

    private static final String MATE_TABLE_CREATE =
            "CREATE TABLE " + MATE_TABLENAME + " (" +
                    MATE_ID + " INT PRIMARY KEY NOT NULL, " +
                    MATE_SERVERID + "STRING NOT NULL, " +
                    MATE_EMAIL + " TEXT NOT NULL," +
                    MATE_FIRSTNAME + " TEXT NOT NULL," +
                    MATE_LASTNAME + " TEXT NOT NULL," +
                    MATE_PHONENBR + " TEXT NOT NULL," +
                    MATE_BIRTHDATE + " DATETIME" +
//                    MATE_PICTURE + " TEXT," +
                    ");";


    //--------------SHARED OBJECTS TABLE----------------
    public static final String MAPOBJECT_TABLENAME = "sharnigobjectlist";

    public static final String MAPOBJECT_ID = "objectid";
    public static final String MAPOBJECT_SERVERID = "objectserverid";
    public static final String MAPOBJECT_NAME = "objectname";
    public static final String MAPOBJECT_DESCRIPTION = "objectdescription";
    public static final String MAPOBJECT_LATITUDE = "lat";
    public static final String MAPOBJECT_LONGITUDE = "lng";


    private static final String MAPOBJECT_TABLE_CREATE =
            "CREATE TABLE " + MAPOBJECT_TABLENAME + " (" +
                    MAPOBJECT_ID + " INT PRIMARY KEY NOT NULL, " +
                    MAPOBJECT_SERVERID + " TEXT NOT NULL, " +
                    MAPOBJECT_NAME + " TEXT NOT NULL, " +
                    MAPOBJECT_DESCRIPTION + " TEXT," +
                    MAPOBJECT_LATITUDE + " REAL NOT NULL," +
                    MAPOBJECT_LONGITUDE + " REAL NOT NULL" +
                    ");";



    //--------------NOTIFICATIONS TABLE----------------
    public static final String NOTIFICATIONS_TABLENAME = "notifications";

    public static final String NOTIFICATION_ID = "notificationid";
    public static final String NOTIFICATION_HASNOTIFICATION = "hasnotification";
    public static final String NOTIFICATION_TIME = "time";
    public static final String NOTIFICATION_TYPE = "type";
    public static final String NOTIFICATION_BODY = "body";
    public static final String NOTIFICATION_OBJECTNAME = "objectname";
    public static final String NOTIFICATION_TASKTYPE = "tasktype";
    public static final String NOTIFICATION_AUTHOR = "author";


    private static final String NOTIFICATIONS_TABLE_CREATE =
            "CREATE TABLE " + NOTIFICATIONS_TABLENAME + " (" +
                    NOTIFICATION_ID + " INT PRIMARY KEY NOT NULL, " +
                    NOTIFICATION_HASNOTIFICATION + " TEXT NOT NULL, " +
                    NOTIFICATION_TYPE + " TEXT," +
                    NOTIFICATION_BODY + " TEXT," +
                    NOTIFICATION_AUTHOR + " TEXT," +
                    NOTIFICATION_TASKTYPE + " TEXT," +
                    NOTIFICATION_TIME + " TEXT," +
                    NOTIFICATION_OBJECTNAME + " TEXT " +
                    ");";



    private static DataBaseHelper instance;

    public static synchronized DataBaseHelper getHelper(Context context) {
        if (instance == null) {
            instance = new DataBaseHelper(context);
        }
        return instance;
    }

    private DataBaseHelper(Context context){
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_TABLE_CREATE);
        db.execSQL(FLAT_TABLE_CREATE);
        db.execSQL(MATE_TABLE_CREATE);
        db.execSQL(MAPOBJECT_TABLE_CREATE);
        db.execSQL(NOTIFICATIONS_TABLE_CREATE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        context.deleteDatabase(DATABASE_NAME);
    }
}
