package uk.ac.coventry.a260ct.orks.slopemanager.database;

/**
 * Created by Freshollie on 15/03/2017.
 */

public class Booking {
    private final int numPeople;
    private int id;
    private SkiSession session;
    private boolean instructor;
    private boolean payStatus;

    public Booking(int id, SkiSession session, int numPeople, boolean wantsInstructor, boolean payStatus) {
        this.id = id;
        this.session = session;
        this.instructor = wantsInstructor;
        this.payStatus = payStatus;
        this.numPeople = numPeople;
    }

    public SkiSession getSession() {
        return session;
    }

    public int getId() {
        return id;
    }

    public boolean wantsInstructor() {
        return instructor;
    }

    public boolean isPaid() {
        return payStatus;
    }

    public int getNumPeople() {
        return numPeople;
    }
}
