package uk.ac.coventry.a260ct.orks.slopemanager.booking;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.MonitoringInstrumentation;

import org.junit.Rule;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import uk.ac.coventry.a260ct.orks.slopemanager.R;
import uk.ac.coventry.a260ct.orks.slopemanager.SlopeManagerApplication;
import uk.ac.coventry.a260ct.orks.slopemanager.TestUtils;
import uk.ac.coventry.a260ct.orks.slopemanager.dashboard.DashboardActivity;
import uk.ac.coventry.a260ct.orks.slopemanager.database.Booking;
import uk.ac.coventry.a260ct.orks.slopemanager.database.Customer;
import uk.ac.coventry.a260ct.orks.slopemanager.database.SkiSession;
import uk.ac.coventry.a260ct.orks.slopemanager.database.SlopeDatabase;
import uk.ac.coventry.a260ct.orks.slopemanager.database.User;
import uk.ac.coventry.a260ct.orks.slopemanager.login.LoginActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.*;
import static uk.ac.coventry.a260ct.orks.slopemanager.TestUtils.getApplication;

/**
 * Created by freshollie on 30/03/17.
 */
@SmallTest
public class BookingsActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
            LoginActivity.class);

    private User getObserverCustomer() {
        SlopeManagerApplication application = TestUtils.getApplication();

        User customer = application.getObserveCustomer();

        if (customer == null) { // We are not observing a specific user
            customer = application.getLoginSessionManager().getUser();
        }
        return customer;
    }

    private void setObserverCustomer(User user) {
        SlopeManagerApplication application = TestUtils.getApplication();

        application.setObserveCustomer(user);
    }

    private void removeAllBookings() {
        SlopeDatabase database = TestUtils.getDatabase();
        Booking[] bookings = database.getBookingsForUser(getObserverCustomer());

        for (Booking booking: bookings) {
            database.removeBooking(booking.getId());
        }
    }

    /**
     * Books a random session making sure the user
     * is not already booked for that session
     */
    public Booking bookRandomSession() {
        Calendar calendar = Calendar.getInstance();
        Random random = new Random();

        User user = getObserverCustomer();
        SlopeDatabase database = TestUtils.getDatabase();

        SkiSession session;
        do {
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, random.nextInt(100));  // random day
            session = database.getSessionFromDateAndSlot(calendar.getTime(), random.nextInt(10));

            if (session == null) {
                continue;
            }

            for (Booking booking: database.getBookingsForUser(user)) {
                if (booking.getSession().getId() == session.getId()) {
                    session = null;
                    break;
                }
            }

        } while (session == null);

        int bookingId =
                database.createBooking(
                        session.getId(),
                        user.getId(),
                        random.nextBoolean(),
                        random.nextBoolean()
                );

        return database.getBookingFromId(bookingId);
    }

    public void checkForBooking(Booking booking) {
        onView(withId(R.id.bookings_recycler_view)).check(matches(isDisplayed()))
                .perform(RecyclerViewActions.actionOnItem(
                        allOf(
                                hasDescendant(withText(booking.getSession().getDateString())),
                                hasDescendant(withText(booking.getSession().getTimeString())),
                                hasDescendant(withText(
                                        booking.isPaid() ?
                                                TestUtils.getString(R.string.booking_paid):
                                                TestUtils.getString(R.string.booking_unpaid)
                                )),
                                hasDescendant(withText(
                                        booking.wantsInstructor() ?
                                                TestUtils.getString(R.string.with_instructor):
                                                TestUtils.getString(R.string.no_instructor)
                                ))
                        ),
                        click()));
    }

    @Test
    public void loadCreateBookingScreen_test() {
        loadBookingsScreen_test();
        onView(withId(R.id.create_booking_fab)).perform(click());

        onView(withId(android.R.id.button1)).check(matches(isDisplayed()));
    }

    @Test
    public void loadBookingsScreen_test() {
        TestUtils.launchValidLogin();

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.view_sessions_button), withText("My bookings"),
                        withParent(allOf(withId(R.id.dashboard_content),
                                withParent(withId(R.id.dashboard_user_buttons_card)))),
                        isDisplayed()));
        appCompatButton.perform(click());

        onView(withId(R.id.create_booking_fab)).check(matches(isDisplayed()));
    }

    @Test
    public void noBookings_test() {
        Instrumentation.ActivityMonitor monitor =
                getInstrumentation()
                        .addMonitor(BookingsActivity.class.getName(), null, false);

        loadBookingsScreen_test();

        final BookingsActivity activity = (BookingsActivity)
            getInstrumentation().waitForMonitorWithTimeout(monitor, 5);


        removeAllBookings();

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                activity.recreate();
            }
        });

        onView(withId(R.id.no_bookings_text)).check(matches(isDisplayed()));
    }

    @Test
    public void displayOneBookingFirstSessionToday_test() {
        Instrumentation.ActivityMonitor monitor =
                getInstrumentation()
                        .addMonitor(BookingsActivity.class.getName(), null, false);

        loadBookingsScreen_test();

        final BookingsActivity activity = (BookingsActivity)
                getInstrumentation().waitForMonitorWithTimeout(monitor, 5);

        removeAllBookings();

        SlopeDatabase database = TestUtils.getDatabase();

        Date date = new Date();

        final SkiSession session = database.getSessionsForDate(date)[0];

        Booking booking =
                database.getBookingFromId(
                        database.createBooking(
                                session.getId(),
                                getObserverCustomer().getId(),
                                false,
                                false
                        ));

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                activity.recreate();
            }
        });

        checkForBooking(booking);
    }

    @Test
    public void checkNewRandomBookings_test() {
        ArrayList<Booking> bookings = new ArrayList<>();

        Random random = new Random();

        removeAllBookings();

        Instrumentation.ActivityMonitor monitor =
                getInstrumentation()
                        .addMonitor(BookingsActivity.class.getName(), null, false);

        loadBookingsScreen_test();

        final BookingsActivity activity = (BookingsActivity)
                getInstrumentation().waitForMonitorWithTimeout(monitor, 5);

        for (int j = 0; j < (random.nextInt(30) + 5); j++) {
            bookings.add(bookRandomSession());
        }

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                activity.recreate();
            }
        });

        for (Booking booking: bookings) {
            checkForBooking(booking);
        }
    }
}