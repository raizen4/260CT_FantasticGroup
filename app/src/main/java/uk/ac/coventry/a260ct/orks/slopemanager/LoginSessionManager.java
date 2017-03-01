package uk.ac.coventry.a260ct.orks.slopemanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by boldurbogdan on 28/02/2017.
 */

public class LoginSessionManager {
    private LoginManagerPackage managerPackage;
    private static LoginSessionManager INSTANCE=new LoginSessionManager();
    private User user;

    private LoginSessionManager()
    {

    }
    public static LoginSessionManager getInstance(){
           return INSTANCE;
       }

    public void setUser(LoginManagerPackage manager,int id, Context context) {
        this.user=managerPackage.getUser(id);
        SharedPreferences sharedPreferences=context.
                getSharedPreferences(context.getString(R.string.SHARED_PREFERENCES_KEY),Context.MODE_PRIVATE);
         SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt(context.getString(R.string.USER_LOGIN_SESSION_KEY),user.getID);
        editor.apply();

    }

    public User getUser(Context c){
        if (user != null) {
            return user;
        }

        SharedPreferences sharedPreferences=c.getSharedPreferences(c.getString(R.string.SHARED_PREFERENCES_KEY),Context.MODE_PRIVATE);

        int userId = sharedPreferences.getInt(c.getString(R.string.USER_LOGIN_SESSION_KEY), 0);

        if (userId != 0) {
            user = new User();
        }

        return user;
    }

    public void goToLogin(Context context) {
        Intent intent = new Intent(context, LogInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
