package uk.ac.coventry.a260ct.orks.slopemanager;

import android.content.Context;

import java.util.HashSet;

/**
 * Created by boldurbogdan on 01/03/2017.
 */
public class LoginManagerPackage {
    private User user;

    private SlopeManagerApplication application;
    private SlopeDatabase slopeDatabase;

    String permission;
    private static final String costumerPermission = "Costumer";
    private static final String instructorPermission="Instructor";
    private static final String operatorPersmission = "Operator";
    private static final String managerPersmission = "Manager";

    public LoginManagerPackage() {
        application = SlopeManagerApplication.getInstance();
        slopeDatabase = application.getSlopeDatabase();
    }

    public User getUser(int id) {
        if (id > -1) {
            user = slopeDatabase.getUserFromId(id);
        } else {
            user = null;
        }


        // TODO
        // HashSet<String> permissions = slopeDatabase.getPermissions(id);
        // if (permissions.contains(SlopeDatabase.COL_VIEW_BOOKINGS_PERMISSION)) {
        // ...

        return user;

    }

    public int requestUserIdFromCredentials(String username, String password) {
        return slopeDatabase.getUserIdFromCredentials(username, password);
    }
}
