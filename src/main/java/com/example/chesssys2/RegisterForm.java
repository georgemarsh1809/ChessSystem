package com.example.chesssys2;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;

public class RegisterForm {
    private Group reg = new Group();
    private Scene registerFormScene = new Scene(reg,  500, 420, Color.web("B1D0E0"));
    private Stage stage = new Stage();

    private Database db;
    private Connection connectDB;

    private TextField newUsernameField = new TextField();
    private TextField newEmailField = new TextField();
    private PasswordField newPasswordField = new PasswordField();
    private PasswordField retypedPasswordField = new PasswordField();
    private Button submitButton = new Button("submit");

    private CheckBox adminRequestBox = new CheckBox("request admin access");

    private Label emtpyFieldsErrorLabel = new Label("Please fill in all fields before submitting.");
    private Label unmatchedPasswordsLabel = new Label("Passwords do not match. Please try again.");

    private Boolean didPasswordsMatch = false;

    private FontLoader fonts = new FontLoader();

    public RegisterForm (Database parsedDB) {
        db = parsedDB; //parse in an instance of the database
        connectDB = db.setupDataBase();
    }


    public void createRegisterForm() {

        String css = this.getClass().getResource("app.css").toExternalForm();
        this.registerFormScene.getStylesheets().add(css);

        Image bishopIcon = new Image(this.getClass().getResource("bishop2.png").toExternalForm());
        ImageView bishopIconView = new ImageView(bishopIcon);
        bishopIconView.setFitHeight(17);
        bishopIconView.setPreserveRatio(true);
        bishopIconView.setLayoutY(80);
        bishopIconView.setLayoutX(242);

        Line line1 = new Line();
        line1.setId("line1");
        line1.setStartX(75);
        line1.setEndX(235);
        line1.setStartY(90);
        line1.setEndY(90);
        line1.setStrokeWidth(1);

        Line line2 = new Line();
        line2.setId("line1");
        line2.setStartX(260);
        line2.setEndX(430);
        line2.setStartY(90);
        line2.setEndY(90);
        line2.setStrokeWidth(1);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(1);
        dropShadow.setOffsetY(3);
        dropShadow.setColor(Color.color(0.4, 0.5, 0.5));


        Label createAccountLabel = new Label("create your account");
        createAccountLabel.setId("createAccountLabel");
        createAccountLabel.setFont(fonts.lemonMilkMedium30());
        createAccountLabel.setMinWidth(500);
        createAccountLabel.setAlignment(Pos.CENTER);
        createAccountLabel.setLayoutY(40);

        setUpRegistrationFields(); //sets and places all text fields
        setUpRegistrationLabels(); //sets and places all the labels


        submitButton.setId("submitButton");
        submitButton.setLayoutX(190);
        submitButton.setLayoutY(330);
        submitButton.setMinWidth(120);
        submitButton.setMinHeight(30);
        submitButton.setEffect(dropShadow);

        adminRequestBox.setLayoutX(175);
        adminRequestBox.setLayoutY(370);
        adminRequestBox.setId("adminBox");
        adminRequestBox.setMinWidth(150);


        emtpyFieldsErrorLabel.setId("emptyFieldsError");
        emtpyFieldsErrorLabel.setFont(fonts.lemonMilkMedium12());
        emtpyFieldsErrorLabel.setLayoutX(115);
        emtpyFieldsErrorLabel.setLayoutY(12);


        submitButton.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                //System.out.println("Hello World");
                String email = newEmailField.getText();
                String username = newUsernameField.getText();
                String chosenPassword = newPasswordField.getText();
                String repeatedPassword = retypedPasswordField.getText();

                if (chosenPassword.equals(repeatedPassword)) {
                    didPasswordsMatch = true;
                } else {
                    didPasswordsMatch = false;
                }

                if (email.isBlank() || username.isBlank() || chosenPassword.isBlank() || repeatedPassword.isBlank()) {
                    reg.getChildren().remove(emtpyFieldsErrorLabel);
                    reg.getChildren().remove(unmatchedPasswordsLabel);

                    reg.getChildren().add(emtpyFieldsErrorLabel);

                } if (!didPasswordsMatch) {
                    reg.getChildren().remove(emtpyFieldsErrorLabel);
                    reg.getChildren().remove(unmatchedPasswordsLabel);

                    unmatchedPasswordsLabel.setId("unmatchedPasswordsError");
                    unmatchedPasswordsLabel.setFont(fonts.lemonMilkMedium12());
                    unmatchedPasswordsLabel.setLayoutX(105);
                    unmatchedPasswordsLabel.setLayoutY(12);
                    reg.getChildren().add(unmatchedPasswordsLabel);

                } else {
                    reg.getChildren().remove(emtpyFieldsErrorLabel);
                    reg.getChildren().remove(unmatchedPasswordsLabel);

                    try {
                        AttemptRegistration(email, username, chosenPassword);
                        AddLabelsOnceRegistered();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }));


        reg.getChildren().add(bishopIconView);
        reg.getChildren().add(createAccountLabel);
        reg.getChildren().add(line1);
        reg.getChildren().add(line2);

