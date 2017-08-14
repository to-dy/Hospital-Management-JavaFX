package com.tody.controllers;

import com.tody.datamodel.Datasource;
import com.tody.datamodel.DeptAdmin;
import com.tody.datamodel.FrontDeskUser;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.util.Optional;

/**
 * Created by Tody_ on 20/07/2017.
 */
public class AdminController {
    public static final String ID = "Admin";
    public static final String PASSWORD = "ADMIN";
    @FXML
    Label fPassLabel, fConfirmPassLabel, fUserIdLabel, fNameLabel;
    @FXML
    Button loginBtn, viewUsersBtn, addUsersBtn, editUserBtn, delUserBtn, addNewUsrBtn, resetBtn;
    @FXML
    TextField loginIdTxtFd, passTxtFd, nameTxtFd, fPassTxtFd, fConfirmPassTxtFd, fIdTxtFd;
    @FXML
    AnchorPane loginView, addUserView;
    @FXML
    ToggleGroup usrType;
    @FXML
    BorderPane viewUsersView;
    @FXML
    TableView usersTableView;
    @FXML
    TableColumn idCol, userTypeCol, passCol, nameCol;
    @FXML
    GridPane newUserForm;
    @FXML
    HBox usrId;
    @FXML
    private Label speciality = new Label("Speciality :");
    @FXML
    private TextField specTxtFd = new TextField();


