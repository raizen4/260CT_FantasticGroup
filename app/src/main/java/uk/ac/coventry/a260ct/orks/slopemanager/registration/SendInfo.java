package uk.ac.coventry.a260ct.orks.slopemanager.registration;

import java.util.HashMap;

import uk.ac.coventry.a260ct.orks.slopemanager.database.User;

/**
 * Created by boldurbogdan on 08/03/2017.
 */

public interface SendInfo {
    public void sendHashMap(HashMap<User.ATTRIBUTES, String> masterInfo);
    public void sendConfPayment(int successful);
    public void sendTypeOfUser(String type);
}
