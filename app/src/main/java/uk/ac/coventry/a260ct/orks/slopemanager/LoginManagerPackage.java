package uk.ac.coventry.a260ct.orks.slopemanager;

import android.content.Context;

/**
 * Created by boldurbogdan on 01/03/2017.
 */
public class LoginManagerPackage {
    private User user;
    private Database database;
    String permission;
    private static final String costumerPermission = "Costumer";
    private static final String memberPersmission = "Member";
    private static final String operatorPersmission = "Operator";
    private static final String managerPersmission = "Manager";

    public LoginManagerPackage(Context c) {
        this.database = new Database(c);
    }

    public User getUser(int id) {
        if (id != 0) {
            return new User(id, "Test", "Lol", "07485747375", "test@google.com");
        } else {
            return null;
        }
        /*
        // TODO
        String persmissionRetrieved = database.getId(id);
        if (persmissionRetrieved.matches(costumerPermission)) {
            // user=new make the correct type here
        }
        return null;
        */
    }
}
