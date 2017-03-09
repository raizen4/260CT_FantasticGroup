package uk.ac.coventry.a260ct.orks.slopemanager;

import java.util.HashMap;

/**
 * Created by boldurbogdan on 08/03/2017.
 */

public class UserFactory {

    public UserFactory(){}

    public static User getUser(String type, HashMap<String,String>userDetails){
        User user=null;
        switch (type){
            case "Member":
                 user=new Member(userDetails);
                break;
            case "SlopeOperator":
                 user=new SlopeOperator(userDetails);
                break;
            case "normalUser":
                user = new NormalUser(userDetails);
                break;

        }
        return user;
    }
}
