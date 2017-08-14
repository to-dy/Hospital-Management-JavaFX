package com.tody.controllers;

import com.tody.datamodel.Datasource;
import com.tody.datamodel.Doctor;
import com.tody.datamodel.Patient;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by Tody_ on 26/06/2017.
 */
public class DoctorsUIController extends DeptAdminController {
    Patient patient;
    static Doctor doc;
    @FXML
    AnchorPane viewPatientUI;
    @FXML
    BorderPane borderPane;
    @FXML
    Button viewPatientBtn;
    @FXML
    Label userId, userName, userName1;
    @FXML
    TextField nameTxtFd, reasonTxtFd, phoneTxtFd, addressTxtFd, ipidTxtFd, opidTxtFd, ageTxtFd, usrInput,
            viewID, viewName, viewAge, viewAddress, viewPhone;
    @FXML
    DatePicker date;
    @FXML
    TextArea details;
    @FXML
    GridPane gridPane;
    @FXML
    Pane buttons;
    @FXML
    Button viewTreatmentBtn, addTreatmentBtn;
    @FXML
    ImageView img;

    public void initialize(){
        RotateTransition rotateTransition = new RotateTransition();
        rotateTransition.setDuration(Duration.millis(1000));
        rotateTransition.setNode(img);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(Animation.INDEFINITE);
        rotateTransition.play();

        ScaleTransition scaleTransition = new ScaleTransition();
        scaleTransition.setDuration(Duration.millis(1000));
        scaleTransition.setNode(img);
        scaleTransition.setByX(1);
        scaleTransition.setByY(1);
        scaleTransition.setCycleCount(Animation.INDEFINITE);
        scaleTransition .play();
    }


    @FXML
    private void loadPatientView() {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/com/tody/ui/doctorsPatientView.fxml"));
            borderPane.setRight(pane);
        } catch (IOException e) {
            System.out.println("Error loading scene " + e.getMessage());
            e.printStackTrace();
        }
//        newRegBtn.setStyle("");
        viewPatientBtn.setStyle("-fx-background-color: #7f1c27; -fx-text-fill: white;");

    }

    @FXML
    @Override
    protected void viewPatient() {
        super.viewPatient();
        String id = usrInput.getText().toUpperCase().trim();
        if(!id.isEmpty() && id.length() == 5) {
            this.patient = Datasource.getInstance().queryPatient(id);
            if (patient != null) {
                gridPane.setVisible(true);
                viewTreatmentBtn.setDisable(false);
                addTreatmentBtn.setDisable(false);
            }
        }else {
            Alert error = new Alert(Alert.AlertType.ERROR, "Check IPID");
            error.setHeaderText(null);
            error.show();

        }
    }

    @FXML
    private void showViewTreatmentDialog() {
        Dialog<ButtonType> dialog = new Dialog();
        dialog.initOwner(viewPatientUI.getScene().getWindow());
        dialog.setTitle("Treatment Details");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/tody/ui/viewTreatmentDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Error! Couldn't load dialog " + e.getMessage());
            e.printStackTrace();
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.show();
        if (patient != null) {
            ViewTreatmentDialogController controller = fxmlLoader.getController();
            controller.setPatient(patient.getIp_id());
        } else {
            System.out.println("ERROR: NO PATIENT ASSIGNED!");
        }
    }


    @FXML
    private void showAddTreatmentDialog() {
        Dialog<ButtonType> dialog = new Dialog();
        dialog.initOwner(viewPatientUI.getScene().getWindow());
        dialog.setTitle("Add Treatment Details");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/tody/ui/addTreatmentDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Error! Couldn't load dialog " + e.getMessage());
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            TreatmentDialogController controller = fxmlLoader.getController();
            if (controller.addTreatmentDetails(patient, doc)) {
                Alert success = new Alert(Alert.AlertType.INFORMATION, "Treatment Details successfully" +
                        " added for patient : " + patient.getName());
                success.setHeaderText(null);
                success.show();
            }
        }
    }

    @Override
    void reset() {
        patient = null;
        usrInput.clear();
        viewName.clear();
        viewID.clear();
        viewAge.clear();
        viewAddress.clear();
        viewPhone.clear();
        gridPane.setVisible(false);
//        buttons.setVisible(false);
        viewTreatmentBtn.setDisable(true);
        addTreatmentBtn.setDisable(true);
        usrInput.requestFocus();
    }


    public void setDoctor(String id) {
        Doctor doc = Datasource.getInstance().queryDoctor(id);
        this.doc = doc;
        userId.setText(id);
        userName.setText(doc.getName());
        userName1.setText("Dr. " + doc.getName());
    }

    @FXML
    private void logOut() {
        MainController controller = new MainController();
        controller.logOut();
    }
}