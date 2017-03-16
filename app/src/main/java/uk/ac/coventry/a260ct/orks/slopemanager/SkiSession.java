package uk.ac.coventry.a260ct.orks.slopemanager;

import java.text.DateFormat;
import java.util.Date;

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

    public String getTimeString() {
        // Converts slot to the minute it starts in the day
        int slotTimeMins =
                SlopeManagerApplication.OPENING_TIME * 60 + (SESSION_LENGTHS_MINS / 60) * getSlot();

        int hours = slotTimeMins / 60;
        int minutes = slotTimeMins % 60;

        return String.format("%d:%02d:00", hours, minutes);
    }

    public String getDateString() {
        return DateFormat.getInstance().format(getDate());
    }
}
