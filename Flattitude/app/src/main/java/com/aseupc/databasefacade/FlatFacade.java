package com.aseupc.databasefacade;

import com.aseupc.Models.Flat;
import com.aseupc.database.Flat_Web_Services;
import com.aseupc.utility_REST.ResultContainer;

/**
 * Created by MetzoDell on 28-10-15.
 */
public class FlatFacade {

    public static ResultContainer<Flat> createFlat(Flat flat)
    {
    Flat_Web_Services Flat_Ws = new Flat_Web_Services();
    return Flat_Ws.ws_createFlat(flat);
    }
}
