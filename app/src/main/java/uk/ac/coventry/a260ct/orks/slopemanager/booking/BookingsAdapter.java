package uk.ac.coventry.a260ct.orks.slopemanager.booking;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;

import uk.ac.coventry.a260ct.orks.slopemanager.R;
import uk.ac.coventry.a260ct.orks.slopemanager.database.Booking;
import uk.ac.coventry.a260ct.orks.slopemanager.database.SkiSession;

/**
 * Created by Freshollie on 15/03/2017.
 */

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.BookingHolder> {

    private static final String TAG = BookingsAdapter.class.getSimpleName();

    private Booking[] bookings = new Booking[0];
    private BookingsActivity bookingsActivity;

    public static class BookingHolder extends RecyclerView.ViewHolder {
        public TextView bookingPartySize;
        public TextView bookingDate;
        public TextView bookingTime;
        public TextView bookingPaidStatus;
        public TextView bookingInstructor;
        public RelativeLayout bookingCardLayout;

        public BookingHolder(View v) {
            super(v);
            // Extract the UI objects from the view
            bookingDate = (TextView) v.findViewById(R.id.booking_date);
            bookingTime = (TextView) v.findViewById(R.id.booking_time);
            bookingPaidStatus = (TextView) v.findViewById(R.id.booking_paid_status);
            bookingInstructor = (TextView) v.findViewById(R.id.booking_instructor_name);
            bookingCardLayout = (RelativeLayout) v.findViewById(R.id.booking_card_layout);
            bookingPartySize = (TextView) v.findViewById(R.id.num_people_text);
        }
    }

    public BookingsAdapter(BookingsActivity parentActivity) {
        bookingsActivity = parentActivity;
    }

    /**
     * Create a card for every booking
     */
    @Override
    public BookingHolder onCreateViewHolder(ViewGroup parent,
                                            int viewType) {
        // Create the card from the card layout
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout_booking, parent, false);

        // Collect the UI from View
        BookingHolder vh = new BookingHolder(v);

        return vh;
    }

    /**
     * Called when a booking card is going to be displayed on screen,
     * assigns the information of that booking to the card
     * @param holder holds the UI elements
     * @param position position this card is in the list
     */
    @Override
    public void onBindViewHolder(final BookingHolder holder, int position) {
        // Get the booking for that position
        Booking booking = bookings[position];
        SkiSession session = booking.getSession();

        holder.bookingDate.setText(session.getDateString());
        holder.bookingTime.setText(session.getTimeString());

        String instructorString = bookingsActivity.getString(R.string.no_instructor);
        if (bookings[position].wantsInstructor()) {
            instructorString = bookingsActivity.getString(R.string.with_instructor);
        }

        holder.bookingInstructor.setText(instructorString);

        int paidStringResource = R.string.booking_unpaid;
        int paidColorResource = android.R.color.holo_red_light;

        if (booking.isPaid()) {
            paidStringResource = R.string.booking_paid;
            paidColorResource = android.R.color.holo_green_light;
        }

        holder.bookingPaidStatus.setText(bookingsActivity.getString(paidStringResource));
        holder.bookingPaidStatus.setTextColor(ContextCompat.getColor(bookingsActivity, paidColorResource));

        // When a booking card is clicked we need to tell the activity so we
        // can do something with that information
        holder.bookingCardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookingsActivity.onBookingClicked(bookings[holder.getAdapterPosition()].getId());
            }
        });

        holder.bookingPartySize.setText(
                bookingsActivity.getString(R.string.party_size_text, booking.getNumPeople())
        );
    }

    @Override
    public int getItemCount() {
        return bookings.length;
    }

    public void setBookings(Booking[] newBookings) {
        bookings = newBookings;
        notifyDataSetChanged();
    }

    public Booking[] getBookings() {
        return bookings;
    }
}