        reg.getChildren().add(adminRequestBox);

        stage.setTitle("Registration Form");
        stage.setResizable(false);

        stage.setScene(registerFormScene);
        stage.show();
    }

    public void setUpRegistrationFields () {

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(1);
        dropShadow.setOffsetY(3);
        dropShadow.setColor(Color.color(0.4, 0.5, 0.5));


        //Set and place the new username field
        newUsernameField.setId("newUsername");
        newUsernameField.setAlignment(Pos.CENTER_LEFT);
        newUsernameField.setLayoutX(200);
        newUsernameField.setLayoutY(130);
        newUsernameField.setMinHeight(24);
        newUsernameField.setMinWidth(240);
        newUsernameField.setEffect(dropShadow);


        //Set and place the new email field
        newEmailField.setId("newEmail");
        newEmailField.setAlignment(Pos.CENTER_LEFT);
        newEmailField.setLayoutX(200);
        newEmailField.setLayoutY(170);
        newEmailField.setMinHeight(24);
        newEmailField.setMinWidth(240);
        newEmailField.setEffect(dropShadow);

        //Set and place the new password field
        newPasswordField.setId("newPassword");
        newPasswordField.setAlignment(Pos.CENTER_LEFT);
        newPasswordField.setLayoutX(200);
        newPasswordField.setLayoutY(230);
        newPasswordField.setMinHeight(24);
        newPasswordField.setMinWidth(240);
        newPasswordField.setEffect(dropShadow);

        //Set and place the retyped password field
        retypedPasswordField.setId("retypedPassword");
        retypedPasswordField.setAlignment(Pos.CENTER_LEFT);
        retypedPasswordField.setLayoutX(200);
        retypedPasswordField.setLayoutY(270);
        retypedPasswordField.setMinHeight(24);
        retypedPasswordField.setMinWidth(240);
        retypedPasswordField.setEffect(dropShadow);

        reg.getChildren().add(newUsernameField);
        reg.getChildren().add(newEmailField);
        reg.getChildren().add(newPasswordField);
        reg.getChildren().add(retypedPasswordField);
    }

    public void setUpRegistrationLabels () {
        Label usernameLabel = new Label("pick a username");
        usernameLabel.setFont(fonts.lemonMilkMedium13());
        usernameLabel.setId("usernameLabel");

        usernameLabel.setLayoutY(134);
        usernameLabel.setLayoutX(60);


        Label emailLabel = new Label("your email");
        emailLabel.setFont(fonts.lemonMilkMedium13());
        emailLabel.setId("emailLabel");

        emailLabel.setLayoutY(174);
        emailLabel.setLayoutX(99);


        Label passwordLabel = new Label("choose a password");
        passwordLabel.setFont(fonts.lemonMilkMedium13());
        passwordLabel.setId("passwordLabel");

        passwordLabel.setLayoutY(234);
        passwordLabel.setLayoutX(30);

        Label retypedPassLabel = new Label("retype password");
        retypedPassLabel.setFont(fonts.lemonMilkMedium13());
        retypedPassLabel.setId("passwordLabel");

        retypedPassLabel.setLayoutY(274);
        retypedPassLabel.setLayoutX(53);


        reg.getChildren().add(usernameLabel);
        reg.getChildren().add(emailLabel);
        reg.getChildren().add(passwordLabel);
        reg.getChildren().add(retypedPassLabel);
        reg.getChildren().add(submitButton);



    }

    public void AddLabelsOnceRegistered(){
        Label createdLabel = new Label("Account created! Press 'Back' to login.");
        createdLabel.setFont(fonts.lemonMilkMedium13());
        createdLabel.setId("passwordLabel");
        createdLabel.setMinWidth(500);
        createdLabel.setAlignment(Pos.CENTER);

        createdLabel.setLayoutY(20);

        Button backButton = new Button(" Back  ");
        backButton.setFont(fonts.lemonMilkBold14());
        backButton.setId("backButton");
        backButton.setLayoutY(350);
        backButton.setLayoutX(375);

        backButton.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                //System.out.println("Hello World");
                ;
                stage.close();
                Database updatedDB = new Database();

                LoginScreen loginScreen = new LoginScreen(updatedDB);
                loginScreen.CreateLoginScreen();
            }
        }));

        Image backIcon = new Image(this.getClass().getResource("backIcon.png").toExternalForm());
        ImageView backIconView = new ImageView(backIcon);
        backIconView.setFitHeight(14);
        backIconView.setFitWidth(14);
        backButton.setGraphic(backIconView);
        backButton.setContentDisplay(ContentDisplay.LEFT);


        reg.getChildren().add(createdLabel);
        reg.getChildren().add(backButton);

    }

    public void AttemptRegistration(String email, String username, String password) throws SQLException {
        System.out.println("Added new user: " + username + ", " + email + ", " + password);
        System.out.println("Did passwords match? " + didPasswordsMatch);

        Boolean didUserRequestAdminAccess = adminRequestBox.isSelected();

        System.out.println("Checkbox ticked: " + didUserRequestAdminAccess);

        db.createNewUser(email, username, password, didUserRequestAdminAccess, connectDB);


    }
}
