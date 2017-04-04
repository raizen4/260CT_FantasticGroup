package uk.ac.coventry.a260ct.orks.slopemanager.booking;

import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import uk.ac.coventry.a260ct.orks.slopemanager.R;
import uk.ac.coventry.a260ct.orks.slopemanager.SlopeManagerApplication;
import uk.ac.coventry.a260ct.orks.slopemanager.database.SkiSession;
import uk.ac.coventry.a260ct.orks.slopemanager.database.User;
import uk.ac.coventry.a260ct.orks.slopemanager.registration.DatePickerFragment;

public class CreateBookingActivity extends AppCompatActivity {

    private static final String TAG = CreateBookingActivity.class.getSimpleName();
    EditText dateInput;
    Spinner sessionTimeSpinner;
    CheckBox payNowCheckbox;
    CheckBox instructorCheckbox;
    TextView totalCost;
    SlopeManagerApplication application;
    SkiSession[] sessions;
    private User customer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_booking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dateInput = (EditText) findViewById(R.id.booking_date_input);
        sessionTimeSpinner = (Spinner) findViewById(R.id.booktime_spinner);
        payNowCheckbox = (CheckBox) findViewById(R.id.pay_now_checkbox);
        instructorCheckbox = (CheckBox) findViewById(R.id.instructor_checkbox);
        application = (SlopeManagerApplication) getApplication();
        customer = application.getObserveCustomer();
        totalCost = (TextView) findViewById(R.id.total_cost_label);

        if (customer == null) { // We are not observing a specific user
            customer = application.getLoginSessionManager().getUser();
        } else {
            getSupportActionBar()
                    .setTitle(
                            getString(R.string.title_activity_create_booking)
                                    + " - " + customer.getFirstName()
                    );
        }
        setupActions();
    }

    private void showDatepicker() {
        DatePickerFragment picker = new DatePickerFragment(dateInput);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        picker.setFutureOnly(true);
        picker.show(transaction, "Date Picker");
    }

    private void setupActions() {
        dateInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                showDatepicker();
            }
        });

        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatepicker();
            }
        });
        dateInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                onDateSet();
            }
        });

        findViewById(R.id.book_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookSession();
                }
            });
        instructorCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(instructorCheckbox.isChecked()){
                    totalCost.setText("£" + String.valueOf(SlopeManagerApplication.INSTRUCTOR_COST
                            + SlopeManagerApplication.BASIC_COST));
                }else{
                    totalCost.setText("£" + String.valueOf(SlopeManagerApplication.BASIC_COST));
                }
            }
        });


    }



    private void onDateSet() {
        Log.v(TAG, "On date set");
        Date sessionDate = SlopeManagerApplication.stringToDate(dateInput.getText().toString());
        ArrayList<String> spinnerStrings = new ArrayList<>();
        if(sessionDate == null){
            return;
        }
        sessions = application.getSlopeDatabase().getSessionsForDate(sessionDate);
        for(SkiSession session: sessions){
            spinnerStrings.add(session.getTimeString());
        }
        if(spinnerStrings.size() < 1) {
            spinnerStrings.add(getString(R.string.no_sessions));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerStrings);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sessionTimeSpinner.setAdapter(adapter);
        sessionTimeSpinner.setSelection(0);
        totalCost.setText("£" + String.valueOf(SlopeManagerApplication.BASIC_COST));
    }

    private void bookSession() {
        if(sessions.length < 1){
            return;
        }
        SkiSession session = sessions[(int) sessionTimeSpinner.getSelectedItemId()];
        application.getSlopeDatabase()
                .createBooking(
                        session.getId(),
                        customer.getId(),
                        false,
                        instructorCheckbox.isChecked()
                );
        finish();
        };

}



