package uk.ac.coventry.a260ct.orks.slopemanager;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by boldurbogdan on 08/03/2017.
 */

public class UserFactory {

    public UserFactory(){}

<<<<<<< HEAD
<<<<<<< HEAD
    public User getUser(int type, HashMap<String,String>userDetails){
        User user=null;
        switch (type){
            case 0:
                 //user=new Member(userDetails);
=======
    public static User getUser(int userType, HashMap<User.ATTRIBUTES, String> userDetails){
        User user=null;
=======
    public static User getUser(int userType, HashMap<User.ATTRIBUTES, String> userDetails){
        User user=null;
>>>>>>> master
        switch (userType){
            case 0:
                 user=new Member(userDetails);
>>>>>>> master
                break;
            case 1:
                 user=new SlopeOperator(userDetails);
                break;
            case 2:
                user = new NormalUser(userDetails);
                break;
            case 3:
                user = new Member(userDetails);
                break;

        }
        return user;
    }
}
