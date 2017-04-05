package uk.ac.coventry.a260ct.orks.slopemanager.checkin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;


import uk.ac.coventry.a260ct.orks.slopemanager.R;
import uk.ac.coventry.a260ct.orks.slopemanager.SlopeManagerApplication;
import uk.ac.coventry.a260ct.orks.slopemanager.booking.BookingsActivity;
import uk.ac.coventry.a260ct.orks.slopemanager.booking.BookingsAdapter;
import uk.ac.coventry.a260ct.orks.slopemanager.database.Booking;
import uk.ac.coventry.a260ct.orks.slopemanager.database.Customer;
import uk.ac.coventry.a260ct.orks.slopemanager.database.SlopeDatabase;
import uk.ac.coventry.a260ct.orks.slopemanager.database.User;


public class CheckinActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    private User[] users;
    private SlopeManagerApplication application;
    private AutoCompleteTextView autocompleteView;

    Button checkinButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        application = (SlopeManagerApplication) getApplication();
        autocompleteView = (AutoCompleteTextView) findViewById(R.id.checkin_text);
        checkinButton = (Button) findViewById(R.id.checkin_button);

        checkinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBookings();
            }
        });

        Log.v(TAG, "On search press");
        final ArrayList<String> names = new ArrayList<>();
        users = application.getSlopeDatabase().getAllUsers();
        for (User user : users) {
            names.add(user.getFirstName() + " " + user.getSurname() + " - " + user.getEmail());


            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);
            autocompleteView.setAdapter(adapter);
            autocompleteView.setThreshold(0);

            autocompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.v(TAG, String.valueOf(i));
                    if (users.length <= i) {
                        return;
                    } else {
                        String email = ((TextView) view).getText().toString().split(" - ")[1];
                        Log.v(TAG, email);
                        User user = getUserFromEmail(email);
                        application.setObserveCustomer(user);
                        openBookings();

                    }
                }
            });
        }
    }

    public User getUserFromEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }

        return null;
    }

    public void openBookings() {
        Log.v(TAG, "Open Bookings");

        String autocompleteText = autocompleteView.getText().toString();

        if (autocompleteText.isEmpty()) {
            Toast.makeText(getApplicationContext(), "No details searched", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(getApplication(), BookingsActivity.class));

        }
    }
}

