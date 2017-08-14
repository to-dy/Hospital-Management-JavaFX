package com.tody.controllers;

import com.tody.datamodel.Datasource;
import com.tody.datamodel.FrontDeskUser;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.Collections;


public class FrontDeskController {
    @FXML
    Label userId;
    @FXML
    Label userName;
    @FXML
    Label nameErrorLabel, reasonErrorLabel, resetLabel;
    @FXML
    Button logOutBtn;
    @FXML
    TextField opName;
    @FXML
    ComboBox visitReason;
    @FXML
    TextField opId;

    String id;

    public void initialize() {
        visitReason.getItems().addAll("Select Reason", "Medical Check Up", "Emergency", "Doctor's Appointment");
        visitReason.getSelectionModel().selectFirst();


        visitReason.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> reasonError());
        opName.textProperty().addListener((observable, oldValue, newValue) -> validateName());

        opName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(validateName()) {
                    generateOPId();
                }
            }
        });

    }


    @FXML
    private void logOut() {
        MainController controller = new MainController();
        controller.logOut();
    }


    // clears form  fields
    @FXML
    private void reset() {
        opName.clear();
        opId.clear();
        nameErrorLabel.setVisible(false);
        visitReason.getSelectionModel().selectFirst();
        reasonErrorLabel.setVisible(false);
        visitReason.setDisable(false);
        opName.setDisable(false);
        resetLabel.setVisible(false);
    }

    @FXML
    private void generateOPId() {
        if (validateName() && opId.getText().isEmpty() &&
                visitReason.getSelectionModel().getSelectedItem().toString() != "Select Reason") {
            nameErrorLabel.setVisible(false);
            reasonErrorLabel.setVisible(false);
            ArrayList<Integer> list = new ArrayList<>();
            for (int i = 0; i <= 9; i++) {
                list.add(new Integer(i));
            }
            Collections.shuffle(list);
            StringBuilder number = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                number.append(list.get(i));
            }
            opId.setText(number.toString());
            System.out.println(number);
        } else {
            if (!validateName()) {
                nameErrorLabel.setVisible(true);
            }
//            else if (visitReason.getSelectionModel().getSelectedItem().toString() == "Select Reason") {
//                reasonErrorLabel.setVisible(true);
//            }
        }
    }

    private boolean validateName() {
        boolean flag = false;
        if (!opName.getText().isEmpty()) {
            if (opName.getText().length() >= 2) {
                flag = true;
                nameErrorLabel.setVisible(false);
                reasonError();
            }
            return flag;
        } else {
            nameErrorLabel.setVisible(true);
            return false;
        }
    }

    private void reasonError() {
        if (visitReason.getSelectionModel().getSelectedItem().toString() == "Select Reason") {
            reasonErrorLabel.setVisible(true);
        } else {
            reasonErrorLabel.setVisible(false);
        }
    }

    @FXML
    private void validateOPID() {
        opName.setDisable(true);
        visitReason.setDisable(true);
        int opid = Integer.parseInt(opId.getText());
        String name = opName.getText();
        String vReason = visitReason.getSelectionModel().getSelectedItem().toString();

        if (validateName() && vReason != "Select Reason" && !opId.getText().isEmpty()) {
            if (Datasource.getInstance().storeOPID(opid, name, vReason)) {
                Alert success = new Alert(Alert.AlertType.CONFIRMATION, "OPID : " + opid
                        + "\nName : " + name
                        + "\nReason for Visit: " + vReason);
                success.setHeaderText(null);
                success.setTitle("OPID Stored successfully");
                success.show();
            } else {
                Alert error = new Alert(Alert.AlertType.ERROR, "Error storing OPID! Please contact the system administrator!");
                error.setHeaderText(null);
                error.show();
            }
        } else {
            Alert error = new Alert(Alert.AlertType.ERROR, "Fix Errors!\n" + "Insert patients Name or \"UNKNOWN\" if the patients name is not known");
            error.setHeaderText(null);
            error.show();
        }
        resetLabel.setVisible(true);
    }

    // Sets the current user based on user id
    public void setUser(String id) {
        this.id = id;
        FrontDeskUser user = Datasource.getInstance().queryFrontDeskUser(id);
        userName.setText(user.getName());
        userId.setText(id);
    }


}