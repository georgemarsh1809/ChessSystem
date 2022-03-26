package com.example.chesssys2;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;

public class ChessSys extends Application {

    Database db = new Database();
    //Connection connectDB = db.setupDataBase();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primStage) throws IOException {
        //Required 'Start' method;
        //This method will also load the login interface
        //FXMLLoader fxmlLoader = new FXMLLoader(ChessSys.class.getResource("hello-view.fxml"));
        //to use this fxmlLoader, replace 'root' with 'fxmlLoader.load()' when creating the primary scene

        LoginScreen loginScreen = new LoginScreen(db); //creates an instance of the LoginScreen
        loginScreen.CreateLoginScreen();




    }


}