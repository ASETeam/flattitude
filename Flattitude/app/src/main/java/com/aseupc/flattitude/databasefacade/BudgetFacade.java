package com.aseupc.flattitude.databasefacade;

import com.aseupc.flattitude.Models.BudgetOperation;
import com.aseupc.flattitude.Models.BudgetOperationDBAdapter;
import com.aseupc.flattitude.database.Budget_Web_Service;

import java.util.ArrayList;

/**
 * Created by Vavou on 13/01/2016.
 * Facade to access the database in order to do some budget requests
 */
public class BudgetFacade {

    public static Double[] getBalances(String flatId, String userId) {
        return new Budget_Web_Service().getBalances(flatId, userId);
    }

    public static boolean addBudgetOperation(BudgetOperation bo) {
        return new Budget_Web_Service().addBudgetOperation(bo);
    }

    public static ArrayList<BudgetOperationDBAdapter> retrieveBudgetOperations(String flatId) {
        return new Budget_Web_Service().retrieveBudgetOperations(flatId);
    }
}
