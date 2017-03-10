package uk.ac.coventry.a260ct.orks.slopemanager;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by boldurbogdan on 08/03/2017.
 */

public class UserFactory {

    public UserFactory(){}

    public static User getUser(int userType, HashMap<User.ATTRIBUTES, String> userDetails){
        User user=null;
        switch (userType){
            case 0:
                 user=new Costumer(userDetails);
                break;
            case 1:
                 user=new SlopeOperator(userDetails);
                break;
            case 2:
                user = new Costumer(userDetails);
                break;
            case 3:
                user = new Costumer(userDetails);//Manager here
                break;

        }
        return user;
    }
}
