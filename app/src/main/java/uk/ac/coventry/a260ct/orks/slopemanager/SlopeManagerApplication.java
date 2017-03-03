package uk.ac.coventry.a260ct.orks.slopemanager;

import android.app.Application;

/**
 * Created by Freshollie on 03/03/2017.
 */

public class SlopeManagerApplication extends Application{
    private SlopeDatabase slopeDatabase;
    private LoginSessionManager loginSessionManager;

    private static SlopeManagerApplication INSTANCE;

    @Override
    public void onCreate() {
        INSTANCE = this;
        slopeDatabase = new SlopeDatabase(getApplicationContext());
        loginSessionManager = new LoginSessionManager(getApplicationContext());
    }

    public static SlopeManagerApplication getInstance() {
        return INSTANCE;
    }

    public SlopeDatabase getSlopeDatabase() {
        return slopeDatabase;
    }

    public LoginSessionManager getLoginSessionManager() {
        return loginSessionManager;
    }
}
