package uk.ac.coventry.a260ct.orks.slopemanager;

import android.annotation.SuppressLint;
import android.app.Application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.ac.coventry.a260ct.orks.slopemanager.login.LoginSessionManager;
import uk.ac.coventry.a260ct.orks.slopemanager.database.SlopeDatabase;
import uk.ac.coventry.a260ct.orks.slopemanager.database.User;

/**
 * Created by Freshollie on 03/03/2017.
 */

public class SlopeManagerApplication extends Application {
    public static String DATE_FORMAT = "yyyy-MM-dd";
    public static int OPENING_TIME = 9; //24 hour clock

    private SlopeDatabase slopeDatabase;
    private LoginSessionManager loginSessionManager;

    private User observeCustomer;

    private static SlopeManagerApplication INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        slopeDatabase = new SlopeDatabase(getApplicationContext());
        loginSessionManager = new LoginSessionManager(getApplicationContext());
    }

    public static SlopeManagerApplication getInstance() {
        return INSTANCE;
    }

    public static int INSTRUCTOR_COST = 15; //cost of the instructor per hour in pounds
    public static int BASIC_COST = 20; //cost of session without instructor

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

    @SuppressLint("SimpleDateFormat")
    public static String dateToString(Date date) {
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }

    @SuppressLint("SimpleDateFormat")
    public static Date stringToDate(String dateString) {

        try {
            return new SimpleDateFormat(DATE_FORMAT).parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
