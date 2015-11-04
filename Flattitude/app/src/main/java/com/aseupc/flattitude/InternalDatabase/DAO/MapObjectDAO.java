package com.aseupc.flattitude.InternalDatabase.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.aseupc.flattitude.InternalDatabase.DBDAO;
import com.aseupc.flattitude.InternalDatabase.DataBaseHelper;
import com.aseupc.flattitude.Models.MapObject;
import com.aseupc.flattitude.Models.Mate;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jordi on 04/11/2015.
 */
public class MapObjectDAO extends DBDAO {
    private static final String WHERE_ID_EQUALS = DataBaseHelper.MAPOBJECT_ID
        + " =?";

    public MapObjectDAO(Context context) {
        super(context);
    }

    public long save(MapObject mo) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.MAPOBJECT_ID, mo.getId());
        values.put(DataBaseHelper.MAPOBJECT_NAME, mo.getName());
        values.put(DataBaseHelper.MAPOBJECT_DESCRIPTION, mo.getDescription());
        values.put(DataBaseHelper.MAPOBJECT_LATITUDE, mo.getLatitude());
        values.put(DataBaseHelper.MAPOBJECT_LONGITUDE, mo.getLongitude());

        return database
            .insert(DataBaseHelper.MAPOBJECT_TABLENAME, null, values);
    }

    public long update(MapObject mo) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.MAPOBJECT_NAME, mo.getName());
        values.put(DataBaseHelper.MAPOBJECT_DESCRIPTION, mo.getDescription());
        values.put(DataBaseHelper.MAPOBJECT_LATITUDE, mo.getLatitude());
        values.put(DataBaseHelper.MAPOBJECT_LONGITUDE, mo.getLongitude());

        long result = database.update(DataBaseHelper.MAPOBJECT_TABLENAME,
            values, WHERE_ID_EQUALS,
            new String[] { String.valueOf(mo.getId()) });
        return result;
    }

    public int deleteDept(MapObject mo) {
        return database.delete(DataBaseHelper.MAPOBJECT_TABLENAME,
            WHERE_ID_EQUALS, new String[] { String.valueOf(mo.getId()) });
    }

    public List<MapObject> getMapObjects() {
        Cursor cursor = database.query(DataBaseHelper.MAPOBJECT_TABLENAME,
            new String[] {
                DataBaseHelper.MAPOBJECT_ID,
                DataBaseHelper.MAPOBJECT_NAME,
                DataBaseHelper.MAPOBJECT_DESCRIPTION,
                DataBaseHelper.MAPOBJECT_LATITUDE,
                DataBaseHelper.MAPOBJECT_LONGITUDE},
            null, null, null, null, null);

        List<MapObject> list = new LinkedList<>();
        while(cursor.moveToNext()) {
            MapObject mo = new MapObject();
            mo.setId(cursor.getInt(0));
            mo.setName(cursor.getString(1));
            mo.setDescription(cursor.getString(2));
            mo.setLatitude(cursor.getDouble(3));
            mo.setLongitude(cursor.getDouble(4));
            list.add(mo);
        }
        return list;
    }
}