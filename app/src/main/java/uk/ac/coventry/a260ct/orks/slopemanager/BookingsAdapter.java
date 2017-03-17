package uk.ac.coventry.a260ct.orks.slopemanager;

import android.support.v4.content.ContextCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by Freshollie on 15/03/2017.
 */

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.BookingHolder> {

    private static final String TAG = BookingsAdapter.class.getSimpleName();
    private HashMap<String, Integer> typeColours = new HashMap<>();

    private Booking[] bookings = new Booking[0];
    private BookingsActivity bookingsActivity;

    public static class BookingHolder extends RecyclerView.ViewHolder {
        public TextView bookingDate;
        public TextView bookingTime;
        public TextView bookingPaidStatus;
        public TextView bookingInstructor;
        public RelativeLayout bookingCardLayout;

        public BookingHolder(View v) {
            super(v);
            bookingDate = (TextView) v.findViewById(R.id.booking_date);
            bookingTime = (TextView) v.findViewById(R.id.booking_time);
            bookingPaidStatus = (TextView) v.findViewById(R.id.booking_paid_status);
            bookingInstructor = (TextView) v.findViewById(R.id.booking_instructor_name);
            bookingCardLayout = (RelativeLayout) v.findViewById(R.id.booking_card_layout);
        }
    }

    public BookingsAdapter(BookingsActivity parentActivity) {
        bookingsActivity = parentActivity;
    }

    @Override
    public BookingHolder onCreateViewHolder(ViewGroup parent,
                                            int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout_booking, parent, false);

        BookingHolder vh = new BookingHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final BookingHolder holder, int position) {
        Booking booking = bookings[position];
        SkiSession session = booking.getSession();

        holder.bookingDate.setText(session.getDateString());
        holder.bookingTime.setText(session.getTimeString());

        String instructorString = "No Instructor";
        if (bookings[position].wantsInstructor()) {
            instructorString = "With Instructor";
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


        holder.bookingCardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookingsActivity.onBookingClicked(bookings[holder.getAdapterPosition()].getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookings.length;
    }

    public void setBookings(Booking[] newBookings) {
        bookings = newBookings;
        notifyDataSetChanged();
    }
}