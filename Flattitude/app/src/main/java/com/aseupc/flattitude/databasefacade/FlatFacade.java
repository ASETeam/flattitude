package com.aseupc.flattitude.databasefacade;

import com.aseupc.flattitude.Models.Flat;
import com.aseupc.flattitude.database.Flat_Web_Services;
import com.aseupc.flattitude.utility_REST.ResultContainer;

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
