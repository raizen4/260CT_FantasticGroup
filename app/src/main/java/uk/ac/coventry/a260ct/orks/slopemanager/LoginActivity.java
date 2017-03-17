package uk.ac.coventry.a260ct.orks.slopemanager;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

    private static final int REQUEST_PHONE_PERMISSION = 0;

    private SlopeManagerApplication application;

    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        application = SlopeManagerApplication.getInstance();

        usernameInput = (EditText) findViewById(R.id.login_username_input);
        passwordInput = (EditText) findViewById(R.id.login_password_input);
        registerButton= (Button) findViewById(R.id.login_register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchRegistration();
            }
        });

        passwordInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onLoginClicked();
                }
                    return false;
            }
        });

        passwordInput.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_UP) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    onLoginClicked();
                    return true;
                }
                return false;
            }
        });


        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginClicked();
            }
        });

        findViewById(R.id.login_register_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchRegistration();
            }
        });

        findViewById(R.id.login_phone_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCall();
            }
        });
    }

    private void requestCall() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    REQUEST_PHONE_PERMISSION);
        } else {
            callReception();
        }
    }

    private void callReception() {
        Intent in = new Intent(Intent.ACTION_CALL, Uri.parse("tel:01344203020"));
        try {
            startActivity(in);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(),
                    "Could not find an activity to place the call.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void launchDashboard() {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void launchRegistration() {
        startActivity(new Intent(this, UserTypeActivity.class));
    }

    private void alertLoginDenied() {
        Snackbar.make(
                findViewById(R.id.activity_login_layout),
                R.string.login_invalid,
                Snackbar.LENGTH_LONG
        ).show();
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
            ).show();
        } else if (passwordTyped.isEmpty()) {
            Snackbar.make(
                    findViewById(R.id.activity_login_layout),
                    R.string.login_no_password,
                    Snackbar.LENGTH_SHORT
            ).show();
        } else {

            // Make a request to the login manager to login using these credentials.
            // The callback will be called once this is complete
            application.getLoginSessionManager()
                    .requestLogin(
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHONE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callReception();
                }
                break;
            }
        }
    }

}