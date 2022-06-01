package com.example.chesssys2;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ApprovalListing {
    private final Database db;

    private AdminUser user;
    private final int userID;

    private final int requestID;
    private final int requestedUserID;
    private final String requestedEmail;
    private final int requestedAdminAccess;
    private final String requestedPassword;

    private final FontLoader fonts = new FontLoader();

    private final Button approveButton = new Button();
    private final Button declineButton = new Button();

    private final Pane requestListing = new Pane();

    private final Label idLabel = new Label();
    private final Label emailLabel = new Label();
    private final Label adminRequestLabel = new Label();

    private final Label loanHeaderLabel = new Label();
    private final Label productTitleLabel = new Label();
    private final Label loanDescriptionLabel = new Label();
    private final Label productDescriptionLabel = new Label();

    private Scene loanScene;

    private final Stage dashStage;
    private final Stage loanStage = new Stage();


    //constructor
    public ApprovalListing(Stage parentStage, Database parsedDB, int userID, int requestID, int requestedUserID, String requestedUserEmail, String requestedPassword, int requestedAdminAccess){
        dashStage = parentStage;
        db = parsedDB;
        this.requestID = requestID;
        this.requestedUserID = requestedUserID;
        this.user = new AdminUser(db, userID); //replace with adminUser?
        this.userID = this.user.getUserID();

        if (this.user.getAccountType().equals("admin")){
            this.user = new AdminUser(db, this.userID);
        }

        this.requestedEmail = requestedUserEmail;
        this.requestedAdminAccess = requestedAdminAccess;
        this.requestedPassword = requestedPassword;


    }

    //getters and setters

    //method renders product listings on the find items screen
    public Pane createListing(Group group){
        idLabel.setText(String.valueOf(this.requestedUserID));
        idLabel.setFont(fonts.lemonMilkMedium13());
        idLabel.setMaxWidth(100);
        idLabel.setMaxHeight(70);
        idLabel.setLayoutX(50);
        idLabel.setLayoutY(5);

        emailLabel.setId("descriptionLabel");
        emailLabel.setText(this.requestedEmail);
        emailLabel.setFont(fonts.lemonMilkRegular13());
        emailLabel.setMaxWidth(200);
        emailLabel.setMaxHeight(70);
        emailLabel.setLayoutX(200);
        emailLabel.setLayoutY(5);
        emailLabel.setWrapText(true);

        adminRequestLabel.setId("descriptionLabel");
        if (this.requestedAdminAccess == 0){
            adminRequestLabel.setText("no");
        } if (this.requestedAdminAccess == 1){
            adminRequestLabel.setText("yes");
        }
        adminRequestLabel.setFont(fonts.lemonMilkRegular13());
        adminRequestLabel.setMaxWidth(230);
        adminRequestLabel.setMaxHeight(100);
        adminRequestLabel.setLayoutX(480);
        adminRequestLabel.setLayoutY(5);
        adminRequestLabel.setWrapText(true);


        approveButton.setId("approveButton");
        approveButton.setText("approve");
        approveButton.setFont(fonts.lemonMilkMedium12());
        approveButton.setMinWidth(70);
        approveButton.setMaxHeight(30);
        approveButton.setLayoutX(650);
        approveButton.setLayoutY(5);
//
        approveButton.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                //System.out.println("Hello World");
                try {
                    user.approveAccountCreation(requestID, requestedEmail, requestedUserID, requestedPassword, requestedAdminAccess);
//                    db.createNewUser(requestedEmail, requestedUserID, requestedPassword, requestedAdminAccess);
//                    db.removeRequest(requestID);
                    Connection updatedDB = db.setupDataBase();
                    dashStage.close();
                    Dashboard updatedDash = new Dashboard(userID, db);
                    updatedDash.createUserDashboard("admin", updatedDB);
                    updatedDash.createSidebar(true);
                    updatedDash.createApprovalsListScreen();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));

        declineButton.setId("declineButton");
        declineButton.setText("decline");
        declineButton.setFont(fonts.lemonMilkMedium12());
        declineButton.setMinWidth(70);
        declineButton.setMaxHeight(30);
        declineButton.setLayoutX(750);
        declineButton.setLayoutY(5);

        declineButton.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                try {
                    user.declineAccountCreation(requestID);
                    Connection updatedDB = db.setupDataBase();
                    dashStage.close();
                    Dashboard updatedDash = new Dashboard(userID, db);
                    updatedDash.createUserDashboard("admin", updatedDB);
                    updatedDash.createSidebar(true);
                    updatedDash.createApprovalsListScreen();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));


        requestListing.setId("approvalListing");
        requestListing.setPrefWidth(950);
        requestListing.setMinHeight(30);


        requestListing.getChildren().add(idLabel);
        requestListing.getChildren().add(emailLabel);
        requestListing.getChildren().add(adminRequestLabel);
        requestListing.getChildren().add(approveButton);
        requestListing.getChildren().add(declineButton);


        return requestListing;
    }

}
