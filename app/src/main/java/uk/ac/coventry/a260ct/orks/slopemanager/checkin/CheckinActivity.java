package uk.ac.coventry.a260ct.orks.slopemanager.checkin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;


import uk.ac.coventry.a260ct.orks.slopemanager.R;
import uk.ac.coventry.a260ct.orks.slopemanager.SlopeManagerApplication;
import uk.ac.coventry.a260ct.orks.slopemanager.booking.BookingsActivity;
import uk.ac.coventry.a260ct.orks.slopemanager.database.User;


public class CheckinActivity extends AppCompatActivity {

    private static final String TAG = CheckinActivity.class.getSimpleName();
    private Spinner spinner;
    private EditText editText;
    private Button button;
    private User[] users;
    private SlopeManagerApplication application;
    private AutoCompleteTextView autocomplete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        application = (SlopeManagerApplication) getApplication();
        autocomplete = (AutoCompleteTextView) findViewById(R.id.checkin_text);


        Log.v(TAG, "On search press");

        ArrayList<String> names = new ArrayList<>();
        users = application.getSlopeDatabase().getAllUsers();
        for (User user : users) {
            names.add(user.getFirstName() + " " + user.getSurname());


            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);
            autocomplete.setAdapter(adapter);
            autocomplete.setThreshold(0);


            autocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (users.length <= i) {
                        return;
                    } else {
                        application.setObserveCustomer(users[i]);
                        startActivity(new Intent(getApplication(), BookingsActivity.class));
                    }
                }
            });


        }
    }
}