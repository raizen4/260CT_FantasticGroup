package uk.ac.coventry.a260ct.orks.slopemanager.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import uk.ac.coventry.a260ct.orks.slopemanager.database.User;
import uk.ac.coventry.a260ct.orks.slopemanager.R;
import uk.ac.coventry.a260ct.orks.slopemanager.SlopeManagerApplication;

/**
 * Created by boldurbogdan on 28/02/2017.
 */

public class LoginSessionManager {

    private static final String TAG = LoginSessionManager.class.getSimpleName();

    private Context context;
    private User user;

    private SlopeManagerApplication application;

    public interface RequestLoginCallback {
        void onGranted();

        void onDenied();
    }

    public LoginSessionManager(Context appContext) {
        context = appContext;
        application = SlopeManagerApplication.getInstance();
    }

    /**
     * We have a user ID from the credentials, so set our currently logged in user to that ID
     * and make user object from that ID
     *
     * @param id
     */
    public void setUser(int id) {
        user = application.getSlopeDatabase().getUserFromId(id);

        if (user != null || id == -1) {
            // Save the currently logged in user's ID locally so they don't
            // need to log in again manually

            SharedPreferences sharedPreferences = context.
                    getSharedPreferences(context.getString(R.string.SHARED_PREFERENCES_KEY), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(context.getString(R.string.USER_LOGIN_SESSION_KEY), id);
            editor.apply();
        }
    }

    public User getUser() {
        if (user != null) {
            // We already have the user in memory
            Log.v(TAG, "User is logged in, already");
            return user;
        }

        // Collect the current user ID stored in the apps locals data storage
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(context.getString(R.string.SHARED_PREFERENCES_KEY), Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt(context.getString(R.string.USER_LOGIN_SESSION_KEY), -1);

        if (userId > -1) {
            // Collect the users information from the database
            Log.v(TAG, "User has a stored login");
            setUser(userId);
        } 

        // Return a user or null if none logged in currently
        return user;
    }

    public void launchLogin() {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public void requestLogin(String username,
                             String password,
                             RequestLoginCallback callback) {

        // retrieve the user id using the username and password
        int userId = application.getSlopeDatabase().getUserIdFromCredentials(username, password);

        if (userId > -1) { // User is valid
            setUser(userId);
            Log.v(TAG, "Login granted");
            callback.onGranted();
        } else {
            Log.v(TAG, "Login denied");
            callback.onDenied();
        }
    }

    public User getUserOrLogout() {
        user = getUser();

        if (user == null) { // No user logged in so go to login screen
            Log.v(TAG, "User not logged in, launching login");
            launchLogin();
        }

        return user;
    }

    public void logout() {
        Log.v(TAG, "Logging out");
        setUser(-1);
    }
}
