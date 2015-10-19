package com.aseupc.databasefacade;
import com.aseupc.database.*;

/**
 * Created by AnasHel on 16-10-15.
 * This class serves as a facade between the database and the application layer. This allows for a flexible implementation of the
 * database transactions. (See Facade Design Pattern)
 */
public class UserFacade {

public static boolean verifyCredentials(String email, String password)
{
    //call function in webservice
    User_Web_Services WS_user = new User_Web_Services();
    boolean result = WS_user.ws_verifyCredentials(email, password);



    return result;
}


public static boolean registerUser(String email, String password, String firstname, String lastname, String phonenumber)
{
    boolean result = User_Web_Services.ws_registerUser(email, password, firstname, lastname, phonenumber);
    //call function in webservice

    return result;
}

}
