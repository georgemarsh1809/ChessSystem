package com.example.chesssys2;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Dashboard {

    private Group dash = new Group();
    private Scene dashboard = new Scene(dash,  1920, 1080, Color.web("6998AB"));
    private Stage stage = new Stage();

    private FontLoader fonts = new FontLoader();

    public Dashboard (String username, Database parsedDB) {
        Database db = parsedDB;
        User currentUser = new User(username, db);
    }

    public void createUserDashboard(String userCreds){
        String css = this.getClass().getResource("app.css").toExternalForm();
        this.dashboard.getStylesheets().add(css);

        Pane sideBar = new Pane();
        sideBar.setStyle("-fx-background-color: #1A374D");
        sideBar.setPrefSize(282, 1080);

        Image profileIcon = new Image(this.getClass().getResource("userIcon.png").toExternalForm());
        ImageView profileIconView = new ImageView(profileIcon);

        profileIconView.setPreserveRatio(true);
        profileIconView.setFitHeight(84);
        profileIconView.setLayoutX(99);
        profileIconView.setLayoutY(75);

        Label creds = new Label(userCreds);
        creds.setLayoutY(500);
        creds.setLayoutX(500);

        dash.getChildren().add(creds);
        dash.getChildren().add(sideBar);
        dash.getChildren().add(profileIconView);
        stage.setTitle("Chess Library System");
        stage.setResizable(false);

        stage.setScene(dashboard);
        stage.show();

    }



}
