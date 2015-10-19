package com.aseupc.InternalDatabase;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jordi on 19/10/2015.
 */
public class DBDAO {

    protected SQLiteDatabase database;
    private DataBaseHelper dbHelper;
    private Context mContext;

    public DBDAO(Context context) {
        this.mContext = context;
        dbHelper = DataBaseHelper.getHelper(mContext);
        open();
    }

    public void open() throws SQLException {
        if(dbHelper == null)
            dbHelper = DataBaseHelper.getHelper(mContext);
        database = dbHelper.getWritableDatabase();
    }

    protected String formatDate(Date date){
        if (date == null)
            return "";
       else  {
        DateFormat df = new SimpleDateFormat("YYYY-MM-dd");
        return df.format(date);}
    }

    protected Date parseDate(String date){
        return new Date();
    }

    protected int formatBoolean(boolean b){
        return b ? 1 : 0;
    }

    protected boolean parseBoolean(int x){
        return x != 0;
    }
}
