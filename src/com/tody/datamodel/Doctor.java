package com.tody.datamodel;

/**
 * Created by Tody_ on 20/05/2017.
 */
public class Doctor {
    int id;
    String name;
    String speciality;
    String userType;
    String password;

    public Doctor() {
        this.id = id;
        this.name = name;
        this.speciality = speciality;
        this.userType = "DOC";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }
}
