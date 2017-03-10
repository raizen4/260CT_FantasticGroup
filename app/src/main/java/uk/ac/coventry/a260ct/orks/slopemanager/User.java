package uk.ac.coventry.a260ct.orks.slopemanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by boldurbogdan on 28/02/2017.
 */

public abstract class User {
    public enum ATTRIBUTES{
        ID,
        FIRST_NAME,
        SURNAME,
        PHONE,
        EMAIL,
        DOB,
        MEMBERSHIP,
        REFERENCE_WORK
    }

    private int ID;
    private String firstName;
    private String surname;
    private String phone;
    private String email;
    private Date dob;
    private int membership;

    private String type;

    User(HashMap<ATTRIBUTES, String> info) {
        this.ID = Integer.parseInt(info.get(ATTRIBUTES.ID));
        this.firstName = info.get(ATTRIBUTES.FIRST_NAME);
        this.surname = info.get(ATTRIBUTES.SURNAME);
        this.phone = info.get(ATTRIBUTES.PHONE);
        this.email = info.get(ATTRIBUTES.EMAIL);
        try {
            this.dob = new SimpleDateFormat("yyyy-mm-dd", Locale.UK).parse(info.get(ATTRIBUTES.DOB));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.membership = Integer.valueOf(info.get(ATTRIBUTES.MEMBERSHIP));
    }

    public User(int ID, String firstName,
                String surname, String phone,
                String email, Date dob,
                int membership) {
        this.ID = ID;
        this.firstName = firstName;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
        this.dob = dob;
        this.membership = membership;
    }


    public int getMembership() {
        return membership;
    }

    public void setMembership(int permissionLevel) {
        this.membership = permissionLevel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
