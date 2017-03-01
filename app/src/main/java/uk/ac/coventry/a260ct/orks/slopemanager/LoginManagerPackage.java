package uk.ac.coventry.a260ct.orks.slopemanager;

import android.content.Context;

/**
 * Created by boldurbogdan on 01/03/2017.
 */
public class LoginManagerPackage {
    private User user;
    private Database database;
    String permission;
    private static final String costumerPermission="Costumer";
    private static final String memberPersmission="Member";
    private static final String operatorPersmission="Operator";
    private static final String managerPersmission="Manager";

    public LoginManagerPackage(Context c){
        this.database=new Database(c);
    }
    public User getUser(int id){
        String persmissionRetrieved=database.getUserPermission(id);
        if(persmissionRetrieved.matches(costumerPermission)){
            user=new
        }
    }
}
