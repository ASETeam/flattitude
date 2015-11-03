package com.aseupc.databasefacade;
import android.util.Log;

import com.aseupc.InternalDatabase.DAO.UserDAO;
import com.aseupc.Models.User;
import com.aseupc.database.*;
import com.aseupc.utility_REST.ResultContainer;

/**
 * Created by AnasHel on 16-10-15.
 * This class serves as a facade between the database and the application layer. This allows for a flexible implementation of the
 * database transactions. (See Facade Design Pattern)
 */
public class UserFacade {

    public static ResultContainer<User> verifyCredentials(String email, String password)
    {
        //call function in webservice
        User_Web_Services WS_user = new User_Web_Services();
        ResultContainer<User> result = WS_user.ws_verifyCredentials(email, password);
        if (result.getSucces() == true)
            Log.i("In facade loggin", "TRUE" );
        return result;
    }


    public static ResultContainer<User> registerUser(String email, String password, String firstname, String lastname, String phonenumber)
    {
        User_Web_Services User_WS = new User_Web_Services();
        ResultContainer<User> result  = User_WS.ws_registerUser(email, password, firstname, lastname, phonenumber);

        // Add notification to server

        return result;
    }

}
