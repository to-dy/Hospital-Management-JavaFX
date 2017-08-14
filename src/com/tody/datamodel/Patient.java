package com.tody.datamodel;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

/**
 * Created by Tody_ on 20/05/2017.
 */
public class Patient {
    SimpleStringProperty ip_id;
    SimpleStringProperty name;
    SimpleIntegerProperty age;
    SimpleStringProperty address;
    SimpleStringProperty phone;
    ObservableList<TreatmentDetails> treatmentDetails;

    public Patient() {
        this.ip_id = new SimpleStringProperty();
        this.name = new SimpleStringProperty();
        this.age = new SimpleIntegerProperty();
        this.address = new SimpleStringProperty();
        this.phone = new SimpleStringProperty();
        this.treatmentDetails = treatmentDetails;
    }

    public String getIp_id() {
        return ip_id.get();
    }


    public void setIp_id(String ip_id) {
        this.ip_id.set(ip_id);
    }

    public String getName() {
        return name.get();
    }


    public void setName(String name) {
        this.name.set(name);
    }

    public int getAge() {
        return age.get();
    }


    public void setAge(int age) {
        this.age.set(age);
    }

    public String getAddress() {
        return address.get();
    }


    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getPhone() {
        return phone.get();
    }


    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public ObservableList<TreatmentDetails> getTreatmentDetails() {
        return treatmentDetails;
    }

    public void setTreatmentDetails(ObservableList<TreatmentDetails> treatmentDetails) {
        this.treatmentDetails = treatmentDetails;
    }
}
