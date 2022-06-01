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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class RegisterForm {
    private final Group reg = new Group();
    private final Scene registerFormScene = new Scene(reg,  500, 420, Color.web("B1D0E0"));
    private final Stage stage = new Stage();

    private final Database db;
    private final Connection connectDB;

    private final TextField newUsernameField = new TextField();

    private final TextField newEmailField = new TextField();
    private final PasswordField newPasswordField = new PasswordField();
    private final PasswordField retypedPasswordField = new PasswordField();
    private final Button submitButton = new Button("submit");

    private final CheckBox adminRequestBox = new CheckBox("request admin access");

    private final Label emtpyFieldsErrorLabel = new Label("Please fill in all fields before submitting.");
    private final Label unmatchedPasswordsLabel = new Label("Passwords do not match. Please try again.");
    private final Label usernameAlreadyExistsError = new Label("That username is taken - please choose another");
    private final Label incorrectEmailFormatError = new Label("Please provide a valid email");
    private final Label incorrectUsernameFormatError = new Label("Please only use numbers for your requested ID");

    private Boolean didPasswordsMatch = false;

    private final FontLoader fonts = new FontLoader();

    public RegisterForm (Database parsedDB, Stage oldStage) {
        Stage prevStage = oldStage;
        oldStage.close();
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


        Label createAccountLabel = new Label("register your account");
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
                Boolean doesUsernameAlreadyExist = null;
                Boolean isPasswordMoreThan8Chars;
                Boolean doesEmailFollowRegEx;

                Boolean validEntries = true;

                String email = newEmailField.getText();
                String username = newUsernameField.getText();
                String chosenPassword = newPasswordField.getText();
                String repeatedPassword = retypedPasswordField.getText();

                List<String> allAccounts = db.getAllAccounts(connectDB);
                System.out.println("All accounts: " + allAccounts);

                List<String> accountsThatEqualUsername = db.getAccountsThatEqual(username, connectDB);
                System.out.println("Accounts that are the same: " + accountsThatEqualUsername);


                doesUsernameAlreadyExist = accountsThatEqualUsername.size() > 0;

                System.out.println(allAccounts);


                didPasswordsMatch = chosenPassword.equals(repeatedPassword);


                //The following, verifies that all fields are filled correctly
                if (email.isBlank() || username.isBlank() || chosenPassword.isBlank() || repeatedPassword.isBlank()) {
                    reg.getChildren().remove(emtpyFieldsErrorLabel);
                    reg.getChildren().remove(unmatchedPasswordsLabel);
                    reg.getChildren().remove(usernameAlreadyExistsError);
                    reg.getChildren().remove(incorrectEmailFormatError);
                    reg.getChildren().remove(incorrectUsernameFormatError);

                    reg.getChildren().add(emtpyFieldsErrorLabel);

                    validEntries = false;

                } if (doesUsernameAlreadyExist) {
                    reg.getChildren().remove(emtpyFieldsErrorLabel);
                    reg.getChildren().remove(unmatchedPasswordsLabel);
                    reg.getChildren().remove(usernameAlreadyExistsError);
                    reg.getChildren().remove(incorrectEmailFormatError);
                    reg.getChildren().remove(incorrectUsernameFormatError);

                    usernameAlreadyExistsError.setId("usernameAlreadyExistsErrorLabel");
                    usernameAlreadyExistsError.setFont(fonts.lemonMilkRegular13());
                    usernameAlreadyExistsError.setMinWidth(500);
                    usernameAlreadyExistsError.setAlignment(Pos.CENTER);
                    usernameAlreadyExistsError.setLayoutY(20);

                    reg.getChildren().add(usernameAlreadyExistsError);
                    newUsernameField.clear();

                    validEntries = false;


                } if (!username.matches("[0-9]*")) {
                    reg.getChildren().remove(emtpyFieldsErrorLabel);
                    reg.getChildren().remove(unmatchedPasswordsLabel);
                    reg.getChildren().remove(usernameAlreadyExistsError);
                    reg.getChildren().remove(incorrectEmailFormatError);
                    reg.getChildren().remove(incorrectUsernameFormatError);

                    incorrectUsernameFormatError.setId("incorrectEmailFormatError");
                    incorrectUsernameFormatError.setLayoutX(105);
                    incorrectUsernameFormatError.setFont(fonts.lemonMilkRegular13());
                    incorrectUsernameFormatError.setLayoutY(20);
                    incorrectUsernameFormatError.setMinWidth(500);
                    incorrectUsernameFormatError.setAlignment(Pos.CENTER);
                    reg.getChildren().add(incorrectUsernameFormatError);

                    validEntries = false;


                }   if (!email.matches("^(.+)@(.+)$")) {
                    reg.getChildren().remove(emtpyFieldsErrorLabel);
                    reg.getChildren().remove(unmatchedPasswordsLabel);
                    reg.getChildren().remove(usernameAlreadyExistsError);
                    reg.getChildren().remove(incorrectEmailFormatError);
                    reg.getChildren().remove(incorrectUsernameFormatError);

                    incorrectEmailFormatError.setId("incorrectEmailFormatError");
                    incorrectEmailFormatError.setFont(fonts.lemonMilkRegular13());
                    incorrectEmailFormatError.setMinWidth(500);
                    incorrectEmailFormatError.setAlignment(Pos.CENTER);
                    incorrectEmailFormatError.setLayoutY(20);

                    reg.getChildren().add(incorrectEmailFormatError);
                    newEmailField.clear();

                    validEntries = false;


                } if (!didPasswordsMatch) {
                    reg.getChildren().remove(emtpyFieldsErrorLabel);
                    reg.getChildren().remove(unmatchedPasswordsLabel);
                    reg.getChildren().remove(usernameAlreadyExistsError);
                    reg.getChildren().remove(incorrectEmailFormatError);
                    reg.getChildren().remove(incorrectUsernameFormatError);

                    unmatchedPasswordsLabel.setId("incorrectEmailFormatError");
                    unmatchedPasswordsLabel.setFont(fonts.lemonMilkRegular13());
                    unmatchedPasswordsLabel.setLayoutY(20);
                    unmatchedPasswordsLabel.setMinWidth(500);
                    unmatchedPasswordsLabel.setAlignment(Pos.CENTER);
                    reg.getChildren().add(unmatchedPasswordsLabel);

                    validEntries = false;


                }   if (validEntries) {
                    reg.getChildren().remove(emtpyFieldsErrorLabel);
                    reg.getChildren().remove(unmatchedPasswordsLabel);
                    reg.getChildren().remove(usernameAlreadyExistsError);
                    reg.getChildren().remove(incorrectEmailFormatError);
                    reg.getChildren().remove(incorrectUsernameFormatError);
//
                    try {
                        createNewAccountRequest(email, username, chosenPassword); //Attempt to add a new account
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
        newUsernameField.setPromptText("[0-9]");
        newUsernameField.setAlignment(Pos.CENTER_LEFT);
        newUsernameField.setLayoutX(200);
        newUsernameField.setLayoutY(130);
        newUsernameField.setMinHeight(24);
        newUsernameField.setMinWidth(200);
        newUsernameField.setEffect(dropShadow);


        //Set and place the new email field
        newEmailField.setId("newEmail");
        newEmailField.setPromptText("example@example.com");
        newEmailField.setAlignment(Pos.CENTER_LEFT);
        newEmailField.setLayoutX(200);
        newEmailField.setLayoutY(170);
        newEmailField.setMinHeight(24);
        newEmailField.setMinWidth(200);
        newEmailField.setEffect(dropShadow);

        //Set and place the new password field
        newPasswordField.setId("newPassword");
        newPasswordField.setAlignment(Pos.CENTER_LEFT);
        newPasswordField.setLayoutX(200);
        newPasswordField.setLayoutY(230);
        newPasswordField.setMinHeight(24);
        newPasswordField.setMinWidth(200);
        newPasswordField.setEffect(dropShadow);

        //Set and place the retyped password field
        retypedPasswordField.setId("retypedPassword");
        retypedPasswordField.setAlignment(Pos.CENTER_LEFT);
        retypedPasswordField.setLayoutX(200);
        retypedPasswordField.setLayoutY(270);
        retypedPasswordField.setMinHeight(24);
        retypedPasswordField.setMinWidth(200);
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
        Label createdLabel = new Label("Account pending approval. Press 'Back' to return to login.");
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
                stage.close();
                Database updatedDB = new Database();
                updatedDB.setupDataBase();

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

    public void createNewAccountRequest(String email, String username, String password) throws SQLException {
        System.out.println("Added new user: " + username + ", " + email + ", " + password);
        System.out.println("Did passwords match? " + didPasswordsMatch);

        Boolean didUserRequestAdminAccess = adminRequestBox.isSelected();

        int didUserRequestAdminAccessAsInt = 0;

        if (didUserRequestAdminAccess){
            didUserRequestAdminAccessAsInt = 1;
        }



        System.out.println("Checkbox ticked: " + didUserRequestAdminAccess);

        db.newAccountRequest(Integer.parseInt(username), email, password, didUserRequestAdminAccessAsInt);


    }
}
