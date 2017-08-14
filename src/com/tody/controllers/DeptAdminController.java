package com.tody.controllers;

import com.tody.datamodel.Datasource;
import com.tody.datamodel.DeptAdmin;
import com.tody.datamodel.Doctor;
import com.tody.datamodel.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Tody_ on 12/06/2017.
 */
public class DeptAdminController {
    String id;
    @FXML
    BorderPane borderPane;

    @FXML
    AnchorPane registrationView, mainView;
    @FXML
    TabPane viewPatient;

    @FXML
    Button newRegBtn;
    @FXML
    Button viewPatientBtn;
    @FXML
    Label userId, userName,userName1, nameLabel, reasonLabel, phoneLabel, addressLabel, referLabel, ipidLabel, opidLabel, ageLabel;
    @FXML
    TextField nameTxtFd, reasonTxtFd, phoneTxtFd, addressTxtFd, ipidTxtFd, opidTxtFd, ageTxtFd, usrInput,
            viewID, viewName, viewAge, viewAddress, viewPhone;
    @FXML
    ChoiceBox referBox;

    @FXML
    GridPane gridPane;

    @FXML
    TableView patientsTable;

    @FXML
    TableColumn idCol, nameCol, ageCol, addressCol, phoneCol;

    public void initialize() {
    }

    @FXML
    private void viewNewRegistration() {
        registrationView.setVisible(true);
        mainView.setVisible(false);
        viewPatient.setVisible(false);

        newRegBtn.setStyle("-fx-background-color: #7f1c27; -fx-text-fill: white;");
        viewPatientBtn.setStyle("");
    }

    @FXML
    private void viewPatientUI() {
        viewPatient.setVisible(true);
        mainView.setVisible(false);
        registrationView.setVisible(false);

        newRegBtn.setStyle("");
        viewPatientBtn.setStyle("-fx-background-color: #7f1c27; -fx-text-fill: white;");
    }


    @FXML
    private void validateOPID() {
        ArrayList op = Datasource.getInstance().validateOPID(Integer.parseInt(opidTxtFd.getText()));
        if (!op.isEmpty()) {
            nameLabel.setVisible(true);
            nameTxtFd.setVisible(true);
            nameTxtFd.setText((String) op.get(0));
            nameTxtFd.setEditable(false);
            ageLabel.setVisible(true);
            ageTxtFd.setVisible(true);
            reasonLabel.setVisible(true);
            reasonTxtFd.setVisible(true);
            reasonTxtFd.setText((String) op.get(1));
            reasonTxtFd.setEditable(false);

            phoneLabel.setVisible(true);
            phoneTxtFd.setVisible(true);
            addressLabel.setVisible(true);
            addressTxtFd.setVisible(true);
            referLabel.setVisible(true);
            referBox.setVisible(true);

            phoneTxtFd.requestFocus();
            referBox.getItems().add("Select Doctor");
            referBox.getSelectionModel().selectFirst();
            List<Doctor> doctors = Datasource.getInstance().queryDoctors();
            for (Doctor doc : doctors) {
                referBox.getItems().addAll(doc.getName() + " (" + doc.getSpeciality() + ")");
            }
        } else {
            Alert notFound = new Alert(Alert.AlertType.ERROR, "Patient with OPID : " + opidTxtFd.getText() + " is not in the database");
            notFound.setHeaderText(null);
            notFound.setTitle(null);
            notFound.show();
        }
    }

    @FXML
    private void onSelect() {
        if (referBox.getSelectionModel().getSelectedItem() != "Select Doctor") {
            generateIPID();
        }
    }

    private void generateIPID() {
        if (ipidTxtFd.getText().isEmpty()) {
            ipidLabel.setVisible(true);
            ipidTxtFd.setVisible(true);

            ArrayList<Character> list = new ArrayList<>();
            for (char i = 'A'; i <= 'Z'; i++) {
                list.add(new Character(i));
            }
            ArrayList<Integer> list1 = new ArrayList<>();
            for (int i = 0; i <= 9; i++) {
                list1.add(new Integer(i));
            }
            Collections.shuffle(list);
            Collections.shuffle(list1);
            StringBuilder id = new StringBuilder();
            for (int i = 0; i < 3; i++) {
                id.append(list1.get(i));
            }
            for (int i = 0; i < 2; i++) {
                id.append(list.get(i));
            }

            ipidTxtFd.setText(id.toString());
            System.out.println("IPID : " + id);
        }


    }

