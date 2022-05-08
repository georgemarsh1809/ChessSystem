package com.example.chesssys2;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Connection;
import java.util.List;

public class LoginScreen {

    //Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));

    private Group root = new Group();
    private Scene loginScene = new Scene(root,  500, 250, Color.web("6998AB"));
    private Stage stage = new Stage();

    Database db;
    Connection connectDB;

    boolean loginValidated = false;

    private FontLoader fonts = new FontLoader();

    private Label welcomeLabel = new Label("Welcome Back!");
    private TextField usernameField = new TextField();
    private PasswordField passwordField = new PasswordField();
    private Button loginButton = new Button("Login ");
    private Button registerButtton = new Button("Register");

    private Label emptyErrorMsg;
    private Label invalidErrorMsg;

    public LoginScreen (Database parsedDB) {
        db = parsedDB;
        connectDB = db.setupDataBase();
    }


    public void CreateLoginScreen (){
        //Load the right CSS file
        String css = this.getClass().getResource("app.css").toExternalForm();
        this.loginScene.getStylesheets().add(css);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(1);
        dropShadow.setOffsetY(3);
        dropShadow.setColor(Color.color(0.4, 0.5, 0.5));

        //Set and place the Welcome label
        welcomeLabel.setId("welcomeLabel");
        welcomeLabel.setFont(fonts.lemonMilkMedium27());
        welcomeLabel.setLayoutX(130);
        welcomeLabel.setLayoutY(29);

        //Set and place the username field
        usernameField.setId("username");
        usernameField.setFont(fonts.lemonMilkRegular13());

        usernameField.setPromptText("Enter username");
        usernameField.setAlignment(Pos.CENTER);
        usernameField.setLayoutX(130);
        usernameField.setLayoutY(90);
        usernameField.setMinHeight(24);
        usernameField.setMinWidth(240);
        usernameField.setEffect(dropShadow);


        //Set and place the password field
        passwordField.setId("passwordField");

        passwordField.setPromptText("Enter password");
        passwordField.setAlignment(Pos.CENTER);
        passwordField.setLayoutX(130);
        passwordField.setLayoutY(130);
        passwordField.setMinHeight(24);
        passwordField.setMinWidth(240);
        passwordField.setEffect(dropShadow);


        //Set and place the login button
        loginButton.setId("loginButton");
        loginButton.setLayoutX(350);
        loginButton.setLayoutY(190);
        loginButton.setMinWidth(70);
        loginButton.setMinWidth(125);
        loginButton.setFont(fonts.lemonMilkBold14());
        loginButton.setMinHeight(30);
        loginButton.setEffect(dropShadow);

        loginButton.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                //System.out.println("Hello World");
                AttemptLogin();
            }
        }));

        Image loginIcon = new Image(this.getClass().getResource("loginIcon.png").toExternalForm());
        ImageView loginIconView = new ImageView(loginIcon);
        loginIconView.setFitHeight(14);
        loginIconView.setFitWidth(14);
        loginButton.setGraphic(loginIconView);
        loginButton.setContentDisplay(ContentDisplay.RIGHT);


        //Set and place the register button
        registerButtton.setId("registerButton");
        registerButtton.setLayoutX(35);
        registerButtton.setLayoutY(190);
        registerButtton.setMinWidth(120);
        registerButtton.setMinHeight(30);
        registerButtton.setFont(fonts.lemonMilkRegular13());
        registerButtton.setEffect(dropShadow);

        registerButtton.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                //System.out.println("Hello World");
                RegisterForm registerForm = new RegisterForm(db, stage);
                registerForm.createRegisterForm();
            }
        }));

        Image chessTwin = new Image(this.getClass().getResource("chess-twin.png").toExternalForm());
        ImageView chessTwinView = new ImageView(chessTwin);
        chessTwinView.setFitWidth(46);
        chessTwinView.setFitHeight(46);
        chessTwinView.setLayoutX(227);
        chessTwinView.setLayoutY(180);
        chessTwinView.setEffect(dropShadow);

        //add fields to the scene/stage
        root.getChildren().add(welcomeLabel);
        root.getChildren().add(loginButton);
        root.getChildren().add(registerButtton);
        root.getChildren().add(usernameField);
        root.getChildren().add(passwordField);
        root.getChildren().add(chessTwinView);

        stage.setTitle("Chess Library System");
        stage.setResizable(false);

        stage.setScene(loginScene);
        stage.show();
    }


    public void AttemptLogin (){
            int username = Integer.parseInt(usernameField.getText());
            String password = passwordField.getText();

            String givenAccount = username + password;

            boolean didMatch = db.getUserAccountQuery(String.valueOf(username), connectDB, givenAccount);


            //determines account type
            String accountType = "";
            int accountTypeAsInt = Integer.parseInt(db.getUserDetailsByID(username).get(3));

            if (accountTypeAsInt == 0){
                accountType = "normal";
            } if (accountTypeAsInt == 1){
                accountType = "admin";
            }

            System.out.println(accountType);

            root.getChildren().remove(emptyErrorMsg); // these 2 lines remove any labels that are currently on the window if they exist
            root.getChildren().remove(invalidErrorMsg); // nothing will happen if they don't exist

            if (usernameField.getLength() > 0 && passwordField.getLength() > 0) {
                if (didMatch) {
                    loginValidated = true;
                } if (!didMatch) {
                    invalidErrorMsg = new Label("Invalid credentials. Please try again.");
                    invalidErrorMsg.setId("invalidErrorMsg");
                    invalidErrorMsg.setFont(fonts.lemonMilkMedium12());
                    invalidErrorMsg.setLayoutX(115);
                    invalidErrorMsg.setLayoutY(12);


                    root.getChildren().add(invalidErrorMsg);
                }

            } else {
                emptyErrorMsg = new Label("Please enter your credentials before attempting login.");
                emptyErrorMsg.setId("emptyErrorMsg");
                emptyErrorMsg.setFont(fonts.lemonMilkMedium12());
                emptyErrorMsg.setLayoutX(65);
                emptyErrorMsg.setLayoutY(12);

                root.getChildren().add(emptyErrorMsg);
            }

            if (loginValidated) {
                Dashboard dashboard = new Dashboard(username, db);
                dashboard.createUserDashboard(accountType, username, connectDB);
                stage.close();
            }
    }
}

