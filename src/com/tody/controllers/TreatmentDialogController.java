package com.tody.controllers;

import com.tody.datamodel.Datasource;
import com.tody.datamodel.Doctor;
import com.tody.datamodel.Patient;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;

import java.time.LocalDate;

/**
 * Created by Tody_ on 18/07/2017.
 */
public class TreatmentDialogController extends DoctorsUIController {
    @FXML
    DatePicker date;
    @FXML
    TextArea details;

    protected boolean addTreatmentDetails(Patient patient, Doctor doc) {
        String note = details.getText();
        LocalDate trtDate = date.getValue();
        String ip_id = patient.getIp_id();
        int doc_id = doc.getId();

        if (Datasource.getInstance().storeTreatment(ip_id, doc_id, note, trtDate)) {
            return true;
        } else {
            return false;
        }
    }
}
