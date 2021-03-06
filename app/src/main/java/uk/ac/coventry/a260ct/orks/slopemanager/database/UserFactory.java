package uk.ac.coventry.a260ct.orks.slopemanager.database;

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
                 user=new Customer(userDetails);
                break;
            case 1:
                 user=new SlopeOperator(userDetails);
                break;
            case 2:
                user = new Instructor(userDetails);
                break;
            case 3:
                user = new Manager(userDetails);
                break;

        }
        return user;
    }

    public static int getUserType(User user) {
        if (user instanceof Customer) {
            return 0;
        } else if (user instanceof Instructor) {
            return 1;
        } else if (user instanceof SlopeOperator) {
            return 2;
        } else if (user instanceof Manager) {
            return 3;
        } else {
            return -1;
        }
    }
}
