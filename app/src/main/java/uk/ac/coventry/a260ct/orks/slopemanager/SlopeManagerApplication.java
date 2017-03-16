package uk.ac.coventry.a260ct.orks.slopemanager;

import android.app.Application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Freshollie on 03/03/2017.
 */

public class SlopeManagerApplication extends Application {
    public static int OPENING_TIME = 9; //24 hour clock

    private SlopeDatabase slopeDatabase;
    private LoginSessionManager loginSessionManager;

    private User observeCustomer;

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

    public void setObserveCustomer(User customer) {
        observeCustomer = customer;
    }

    public User getObserveCustomer() {
        return observeCustomer;
    }

    public static String dateToString(Date date) {
        return new SimpleDateFormat("yyyy-mm-dd", Locale.UK).format(date);
    }

    public static Date stringToDate(String dateString) {

        try {
            return new SimpleDateFormat("yyyy-mm-dd", Locale.UK).parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
