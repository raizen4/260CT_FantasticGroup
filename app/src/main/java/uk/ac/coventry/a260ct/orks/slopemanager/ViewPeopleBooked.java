package uk.ac.coventry.a260ct.orks.slopemanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import uk.ac.coventry.a260ct.orks.slopemanager.database.SkiSession;
import uk.ac.coventry.a260ct.orks.slopemanager.database.SlopeDatabase;

public class ViewPeopleBooked extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    SlopeManagerApplication application;
    Button button_pickdate;
    TextView dateText;
    int day, month, year;
    int dayFinal, monthFinal, yearFinal;
    TextView nobooks;
    String strnobooks;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        application = (SlopeManagerApplication) getApplication();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_people_booked);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final ListView listview_people = (ListView) findViewById(R.id.listview_people);
        dateText = (TextView) findViewById(R.id.dateText);
        nobooks = (TextView) findViewById(R.id.nobooks);

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
                nobooks.setText("");
            }
        });

        strnobooks = "No books for today!";
        Button button_viewpeople = (Button) findViewById(R.id.button_viewpeople);
        button_viewpeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> namesToShowInTheListview=new ArrayList<String>();
                SlopeDatabase database= application.getSlopeDatabase();
                try {
                    namesToShowInTheListview = database.getPeopleForSession(SlopeManagerApplication.stringToDate(dateText.getText().toString()));
                }
                catch (Exception e){
                    //silently fail
                    e.printStackTrace();

                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        getApplicationContext(), android.R.layout.simple_list_item_1,namesToShowInTheListview);
                listview_people.setAdapter(adapter);

                if(namesToShowInTheListview.isEmpty())
                    nobooks.setText(strnobooks);


            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        yearFinal = year;
        monthFinal = month + 1;
        dayFinal = dayOfMonth;

        dateText.setText(yearFinal + "-" + monthFinal + "-" + dayFinal);
    }
}
