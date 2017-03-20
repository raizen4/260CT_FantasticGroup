package uk.ac.coventry.a260ct.orks.slopemanager.database;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import uk.ac.coventry.a260ct.orks.slopemanager.SlopeManagerApplication;

/**
 * Created by Freshollie on 15/03/2017.
 */

public class SkiSession {
    public static int SESSION_LENGTHS_MINS = 60;

    private int id;
    private Date date;
    private int slot;

    public SkiSession(int id, Date date, int slot) {
        this.id = id;
        this.date = date;
        this.slot = slot;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public int getSlot() {
        return slot;
    }

    /**
     * Get the session time as a string
     * @return formatted time string
     */
    public String getTimeString() {
        // Converts slot to the minute it starts in the day
        int slotTimeMins =
                SlopeManagerApplication.OPENING_TIME * 60 + (SESSION_LENGTHS_MINS / 60) * getSlot();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, slotTimeMins/60);
        cal.set(Calendar.MINUTE, slotTimeMins % 60);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(cal.getTime());
    }

    public String getDateString() {
        return DateFormat.getDateInstance().format(getDate());
    }
}
