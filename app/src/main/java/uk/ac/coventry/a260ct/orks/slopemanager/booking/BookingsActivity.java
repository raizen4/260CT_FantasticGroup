package uk.ac.coventry.a260ct.orks.slopemanager.booking;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import uk.ac.coventry.a260ct.orks.slopemanager.R;
import uk.ac.coventry.a260ct.orks.slopemanager.SlopeManagerApplication;
import uk.ac.coventry.a260ct.orks.slopemanager.database.Booking;
import uk.ac.coventry.a260ct.orks.slopemanager.database.SlopeDatabase;
import uk.ac.coventry.a260ct.orks.slopemanager.database.User;

public class BookingsActivity extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();

    private SlopeManagerApplication application;

    private User customer;

    private SlopeDatabase database;

    private BookingsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.create_booking_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CreateBookingActivity.class));
            }
        });


        application = (SlopeManagerApplication) getApplication();
        database = application.getSlopeDatabase();

        customer = application.getObserveCustomer();

        if (customer == null) { // We are not observing a specific user
            customer = application.getLoginSessionManager().getUser();
        } else {
            getSupportActionBar()
                    .setTitle(
                            getString(R.string.title_activity_bookings)
                                    + " - " + customer.getFirstName()
                    );
        }

        if (customer == null) { // User is not even logged in
            application.getLoginSessionManager().launchLogin();
        } else {
            setupPage();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshAdapter();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();  return true;
        }

        return super.onOptionsItemSelected(item);
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
            Log.v(TAG, String.valueOf(DateUtils.isToday(bookingDate.getTime())));
            Log.v(TAG, SlopeManagerApplication.dateToString(new Date()));
            Log.v(TAG, SlopeManagerApplication.dateToString(bookingDate));
            if (DateUtils.isToday(bookingDate.getTime())
                    || bookingDate.getTime() > new Date().getTime()) {
                // Checks that the booking is not in the past
                newBookings.add(booking);
            }
        }

        return newBookings.toArray(new Booking[newBookings.size()]);
    }

    public void refreshAdapter() {
        Booking[] bookings = database.getBookingsForUser(customer);
        Log.v(TAG, Arrays.toString(bookings));

        bookings = filterBookings(bookings);
        Arrays.sort(bookings, new Comparator<Booking>() {
            @Override
            public int compare(Booking o1, Booking o2) {
                return o1.getSession().getDate().compareTo(o2.getSession().getDate());
            }
        });

        if (bookings.length > 0) {
            findViewById(R.id.no_bookings_text).setVisibility(View.GONE);
            adapter.setBookings(filterBookings(bookings));
        } else {
            findViewById(R.id.no_bookings_text).setVisibility(View.VISIBLE);
        }
    }

    public void setupPage() {
        RecyclerView bookingsRecyclerView =
                (RecyclerView) findViewById(R.id.bookings_recycler_view);

        bookingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BookingsAdapter(this);
        bookingsRecyclerView.setAdapter(adapter);
        refreshAdapter();
    }

    public void onBookingClicked(int id) {
        Log.v(TAG, "On booking clicked");
    }
}