    @FXML
    private void newPatient() {
        String ip_id = ipidTxtFd.getText();
        String name = nameTxtFd.getText();
        int age = Integer.parseInt(ageTxtFd.getText());
        String address = addressTxtFd.getText();
        String phone = phoneTxtFd.getText();

        if (!ip_id.isEmpty() && !name.isEmpty()) {
            if (address.isEmpty()) {
                address = "N/A";
            }
            if (Datasource.getInstance().storePatient(ip_id, name, age, address, phone)) {
                Alert success = new Alert(Alert.AlertType.INFORMATION, "New Patient added!\n" +
                        "Details :\n" +
                        "IPID : " + ip_id + "\n" +
                        "Name : " + name + "\n" +
                        "Age  : " + age + "\n" +
                        "Address : " + address + "\n" +
                        "Phone   : " + phone);
                success.setHeaderText(null);
                success.show();
            } else {
                Alert error = new Alert(Alert.AlertType.ERROR, "Error adding patient to the database!\n" +
                        "Check necessary field");
                error.setHeaderText(null);
                error.show();
            }
        }
    }

    @FXML
    protected void viewPatient() {
        String id = usrInput.getText().toUpperCase().trim();
        if (!id.isEmpty() && id.length() == 5) {
            Patient patient = Datasource.getInstance().queryPatient(id);
            if(patient != null) {
                String ip_id = patient.getIp_id();
                String name = patient.getName();
                int age = patient.getAge();
                String address = patient.getAddress();
                String phone = patient.getPhone();

                viewID.setText(ip_id);
                viewName.setText(name);
                viewAge.setText(String.valueOf(age));
                viewAddress.setText(address);
                viewPhone.setText(phone);
                gridPane.setVisible(true);
        }
        else {
            Alert error = new Alert(Alert.AlertType.ERROR, "Check IPID");
            error.setHeaderText(null);
            error.show();
        }
            }


    }

    @FXML
    private void viewPatients() {
        idCol.setCellValueFactory(new PropertyValueFactory<Patient, String>("ip_id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<Patient, String>("name"));
        ageCol.setCellValueFactory(new PropertyValueFactory<Patient, Integer>("age"));
        addressCol.setCellValueFactory(new PropertyValueFactory<Patient, String>("address"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<Patient, String>("phone"));

//        Task<ObservableList<Patient>> task = new GetAllPatientsTask();
//        patientsTable.itemsProperty().bind(task.valueProperty());
        ObservableList<Patient> data = FXCollections.observableArrayList(Datasource.getInstance().queryAllPatients());


        patientsTable.setItems(data);
//        new Thread(task).start();
    }

    @FXML
    void reset() {
        opidTxtFd.clear();
        nameTxtFd.clear();
        ageTxtFd.clear();
        nameLabel.setVisible(false);
        nameTxtFd.setVisible(false);
        ageLabel.setVisible(false);
        ageTxtFd.setVisible(false);
        reasonTxtFd.clear();
        reasonLabel.setVisible(false);
        reasonTxtFd.setVisible(false);

        phoneTxtFd.clear();
        phoneLabel.setVisible(false);
        phoneTxtFd.setVisible(false);
        addressTxtFd.clear();
        addressLabel.setVisible(false);
        addressTxtFd.setVisible(false);
        referBox.getSelectionModel().selectFirst();
        referLabel.setVisible(false);
        referBox.setVisible(false);
        referBox.getItems().clear();

        ipidTxtFd.clear();
        ipidLabel.setVisible(false);
        ipidTxtFd.setVisible(false);
    }

    @FXML
    void resetPatientView() {
        usrInput.clear();
        viewName.clear();
        viewID.clear();
        viewAge.clear();
        viewAddress.clear();
        viewPhone.clear();
        gridPane.setVisible(false);
        usrInput.requestFocus();
    }

    public void setUser(String id) {
        this.id = id;
        DeptAdmin user = Datasource.getInstance().queryDeptAdmin(id);
        userName.setText(user.getName());
        userName1.setText(user.getName());
        userId.setText(id);

    }

    @FXML
    private void logOut() {
        MainController controller = new MainController();
        controller.logOut();
    }

    class GetAllPatientsTask extends Task {
        @Override
        public ObservableList call() throws Exception {
            return FXCollections.observableArrayList(Datasource.getInstance().queryAllPatients());
        }
    }
}
