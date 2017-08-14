package com.tody.datamodel;

import java.time.LocalDate;

/**
 * Created by Tody_ on 20/05/2017.
 */
public class TreatmentDetails {
    LocalDate date;
    String ip_id;
    int doc_id;
    String details;

    public TreatmentDetails() {
        this.ip_id = ip_id;
        this.doc_id = doc_id;
        this.details = details;
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = LocalDate.parse(date);
    }

    public String getIp_id() {
        return ip_id;
    }

    public void setIp_id(String ip_id) {
        this.ip_id = ip_id;
    }

    public int getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(int doc_id) {
        this.doc_id = doc_id;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
