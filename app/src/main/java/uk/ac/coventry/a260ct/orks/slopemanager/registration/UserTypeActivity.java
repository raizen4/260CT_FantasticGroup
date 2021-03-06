package uk.ac.coventry.a260ct.orks.slopemanager.registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import uk.ac.coventry.a260ct.orks.slopemanager.R;

public class UserTypeActivity extends AppCompatActivity {
    private Spinner spinnerType;
    private String type;
    private Button goToRegisteringAct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        goToRegisteringAct= (Button) findViewById(R.id.button_advance_to_register_act);
        spinnerType= (Spinner) findViewById(R.id.spinner_user_type);
        final ArrayList<String> typesOfUsers=new ArrayList<>();
        typesOfUsers.add(0,"Customer");
        typesOfUsers.add(1,"Instructor");
        typesOfUsers.add(2,"Operator");
        typesOfUsers.add(3,"Manager");
        ArrayAdapter<String>adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,typesOfUsers);
        spinnerType.setAdapter(adapter);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type=typesOfUsers.get(i);
                Toast.makeText(getApplicationContext(),type,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        goToRegisteringAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent=new Intent(UserTypeActivity.this,RegisteringActivity.class);
                newIntent.putExtra("type",type);
                Log.i("Type",type);
                startActivity(newIntent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
