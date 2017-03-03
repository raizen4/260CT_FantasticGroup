package uk.ac.coventry.a260ct.orks.slopemanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by boldurbogdan on 28/02/2017.
 */

public class LoginSessionManager {

    private static final String TAG = LoginSessionManager.class.getSimpleName();

    private LoginManagerPackage managerPackage;

    private Context context;
    private User user;

    public interface RequestLoginCallback {
        void onGranted();

        void onDenied();
    }

    public LoginSessionManager(Context appContext) {
        managerPackage = new LoginManagerPackage(context);
        context = appContext;
    }

    /**
     * We have a user ID from the passwords, so set our currently logged in user to that ID
     * and make user object from that ID
     *
     * @param id
     */
    public void setUser(int id) {
        user = managerPackage.getUser(id);
        SharedPreferences sharedPreferences = context.
                getSharedPreferences(context.getString(R.string.SHARED_PREFERENCES_KEY), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(context.getString(R.string.USER_LOGIN_SESSION_KEY), id);
        editor.apply();

    }

    public User getUser() {
        if (user != null) {
            Log.v(TAG, "User is logged in, already");
            return user;
        }

        SharedPreferences sharedPreferences =
                context.getSharedPreferences(context.getString(R.string.SHARED_PREFERENCES_KEY), Context.MODE_PRIVATE);

        int userId = sharedPreferences.getInt(context.getString(R.string.USER_LOGIN_SESSION_KEY), 0);

        if (userId != 0) {
            // TODO
            // This is for development
            Log.v(TAG, "User has a stored login");
            setUser(userId);
        }

        return user;
    }

    public void launchLogin() {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public void requestLogin(String username,
                             String password, RequestLoginCallback callback) {
        //TODO: Request if details are correct and get an ID
        // int userId = requestUser(username, password);

        int userId = 1234;

        if (userId > -1) { // User is valid
            setUser(userId);
            Log.v(TAG, "Login granted");
            callback.onGranted();
        } else {
            Log.v(TAG, "Login denied");
            callback.onDenied();
        }
    }

    public void logout(Context context) {
        Log.v(TAG, "Logging out");
        setUser(0);
    }
}
