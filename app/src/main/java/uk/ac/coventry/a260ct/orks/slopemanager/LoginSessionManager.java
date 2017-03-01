package uk.ac.coventry.a260ct.orks.slopemanager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by boldurbogdan on 28/02/2017.
 */

public class LoginSessionManager {
    private User user;
    private static LoginSessionManager INSTANCE = new LoginSessionManager();

    private LoginSessionManager() {

    }

    public static LoginSessionManager getInstance() {
        return INSTANCE;
    }

    public void setUser(User user, Context context) {
        this.user = user;
        SharedPreferences sharedPreferences = context.
                getSharedPreferences(context.getString(R.string.SHARED_PREFERENCES_KEY), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(context.getString(R.string.USER_LOGIN_SESSION_KEY), user.getID);
        editor.apply();

    }

    public User getUser(Context c) {
        if (user != null) {
            return user;
        }

        SharedPreferences sharedPreferences = c.getSharedPreferences(c.getString(R.string.SHARED_PREFERENCES_KEY), Context.MODE_PRIVATE);

        int userId = sharedPreferences.getInt(c.getString(R.string.USER_LOGIN_SESSION_KEY), 0);

        if (userId != 0) {
            user = new User();
        }

        return user;
    }
}
