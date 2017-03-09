package uk.ac.coventry.a260ct.orks.slopemanager;

import java.util.Date;

/**
 * Created by boldurbogdan on 28/02/2017.
 */

public class User {
    private int ID;
    private String firstName;
    private String surname;
    private String phone;
    private String email;
    private Date dob;
    private int membership;
    private int userType;

    public User(int ID, String firstName,
                String surname, String phone,
                String email, Date dob,
                int membership, int userType) {
        this.ID = ID;
        this.firstName = firstName;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
        this.dob = dob;
        this.membership = membership;
        this.userType = userType;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
