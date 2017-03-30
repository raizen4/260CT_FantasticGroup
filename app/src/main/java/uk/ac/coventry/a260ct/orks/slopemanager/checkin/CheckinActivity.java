package uk.ac.coventry.a260ct.orks.slopemanager.checkin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        application = (SlopeManagerApplication) getApplication();
        editText = (EditText) findViewById(R.id.checkin_text);
        spinner = (Spinner) findViewById(R.id.checkin_spinner);
        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (users.length <= i) {
                    return;
                }
                application.setObserveCustomer(users[i]);
                startActivity(new Intent(getApplication(), BookingsActivity.class));
            }


        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.checkin_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSearchPress();
            }
        });
    }

    private void onSearchPress() {
        Log.v(TAG, "On search press");
        ArrayList<String> spinnerStrings = new ArrayList<>();
        String[] names = editText.getText().toString().split(" ");
        if (names.length < 2) {
            return;
        }


        users = application.getSlopeDatabase().getUsersFromName(names[0], names[1]);
        for (User user : users) {
            spinnerStrings.add(user.getEmail());

        }
        if (spinnerStrings.size() < 1) {
            spinnerStrings.add("No user with this name");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerStrings);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
    }


}
