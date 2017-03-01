package uk.ac.coventry.a260ct.orks.slopemanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Freshollie on 01/03/2017.
 */

public class DashboardActivity extends AppCompatActivity {
    private static final String TAG = DashboardActivity.class.getSimpleName();

    private LoginSessionManager loginSessionManager;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.dashboard_activity_toolbar);
        setSupportActionBar(toolbar);

        loginSessionManager = LoginSessionManager.getInstance(this);
        user = loginSessionManager.getUser(this);

        if (user == null) { // No user logged in so go to login screen
            Log.v(TAG, "User not logged in, launching login");
            LoginSessionManager.launchLogin(this);
        }
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
            loginSessionManager.logout(this);
            LoginSessionManager.launchLogin(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
