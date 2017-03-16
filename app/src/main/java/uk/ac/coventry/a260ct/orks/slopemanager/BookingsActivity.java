package uk.ac.coventry.a260ct.orks.slopemanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

public class BookingsActivity extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();

    private SlopeManagerApplication application;

    private User customer;

    private SlopeDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        application = (SlopeManagerApplication) getApplication();
        database = application.getSlopeDatabase();

        customer = application.getObserveCustomer();

        if (customer == null) { // We are not observing a specific user
            customer = application.getLoginSessionManager().getUser();
        }

        if (customer == null) { // User is not even logged in
            application.getLoginSessionManager().launchLogin();
        } else {
            setupPage();
        }
    }

    /**
     * Filters the list of bookings. For now just make sure the bookings were not yesterday
     * @param bookings
     * @return
     */
    public Booking[] filterBookings(Booking[] bookings) {
        ArrayList<Booking> newBookings = new ArrayList<>();

        for(Booking booking: bookings) {
            Date bookingDate = booking.getSession().getDate();
            if (DateUtils.isToday(bookingDate.getTime())
                    || bookingDate.getTime() > new Date().getTime()) {
                // Checks that the booking is not in the past
                newBookings.add(booking);
            }
        }

        return newBookings.toArray(new Booking[newBookings.size()]);
    }

    public void setupPage() {
        RecyclerView bookingsRecyclerView =
                (RecyclerView) findViewById(R.id.bookings_recycler_view);

        bookingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        BookingsAdapter adapter = new BookingsAdapter(this);
        Booking[] bookings = database.getBookingsForUser(customer);

        adapter.setBookings(filterBookings(bookings));
        bookingsRecyclerView.setAdapter(adapter);
    }

    public void onBookingClicked(int id) {
        Log.v(TAG, "On booking clicked");
    }
}
