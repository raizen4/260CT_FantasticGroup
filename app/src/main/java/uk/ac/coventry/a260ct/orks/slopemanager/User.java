package uk.ac.coventry.a260ct.orks.slopemanager;

import java.util.HashMap;

/**
 * Created by boldurbogdan on 28/02/2017.
 */

public abstract class User {
    private int ID;
    private String firstName;
    private String surname;
    private String phone;
    private String email;
    private int permissionLevel;
    private String type;

    User(HashMap<String, String> info) {
        this.ID = Integer.parseInt(info.get("ID"));
        this.firstName = info.get("firstName");
        this.surname = info.get("surname");
        this.phone = info.get("phone");
        this.email =info.get("email") ;
        this.permissionLevel = Integer.parseInt(info.get("permissionLevel"));
    }


    public int getMembership() {
        return permissionLevel;
    }

    public void setMembership(int permissionLevel) {
        this.permissionLevel = permissionLevel;
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
