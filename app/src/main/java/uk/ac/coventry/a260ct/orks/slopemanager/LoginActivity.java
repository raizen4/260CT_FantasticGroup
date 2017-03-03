package uk.ac.coventry.a260ct.orks.slopemanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);

        usernameInput = (EditText) findViewById(R.id.login_username_input);
        passwordInput = (EditText) findViewById(R.id.login_password_input);
        passwordInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onLoginClicked();
                    return true;
                } else {
                    return false;
                }
            }
        });


        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginClicked();
            }
        });
    }

    private void launchDashboard() {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void launchRegistration() {
        startActivity(new Intent(this, RegistrationActivity.class));
    }

    private void alertLoginDenied() {

    }

    private void onLoginClicked() {
        Log.v(TAG, "Login clicked");
        String usernameTyped = usernameInput.getText().toString();
        String passwordTyped = passwordInput.getText().toString();

        if (usernameTyped.isEmpty()) {
            Snackbar.make(
                    findViewById(R.id.activity_login_layout),
                    R.string.login_no_username,
                    Snackbar.LENGTH_SHORT
            );
        } else if (passwordTyped.isEmpty()) {
            Snackbar.make(
                    findViewById(R.id.activity_login_layout),
                    R.string.login_no_password,
                    Snackbar.LENGTH_SHORT
            ).show();
        } else {
            Toast.makeText(this, R.string.requesting_login, Toast.LENGTH_SHORT).show();

            // Make a request to the login manager to login using these credentials.
            // The callback will be called once this is complete
            LoginSessionManager.getInstance(this)
                    .requestLogin(this,
                            usernameTyped, passwordTyped,
                            new LoginSessionManager.RequestLoginCallback() {
                                @Override
                                public void onGranted() {
                                    launchDashboard();
                                }

                                @Override
                                public void onDenied() {
                                    alertLoginDenied();
                                }
                            });
        }
    }

}