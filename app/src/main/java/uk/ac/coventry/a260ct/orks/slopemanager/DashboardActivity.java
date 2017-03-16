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
        toolbar.setTitle(getString(R.string.dashboard_title));
        setSupportActionBar(toolbar);

        slopeManagerApplication = (SlopeManagerApplication) getApplication();
        loginSessionManager = slopeManagerApplication.getLoginSessionManager();
        user = loginSessionManager.getUserOrLogout();

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
        if (user instanceof Customer) {

        }
        findViewById(R.id.dashboard_slopemanager_buttons_card).setVisibility(View.GONE);
        findViewById(R.id.view_sessions_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BookingsActivity.class));
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
