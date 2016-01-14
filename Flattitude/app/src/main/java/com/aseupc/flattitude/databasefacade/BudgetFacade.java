package com.aseupc.flattitude.databasefacade;

import com.aseupc.flattitude.database.Budget_Web_Service;

/**
 * Created by Vavou on 13/01/2016.
 * Facade to access the database in order to do some budget requests
 */
public class BudgetFacade {

    public static Double[] getBalances(String flatId, String userId) {
        return new Budget_Web_Service().getBalances(flatId, userId);
    }

    public static boolean putMoneyOnCommon(String flatId, String userId, String amount, String description) {
        return new Budget_Web_Service().putMoneyOnCommon(flatId, userId, amount, description);
    }
}
