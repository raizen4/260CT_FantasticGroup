package uk.ac.coventry.a260ct.orks.slopemanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

import uk.ac.coventry.a260ct.orks.slopemanager.database.SkiSession;

public class ViewPeopleBooked extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    SlopeManagerApplication application;
    Button button_pickdate;
    TextView dateText;
    int day, month, year;
    int dayFinal, monthFinal, yearFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        application = (SlopeManagerApplication) getApplication();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_people_booked);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        dateText = (TextView) findViewById(R.id.dateText);

        button_pickdate = (Button) findViewById(R.id.button_pickdate);
        button_pickdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(ViewPeopleBooked.this, ViewPeopleBooked.this,
                        year, month, day);
                datePickerDialog.show();
            }
        });


        Button button_viewpeople = (Button) findViewById(R.id.button_viewpeople);
        button_viewpeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SkiSession[] sessions = application.getSlopeDatabase().getSessionsForDate(SlopeManagerApplication.stringToDate(dateText.getText().toString()));


            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        yearFinal = year;
        monthFinal = month + 1;
        dayFinal = dayOfMonth;

        dateText.setText(yearFinal+"-" + (+monthFinal + 1) +"-"+dayFinal);
    }
}
