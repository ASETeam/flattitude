package com.aseupc.InternalDatabase.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.aseupc.InternalDatabase.DBDAO;
import com.aseupc.InternalDatabase.DataBaseHelper;
import com.aseupc.Models.Flat;
import com.aseupc.Models.User;

/**
 * Created by Jordi on 19/10/2015.
 */
public class FlatDAO extends DBDAO {

    private static final String WHERE_ID_EQUALS = DataBaseHelper.FLAT_ID
            + " =?";

    public FlatDAO(Context context) {
        super(context);
    }

    public long save(Flat flat) {

        ContentValues values = new ContentValues();


        return database
                .insert(DataBaseHelper.FLAT_TABLENAME, null, values);
    }

    public long update(Flat flat) {
        ContentValues values = new ContentValues();


        long result = database.update(DataBaseHelper.FLAT_TABLENAME,
                values, WHERE_ID_EQUALS,
                new String[] { String.valueOf(flat.getId()) });
        return result;
    }

    public int deleteDept(Flat flat) {
        return database.delete(DataBaseHelper.FLAT_TABLENAME,
                WHERE_ID_EQUALS, new String[] { String.valueOf(flat.getId()) });
    }

    public Flat getFlat() {
        Cursor cursor = database.query(DataBaseHelper.FLAT_TABLENAME,
                new String[] { },
                null, null, null, null, null);

        if(cursor.moveToNext()) {
            Flat flat = new Flat();
            return flat;
        }
        else return null;
    }
}
