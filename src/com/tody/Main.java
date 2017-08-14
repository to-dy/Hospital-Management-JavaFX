package com.tody;

import com.tody.datamodel.Datasource;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage theStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        theStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("ui/main.fxml"));
        primaryStage.setTitle("XYZ Hospital");
        primaryStage.setScene(new Scene(root));
        primaryStage.getIcons().add(new Image("com/tody/images/icon.png"));
        primaryStage.setWidth(705);
        primaryStage.setHeight(529);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        super.init();
        if(!Datasource.getInstance().open()){
            System.out.println("ERROR! : Couldn't connect to the database");
            Platform.exit();
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Datasource.getInstance().close();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
