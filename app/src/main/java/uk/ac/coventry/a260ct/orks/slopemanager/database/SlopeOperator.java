package uk.ac.coventry.a260ct.orks.slopemanager.database;

import java.util.HashMap;

/**
 * Created by boldurbogdan on 28/02/2017.
 */

public class SlopeOperator extends User {
    private String referenceWork;
    public SlopeOperator(HashMap<User.ATTRIBUTES, String> info){
        super(info);
        this.referenceWork=info.get(ATTRIBUTES.REFERENCE_WORK);
    }

    public String getReferenceWork() {
        return referenceWork;
    }

    public void setReferenceWork(String referenceWork) {
        this.referenceWork = referenceWork;
    }
}
