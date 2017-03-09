package uk.ac.coventry.a260ct.orks.slopemanager;

import java.util.HashMap;

/**
 * Created by boldurbogdan on 08/03/2017.
 */

public class UserFactory {

    public UserFactory(){}

    public User getUser(int type, HashMap<String,String>userDetails){
        User user=null;
        switch (type){
            case 0:
                 //user=new Member(userDetails);
                break;
            case 1:
                 user=new SlopeOperator(userDetails);
                break;
            case 2:
                user = new NormalUser(userDetails);
                break;

        }
        return user;
    }
}
