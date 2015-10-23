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
        values.put(DataBaseHelper.FLAT_ID, flat.getId());
        values.put(DataBaseHelper.FLAT_SERVERID, flat.getServerid());
        values.put(DataBaseHelper.FLAT_NAME, flat.getName());
        values.put(DataBaseHelper.FLAT_COUNTRY, flat.getCountry());
        values.put(DataBaseHelper.FLAT_CITY, flat.getCity());
        values.put(DataBaseHelper.FLAT_POSTCODE, flat.getPostcode());
        values.put(DataBaseHelper.FLAT_ADDRESS, flat.getAddress());
        values.put(DataBaseHelper.FLAT_IBAN, flat.getIban());

        return database
                .insert(DataBaseHelper.FLAT_TABLENAME, null, values);
    }

    public long update(Flat flat) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.FLAT_SERVERID, flat.getServerid());
        values.put(DataBaseHelper.FLAT_NAME, flat.getName());
        values.put(DataBaseHelper.FLAT_COUNTRY, flat.getCountry());
        values.put(DataBaseHelper.FLAT_CITY, flat.getCity());
        values.put(DataBaseHelper.FLAT_POSTCODE, flat.getPostcode());
        values.put(DataBaseHelper.FLAT_ADDRESS, flat.getAddress());
        values.put(DataBaseHelper.FLAT_IBAN, flat.getIban());

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
            new String[] {
                DataBaseHelper.FLAT_ID,
                DataBaseHelper.FLAT_SERVERID,
                DataBaseHelper.FLAT_NAME,
                DataBaseHelper.FLAT_COUNTRY,
                DataBaseHelper.FLAT_CITY,
                DataBaseHelper.FLAT_POSTCODE,
                DataBaseHelper.FLAT_ADDRESS,
                DataBaseHelper.FLAT_IBAN,
            },
            null, null, null, null, null);

        if(cursor.moveToNext()) {
            Flat flat = new Flat();
            flat.setServerid(cursor.getString(1));
            flat.setName(cursor.getString(2));
            flat.setCountry(cursor.getString(3));
            flat.setCity(cursor.getString(4));
            flat.setPostcode(cursor.getString(5));
            flat.setAddress(cursor.getString(6));
            flat.setIban(cursor.getString(7));
            return flat;
        }
        else return null;
    }
}
