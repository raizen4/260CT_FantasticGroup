package uk.ac.coventry.a260ct.orks.slopemanager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by boldurbogdan on 01/03/2017.
 */

public interface UpdateInfo {
    void setInfo(String fragmentName,HashMap<User.ATTRIBUTES,String> infoToUpdate);

    void setInfo(String fragmentName, ArrayList<String> infoToUpdate, ArrayList<String> infoToUpdate2);


}
