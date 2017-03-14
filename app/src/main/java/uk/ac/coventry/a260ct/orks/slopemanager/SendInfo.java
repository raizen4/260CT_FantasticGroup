package uk.ac.coventry.a260ct.orks.slopemanager;

import java.util.HashMap;

/**
 * Created by boldurbogdan on 08/03/2017.
 */

public interface SendInfo {
    public void sendHashMap(HashMap<User.ATTRIBUTES, String> masterInfo);
    public void sendConfPayment(int successful);
    public void sendTypeOfUser(String type);
}