    public void initialize() {
        //FOR EASY ADMIN LOGIN
        loginIdTxtFd.setText(ID);
        passTxtFd.setText(PASSWORD);

        /* Disable delete and edit user buttons for Doctors(userType) for data integrity */
        usersTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue instanceof DeptAdmin || newValue instanceof FrontDeskUser) {
                    editUserBtn.setDisable(false);
                    delUserBtn.setDisable(false);
                } else {
                    editUserBtn.setDisable(true);
                    delUserBtn.setDisable(true);
                }
            }
        });

        /* implements form validation */
        newUserForm.hoverProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (validateForm()) {
                    addNewUsrBtn.setDisable(false);
                } else {
                    if (addNewUsrBtn.isVisible()) {
                        addNewUsrBtn.setDisable(true);
                    }
                }
            }
        });

        /*
        Dynamically adds form fields when adding a new user (depends on the type of user
        being added)
        */
        usrType.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                RadioButton selectedRadioButton = (RadioButton) usrType.getSelectedToggle();
                String selected = selectedRadioButton.getText().toUpperCase();
                if (selected.equalsIgnoreCase("DOC")
                        && newUserForm.getChildren().size() == 10) {
                    addField();
                } else {
                    if (newUserForm.getChildren().size() == 12) {
                        removeField();
                    }
                }
            }
        });
        addUsersBtn.setStyle("-fx-background-color: #7f1c27; -fx-text-fill: white");

        passErrorAlert(fPassTxtFd, fPassLabel, fConfirmPassTxtFd, fConfirmPassLabel);
        passErrorAlert(fConfirmPassTxtFd, fPassLabel, fPassTxtFd, fConfirmPassLabel);
    }

    // Dynamically add form fields
    private void addField() {
        specTxtFd.getStyleClass().add("c-text-field");
        newUserForm.getChildren().remove(4, 10);
        newUserForm.addRow(2, speciality, specTxtFd);
        newUserForm.addRow(3, fPassLabel, fPassTxtFd);
        newUserForm.addRow(4, fConfirmPassLabel, fConfirmPassTxtFd);
        newUserForm.addRow(5, fUserIdLabel, fIdTxtFd);
    }

    // Dynamically removes form fields
    private void removeField() {
        newUserForm.getChildren().remove(4, 12);
        newUserForm.addRow(2, fPassLabel, fPassTxtFd);
        newUserForm.addRow(3, fConfirmPassLabel, fConfirmPassTxtFd);
        newUserForm.addRow(4, fUserIdLabel, fIdTxtFd);

    }


    @FXML
    private void loadAddUsersView() {
        switchView(addUserView.isVisible(), addUserView, viewUsersView);
    }

    @FXML
    private void loadUsersView() {
        switchView(viewUsersView.isVisible(), viewUsersView, addUserView);
        viewUsers();
    }

    /**
     * For switching views
     * boolean visible - to check pane visibility.
     * Since there are only two view options to switch to this method
     * accepts two panes an switches the visibility when needed
     */
    private void switchView(boolean visible, Pane ne, Pane ol) {
        if (!visible) {
            ne.setVisible(true);
            ol.setVisible(false);
        }
        if (ne == addUserView) {
            viewUsersBtn.setStyle("");
            addUsersBtn.setStyle("-fx-background-color: #7f1c27; -fx-text-fill: white");
        } else {
            addUsersBtn.setStyle("");
            viewUsersBtn.setStyle("-fx-background-color: #7f1c27; -fx-text-fill: white");
        }

    }

    @FXML
    private void login() {
        if (loginIdTxtFd.getText().equalsIgnoreCase(ID) && passTxtFd.getText().equalsIgnoreCase(PASSWORD)) {
            System.out.println("Admin login Successful");
            addUserView.setVisible(true);
            loginView.setVisible(false);
            viewUsersView.setVisible(false);

            addUsersBtn.setVisible(true);
            viewUsersBtn.setVisible(true);
            usrId.setVisible(true);
        }
    }

    @FXML
    private void logout() {
        MainController controller = new MainController();
        controller.logOut();
    }

    @FXML
    private void onAddUserClick() {
        if (addNewUser()) {
            RadioButton btn = (RadioButton) usrType.getSelectedToggle();
            String userType = btn.getText();
            Alert success = new Alert(Alert.AlertType.INFORMATION, "New user added!\n" +
                    "Details:\n" +
                    "Name : " + nameTxtFd.getText() +
                    "User Type : " + userType);
            success.setHeaderText(null);
            success.show();
            reset();
        } else {
            Alert error = new Alert(Alert.AlertType.ERROR, "Unable to add new User to the database");
            error.setHeaderText(null);
            error.show();
        }
    }

    //handles adding a new user
    private boolean addNewUser() {
        boolean status = false;
        if (validateForm()) {
            String speciality = "";

            RadioButton selectedRadioButton = (RadioButton) usrType.getSelectedToggle();
            String selected = selectedRadioButton.getText();

            if (selected.equalsIgnoreCase("DOC")) {
                speciality = specTxtFd.getText().trim();
            }
            int id = Integer.parseInt(fIdTxtFd.getText().trim());
            String name = nameTxtFd.getText().trim();
            String password = null;
            password = fPassTxtFd.getText().trim();

            if (Datasource.getInstance().addUser(selected, name, id, password, speciality)) {
                status = true;
            }
        }
        return status;
    }

    //checks if the value in password fields are equal
    private boolean validatePassword(TextField f, TextField f1) {
        boolean b = false;
        String pass1 = f.getText().trim();
        String pass2 = f1.getText().trim();

        if (pass1.equals(pass2)) {
            b = true;
        }
        return b;
    }

    /**
     * Styles the password label RED to indicate a warning
     * Accepts two textfield (password and confirm password fields)
     * and their labels
     */
    private void passError(TextField f, Label l, TextField f1, Label l1) {
        if (!validatePassword(f, f1)) {
            l.setText("Password :*");
            l.setStyle("-fx-text-fill: red");
            l1.setText("Confirm Password :*");
            l1.setStyle("-fx-text-fill: red");
        } else {
            l.setText("Password :");
            l.setStyle("");
            l1.setText("Confirm Password :");
            l1.setStyle("");
        }
    }

    // method for adding a listener that warns for mismatched passwords
    private void passErrorAlert(TextField f, Label l, TextField f2, Label l1) {
        f.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                passError(f, l, f2, l1);
            }
        });
    }

    // ensures the form fields are validated
    private boolean validateForm() {
        boolean b = false;
        RadioButton selectedRadioButton = (RadioButton) usrType.getSelectedToggle();
        String selected;
        String name = nameTxtFd.getText().trim();
        if (selectedRadioButton != null) {
            selected = selectedRadioButton.getText();

            if (!selected.isEmpty() && !fIdTxtFd.getText().isEmpty() && !name.isEmpty() && validatePassword(fPassTxtFd, fConfirmPassTxtFd)) {
                if (selected.equalsIgnoreCase("DOC")) {
                    if (specTxtFd.getText().isEmpty()) {
                        speciality.setStyle("-fx-text-fill: red");
                        return false;
                    } else {
                        speciality.setStyle("");
                        b = true;
                    }
                }
                b = true;
            }
        }
        return b;
    }


    @FXML
    private void onDeleteClick() {
        String userType = "";
        int id = 0;
        String name = "";
        Object user = usersTableView.getSelectionModel().getSelectedItem();
        /* No option to delete a Doctor to ensure data integrity and
         avoid bugs in the patient treatment details */
        if (user instanceof FrontDeskUser) {
            userType = "FD";
            id = ((FrontDeskUser) user).getId();
            name = ((FrontDeskUser) user).getName();
        } else if (user instanceof DeptAdmin) {
            userType = "DA";
            id = ((DeptAdmin) user).getId();
            name = ((DeptAdmin) user).getName();
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setContentText("Are you sure you want to delete user :\n" +
                "ID : " + id +
                "\nName : " + name);

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (Datasource.getInstance().deleteUser(userType, id)) {
                viewUsers();
                Alert success = new Alert(Alert.AlertType.INFORMATION, "User successfully deleted");
                success.setHeaderText(null);
                success.show();
            } else {
                Alert error = new Alert(Alert.AlertType.ERROR, "Unable to delete user from the database.");
                error.setHeaderText(null);
                error.show();
            }
        }
    }

    @FXML
    private void onEditClick() {
        Object user = usersTableView.getSelectionModel().getSelectedItem();
        editUser(user);
    }

    /*Dynamically generates a Dialog to edit the selected user */
    private void editUser(Object user) {
        String userType = "";
        if (user instanceof FrontDeskUser) {
            userType = "FD";
        } else if (user instanceof DeptAdmin) {
            userType = "DA";
        }

        AnchorPane pane = new AnchorPane();
        pane.setPrefWidth(400);
        pane.setPrefHeight(250);
        String css = getClass().getResource("../css/style.css").toExternalForm();
        pane.getStylesheets().add(css);
        pane.setStyle("-fx-background-color: white");

        GridPane gridPane = new GridPane();
        gridPane.setLayoutX(45);
        gridPane.setLayoutY(50);

        Label id = new Label("User ID :");
        TextField idTf = new TextField();
        idTf.setDisable(true);
        styleTextField(idTf);
        Label name = new Label("Name :");
        TextField nameTf = new TextField();
        styleTextField(nameTf);
        Label pass = new Label("Password :");
        PasswordField passTf = new PasswordField();
        styleTextField(passTf);
        Label confirmPass = new Label("Confirm Password :");
        PasswordField confirmPassTf = new PasswordField();
        styleTextField(confirmPassTf);

        passErrorAlert(passTf, pass, confirmPassTf, confirmPass);
        passErrorAlert(confirmPassTf, pass, passTf, confirmPass);

        Node[] nodes = {id, idTf, name, nameTf, pass, passTf, confirmPass, confirmPassTf};
        for (int i = 0; i < nodes.length; i += 2) {
            gridPane.addRow(i, nodes[i], nodes[i + 1]);
        }
//        gridPane.add(id,0,0);
//        gridPane.add(idTf,1,0);
//        gridPane.add(name,0,1);
//        gridPane.add(nameTf,1,1);
//        gridPane.add(pass,0,2);
//        gridPane.add(passTf,1,2);
//        gridPane.add(confirmPass,0,3);
//        gridPane.add(confirmPassTf,1,3);

        gridPane.setVgap(10);
        gridPane.prefWidth(340);
        ColumnConstraints constraints = new ColumnConstraints();
        constraints.setPercentWidth(50);
        gridPane.getColumnConstraints().add(0, constraints);
        pane.getChildren().add(gridPane);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../ui/editUser.fxml"));
        fxmlLoader.setController(this);

        if (user instanceof FrontDeskUser) {
            int usrId = ((FrontDeskUser) user).getId();
            idTf.setText(String.valueOf(usrId));
            nameTf.setText(((FrontDeskUser) user).getName());
            passTf.setText(((FrontDeskUser) user).getPassword());
            confirmPassTf.setText(((FrontDeskUser) user).getPassword());
        } else if (user instanceof DeptAdmin) {
            int usrId = ((DeptAdmin) user).getId();
            idTf.setText(String.valueOf(usrId));
            nameTf.setText(((DeptAdmin) user).getName());
            passTf.setText(((DeptAdmin) user).getPassword());
            confirmPassTf.setText(((DeptAdmin) user).getPassword());
        }


        Dialog<ButtonType> dialog = new Dialog();
        dialog.initOwner(usersTableView.getScene().getWindow());
        dialog.setTitle("Edit user");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        dialog.getDialogPane().setContent(pane);

        Optional<ButtonType> result = dialog.showAndWait();
        if (validatePassword(passTf, confirmPassTf) && result.isPresent() && result.get() == ButtonType.OK) {
            editUpdate(userType, nameTf.getText(), passTf.getText(), Integer.parseInt(idTf.getText()));
        }
        if (!validatePassword(passTf, confirmPassTf)) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Passwords do not match");
            a.setHeaderText(null);
            a.show();
        }
    }

    //updates user in the database
    private void editUpdate(String userType, String name, String pass, int id) {

        if (Datasource.getInstance().updateUser(userType, name, id, pass)) {
            Alert success = new Alert(Alert.AlertType.INFORMATION, "User updated successfully");
            success.setHeaderText(null);
            success.show();
            viewUsers();
        }
    }

    private void styleTextField(TextField f) {
        f.getStyleClass().add("c-text-field");
    }

    @FXML
    private void reset() {
        nameTxtFd.clear();
        fPassTxtFd.clear();
        fConfirmPassTxtFd.clear();
        fIdTxtFd.clear();
        specTxtFd.clear();

        nameTxtFd.requestFocus();
    }

    private void viewUsers() {
        ObservableList data = FXCollections.observableArrayList(Datasource.getInstance().queryAllUsers());

        idCol.setCellValueFactory(new PropertyValueFactory<Object, Integer>("id"));
        userTypeCol.setCellValueFactory(new PropertyValueFactory<Object, String>("userType"));
        passCol.setCellValueFactory(new PropertyValueFactory<Object, String>("password"));
        nameCol.setCellValueFactory(new PropertyValueFactory<Object, String>("name"));

        usersTableView.setItems(data);
    }

}


