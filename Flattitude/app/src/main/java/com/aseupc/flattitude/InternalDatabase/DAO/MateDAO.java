package com.aseupc.flattitude.InternalDatabase.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.aseupc.flattitude.InternalDatabase.DBDAO;
import com.aseupc.flattitude.InternalDatabase.DataBaseHelper;
import com.aseupc.flattitude.Models.Mate;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jordi on 19/10/2015.
 */
public class MateDAO extends DBDAO {
    private static final String WHERE_ID_EQUALS = DataBaseHelper.MATE_ID
            + " =?";

    public MateDAO(Context context) {
        super(context);
    }

    public long save(Mate mate) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.MATE_ID, mate.getId());
        values.put(DataBaseHelper.MATE_SERVERID, mate.getServerid());
        values.put(DataBaseHelper.MATE_EMAIL, mate.getEmail());
        values.put(DataBaseHelper.MATE_FIRSTNAME, mate.getFirstname());
        values.put(DataBaseHelper.MATE_LASTNAME, mate.getLastname());
        values.put(DataBaseHelper.MATE_PHONENBR, mate.getPhonenbr());
        values.put(DataBaseHelper.MATE_BIRTHDATE, formatDate(mate.getBirthdate()));

        return database
                .insert(DataBaseHelper.MATE_TABLENAME, null, values);
    }

    public long update(Mate mate) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.MATE_SERVERID,mate.getServerid());
        values.put(DataBaseHelper.MATE_EMAIL, mate.getEmail());
        values.put(DataBaseHelper.MATE_FIRSTNAME, mate.getFirstname());
        values.put(DataBaseHelper.MATE_LASTNAME, mate.getLastname());
        values.put(DataBaseHelper.MATE_PHONENBR, mate.getPhonenbr());
        values.put(DataBaseHelper.MATE_BIRTHDATE, formatDate(mate.getBirthdate()));

        long result = database.update(DataBaseHelper.MATE_TABLENAME,
                values, WHERE_ID_EQUALS,
                new String[] { String.valueOf(mate.getId()) });
        return result;
    }

    public int deleteDept(Mate mate) {
        return database.delete(DataBaseHelper.MATE_TABLENAME,
                WHERE_ID_EQUALS, new String[] { String.valueOf(mate.getId()) });
    }

    public List<Mate> getMates() {
        Cursor cursor = database.query(DataBaseHelper.MATE_TABLENAME,
                new String[] { DataBaseHelper.MATE_ID,
                        DataBaseHelper.MATE_SERVERID,
                        DataBaseHelper.MATE_EMAIL,
                        DataBaseHelper.MATE_FIRSTNAME,
                        DataBaseHelper.MATE_LASTNAME,
                        DataBaseHelper.MATE_PHONENBR,
                        DataBaseHelper.MATE_BIRTHDATE},
                null, null, null, null, null);

        List<Mate> list = new LinkedList<Mate>();
        while(cursor.moveToNext()) {
            Mate mate = new Mate();
            mate.setServerid(cursor.getString(1));
            mate.setEmail(cursor.getString(2));
            mate.setFirstname(cursor.getString(3));
            mate.setLastname(cursor.getString(4));
            mate.setPhonenbr(cursor.getString(5));
            mate.setBirthdate(parseDate(cursor.getString(6)));
            list.add(mate);
        }
        if(cursor != null)
        { cursor.close();} else {}
        return list;
    }
}
