package uk.ac.coventry.a260ct.orks.slopemanager;

/**
 * Created by boldurbogdan on 28/02/2017.
 */

public class SlopeOperator extends  User {
    public SlopeOperator(int ID, String firstName, String surname, String phone, String email, int membership) {
        super(ID, firstName, surname, phone, email, membership);
    }

    @Override
    public User getType(String type) {
        return null;
    }
}
