package com.aseupc.database;

/**
 * Created by AnasHel on 18-10-15.
 */
public class User_Web_Services {

    public static boolean ws_verifyCredentials(String email, String password){
        // call http://ec2-52-27-170-102.us-west-2.compute.amazonaws.com:8080/FlattitudeServer/flattitude/user/login/{email}/{password}

        return true;
    }

    public static boolean ws_registerUser(String email, String password, String firstname, String lastname, String phonenumber)
{

    return true;
}

}
