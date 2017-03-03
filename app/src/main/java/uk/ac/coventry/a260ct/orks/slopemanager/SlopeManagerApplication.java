package uk.ac.coventry.a260ct.orks.slopemanager;

import android.app.Application;

/**
 * Created by Freshollie on 03/03/2017.
 */

public class SlopeManagerApplication extends Application{
    private Database database;
    private LoginSessionManager loginSessionManager;

    private static SlopeManagerApplication INSTANCE;

    @Override
    public void onCreate() {
        database = new Database(getApplicationContext());
        loginSessionManager = new LoginSessionManager(getApplicationContext());
    }

    public static SlopeManagerApplication getInstance() {
        return INSTANCE;
    }

    public Database getDatabase() {
        return database;
    }

    public LoginSessionManager getLoginSessionManager() {
        return loginSessionManager;
    }
}
