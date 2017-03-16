package uk.ac.coventry.a260ct.orks.slopemanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by Freshollie on 01/03/2017.
 */

public class DashboardActivity extends AppCompatActivity {
    private static final String TAG = DashboardActivity.class.getSimpleName();

    private LoginSessionManager loginSessionManager;
    private User user;

    private SlopeManagerApplication slopeManagerApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.dashboard_activity_toolbar);
        String Title = "";
        setSupportActionBar(toolbar);

        slopeManagerApplication = (SlopeManagerApplication) getApplication();
        loginSessionManager = slopeManagerApplication.getLoginSessionManager();
        user = loginSessionManager.getUserOrLogout();

        switch(UserFactory.getUserType(user)){
            case 0:
                Title = " - Customer";
                break;
            case 1:
                Title = " - Intrusctor";
                break;
            case 2:
                Title = " - Slope Operator";
                break;
            case 3:
                Title = " - Manager";
                break;
        }
        getSupportActionBar().setTitle(getString(R.string.dashboard_title) + Title);

        if (user == null) {
            loginSessionManager.launchLogin();
        } else {
            setupOptions();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        user = loginSessionManager.getUserOrLogout();
    }

    public void setupOptions() {
        switch(UserFactory.getUserType(user)) {
            case 0:
                findViewById(R.id.dashboard_slopeoperator_buttons_card).setVisibility(View.GONE);
                findViewById(R.id.dashboard_instructor_buttons_card).setVisibility(View.GONE);
                findViewById(R.id.dashboard_slopemanager_buttons_card).setVisibility(View.GONE);
                break;
            case 1:
                findViewById(R.id.dashboard_slopeoperator_buttons_card).setVisibility(View.GONE);
                findViewById(R.id.dashboard_slopemanager_buttons_card).setVisibility(View.GONE);
                break;
            case 2:
                findViewById(R.id.dashboard_instructor_buttons_card).setVisibility(View.GONE);
                findViewById(R.id.dashboard_slopemanager_buttons_card).setVisibility(View.GONE);
                break;
        }
        findViewById(R.id.view_sessions_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BookingsActivity.class));
            }
        });

        findViewById(R.id.personal_details_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getApplicationContext(), ViewDetailsActivity.class));
            }
        });
        findViewById(R.id.view_members_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getApplicationContext(), ViewMembers.class));
            }
        });
        findViewById(R.id.view_slope_schedule_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getApplicationContext(), ViewSlopeSchedule.class));
            }
        });
        findViewById(R.id.view_instructors_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getApplicationContext(), ViewInstructors.class));
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            loginSessionManager.logout();
            loginSessionManager.launchLogin();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
