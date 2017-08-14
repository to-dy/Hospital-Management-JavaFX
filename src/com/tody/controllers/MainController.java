package com.tody.controllers;

import com.tody.Main;
import com.tody.datamodel.Datasource;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    @FXML
    TextField userIdField;
    @FXML
    ToggleGroup userType;
    @FXML
    PasswordField passwordField;
    @FXML
    Label loginError;
    @FXML
    Button login_btn;
    String userId;

    static Stage window;
    static Stage loginWindow;
    FXMLLoader loader;

    public void initialize() {
        passwordField.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    try {
                        login();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // Enables sign in button when user type is selected
        userType.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if(userType.getSelectedToggle() != null) {
                    login_btn.setDisable(false);
                }else {
                    login_btn.setDisable(true);
                }
            }
        });

    }

    @FXML
    public void login() {
        RadioButton selectedRadioButton = (RadioButton) userType.getSelectedToggle();
        String selected = selectedRadioButton.getText();


        if (Datasource.getInstance().validate(userIdField.getText(), passwordField.getText(), selected)) {
            System.out.println("Login Successful");
            userId = userIdField.getText().toUpperCase();
            //instance of the main stage(the first stage)
            loginWindow = Main.theStage;
            window = new Stage();
            switch (selected) {
                case "FD":
                    loadWindow("/com/tody/ui/frontDesk.fxml", "Front Desk Interface");
                    FrontDeskController controller = loader.getController();
                    controller.setUser(userId);
                    break;

                case "DA":
                    loadWindow("/com/tody/ui/deptAdmin.fxml", "DepartmentAdmin Interface");
                    DeptAdminController deptAdminController = loader.getController();
                    deptAdminController.setUser(userId);
                    break;

                case "DOC":
                    loadWindow("/com/tody/ui/doctorsUI.fxml", "Doctor's Interface");
                    DoctorsUIController docController = loader.getController();
                    docController.setDoctor(userId);
                    break;

                default:
                    System.out.println("No selected user type");
            }

        } else {
            System.out.println("Unable to Login!");
            loginError.setText("Incorrect UserId or Password!");
            passwordField.clear();
            loginError.setVisible(true);
        }

    }

    /* Loads a new window based on the user type
    * url - path to fxml;
    * windowTitle - to set the title for the new window
    * */
    private void loadWindow(String url, String windowTitle) {
        loader = new FXMLLoader(getClass().getResource(url));
        try {
            window.setScene(new Scene(loader.load()));
            window.setTitle(windowTitle);
            window.setWidth(705);
            window.setHeight(529);
            window.setResizable(false);
            window.getIcons().add(new Image("com/tody/images/icon.png"));
            window.show();
            loginWindow.hide();
        } catch (IOException e) {
            System.out.println("Error loading scene " + e.getMessage());
        }
    }

    @FXML
    private void loadAdminLogin(){
        loginWindow = Main.theStage;
        window = new Stage();
        loadWindow("/com/tody/ui/adminUI.fxml", "Admin's Interface");
    }

    public void logOut() {
        try {
            Stage window = Main.theStage;
            window.setTitle("XYZ Hospital");
            window.setScene(new Scene(FXMLLoader.load(getClass().getResource("/com/tody/ui/main.fxml"))));
            window.show();
            MainController.window.close();
        } catch (IOException e) {
            System.out.println("Error logging out!" + e.getMessage());
        }

    }

}
