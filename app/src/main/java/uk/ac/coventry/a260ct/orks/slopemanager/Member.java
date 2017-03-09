package uk.ac.coventry.a260ct.orks.slopemanager;

import java.util.Date;

/**
 * Created by boldurbogdan on 28/02/2017.
 */

public class Member extends User {

    public Member(int ID, String firstName,
                  String surname, String phone,
                  String email, Date dob,
                  int membership, int userType) {
        super(ID, firstName, surname, phone, email, dob, membership, userType);
    }
}
