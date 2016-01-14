package com.aseupc.flattitude.InternalDatabase.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.aseupc.flattitude.InternalDatabase.DBDAO;
import com.aseupc.flattitude.InternalDatabase.DataBaseHelper;
import com.aseupc.flattitude.Models.BudgetOperationDBAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Vavou on 14/01/2016.
 * Budget Operation DBDAO
 */
public class BudgetOperationDAO extends DBDAO {
    public BudgetOperationDAO(Context context) {
        super(context);
    }

    public int deleteAll(){
        return database.delete(DataBaseHelper.BUDGET_OPERATION_TABLENAME, null, null);
    }

    public void save(ArrayList<BudgetOperationDBAdapter> operations) {
        ContentValues values = new ContentValues();
        for (BudgetOperationDBAdapter budgetOperation : operations) {
            values.put(DataBaseHelper.BUDGET_OPERATION_ID, budgetOperation.getId());
            values.put(DataBaseHelper.BUDGET_OPERATION_USER, budgetOperation.getUserName());
            values.put(DataBaseHelper.BUDGET_OPERATION_FLAT, budgetOperation.getFlatId());
            values.put(DataBaseHelper.BUDGET_OPERATION_DATE, formatDate(budgetOperation.getDate()));
            values.put(DataBaseHelper.BUDGET_OPERATION_AMOUNT, budgetOperation.getAmount());
            values.put(DataBaseHelper.BUDGET_OPERATION_DESCRIPTION, budgetOperation.getDescription());
            database.insert(DataBaseHelper.BUDGET_OPERATION_TABLENAME, null, values);
            values.clear();
        }
    }

    public ArrayList<BudgetOperationDBAdapter> getBudgetOperations() {
        Cursor cursor = database.query(DataBaseHelper.BUDGET_OPERATION_TABLENAME,
                new String[] {
                        DataBaseHelper.BUDGET_OPERATION_ID,
                        DataBaseHelper.BUDGET_OPERATION_USER,
                        DataBaseHelper.BUDGET_OPERATION_FLAT,
                        DataBaseHelper.BUDGET_OPERATION_AMOUNT,
                        DataBaseHelper.BUDGET_OPERATION_DESCRIPTION,
                        DataBaseHelper.BUDGET_OPERATION_DATE
                },
                null, null, null, null, DataBaseHelper.BUDGET_OPERATION_DATE + " DESC");

        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<BudgetOperationDBAdapter> list = new ArrayList<>();
        try {
            while(cursor.moveToNext()) {
                BudgetOperationDBAdapter bo = new BudgetOperationDBAdapter(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getDouble(3),
                        cursor.getString(4),
                        dateFormat.parse(cursor.getString(5))
                );
                list.add(bo);
            }
            cursor.close();
        }
        catch (Exception e) {
            return null;
        }

        return list;
    }
}
