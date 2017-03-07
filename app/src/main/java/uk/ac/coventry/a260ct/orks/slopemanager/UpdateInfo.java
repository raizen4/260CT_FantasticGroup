package uk.ac.coventry.a260ct.orks.slopemanager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by boldurbogdan on 01/03/2017.
 */

public interface UpdateInfo {
    void setInfo(int fragmentNumber,HashMap<String,String> infoToUpdate);
    void setInfo(int fragmentNumber, ArrayList<String> infoToUpdate, String foodPref);
    void showInfo(HashMap<String,String>infoToUpdate);
    void showInfo(ArrayList<String>info);

}
