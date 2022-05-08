package com.example.chesssys2;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.Connection;
import java.text.ParseException;
import java.util.GregorianCalendar;
import java.util.List;

public class LoanListing {
    private FontLoader fonts = new FontLoader();

    private Stage dashStage;
    private Database db;
//    private int user;
    private User user;
    private String accountType;

    private int loanID;
    private String productID;
    private String startDate;
    private String dueDate;

    private String prodImageName;
    private String prodTitle;
    private String prodDescription;
    private List<String> productDetails;

    private Label prodTitleLabel = new Label();
    private Label prodDescriptionLabel = new Label();
    private Label loanStartDateLabel = new Label();
    private Label loanDueDateLabel = new Label();

    private Label statusPreLabel = new Label();
    private Label loanStatusLabel = new Label();

    private Label areYouSureLabel = new Label();

    private Button returnButton = new Button("return");

    private Button noButton = new Button("NO");
    private Button yesButton = new Button("YES");


    private Pane loanElement = new Pane();

    private Stage returnStage = new Stage();
    private Scene returnScene;


    public LoanListing(Stage parentStage, Database parsedDB, int user, int loanID, String productID, String startDate, String dueDate){
        dashStage = parentStage;
        this.db = parsedDB;
        this.user = new User(this.db, user);
        this.accountType = this.user.getAccountType();

        this.loanID = loanID;
        this.productID = productID;
        this.startDate = startDate;
        this.dueDate = dueDate;


        this.productDetails = db.getProductDetailsFromID(productID);
        this.prodTitle = productDetails.get(1);
        this.prodDescription = productDetails.get(3);
        this.prodImageName = productDetails.get(6);

        System.out.println("Product details: " + productDetails);

    }

    public Pane createLoanElement(Group dash) throws ParseException {
        String css = this.getClass().getResource("app.css").toExternalForm();
        int intVersionOfProductID = Integer.parseInt(this.productID);

        Loan currentLoan = new Loan(db, this.user.getUserID(), this.loanID, intVersionOfProductID, this.dueDate);
        String status = currentLoan.getStatus();
        String hasBeenReturned = currentLoan.getLoanReturned(String.valueOf(this.loanID));


        Image prodImg = new Image(this.getClass().getResource(this.prodImageName).toExternalForm());
        ImageView prodImgView = new ImageView(prodImg);
        prodImgView.setId("imgView");
        prodImgView.setFitHeight(250);
        prodImgView.setFitWidth(250);
        prodImgView.relocate(10, 10);

        prodTitleLabel.setText(this.prodTitle);
        prodTitleLabel.setFont(fonts.lemonMilkMedium16());
        prodTitleLabel.setMaxWidth(230);
        prodTitleLabel.setMaxHeight(50);
        prodTitleLabel.setLayoutX(20);
        prodTitleLabel.setLayoutY(prodImgView.getLayoutY()+260);
        prodTitleLabel.setWrapText(true);

        prodDescriptionLabel.setId("descriptionLabel");
        prodDescriptionLabel.setText(this.prodDescription);
        prodDescriptionLabel.setFont(fonts.lemonMilkRegular13());
        prodDescriptionLabel.setMaxWidth(230);
        prodDescriptionLabel.setMaxHeight(60);
        prodDescriptionLabel.setLayoutX(20);
        prodDescriptionLabel.setLayoutY(prodTitleLabel.getLayoutY()+50);
        prodDescriptionLabel.setWrapText(true);

        loanStartDateLabel.setText("Issued On: " + this.startDate);
        loanStartDateLabel.setFont(fonts.lemonMilkRegular13());
        loanStartDateLabel.setMaxWidth(200);
        loanStartDateLabel.setMaxHeight(50);
        loanStartDateLabel.setLayoutX(20);
        loanStartDateLabel.setLayoutY(prodDescriptionLabel.getLayoutY()+120);

        statusPreLabel.setText("Status: ");
        statusPreLabel.setFont(fonts.lemonMilkMedium13());
        statusPreLabel.setMaxHeight(30);
        statusPreLabel.setMaxWidth(150);
        statusPreLabel.setLayoutY(prodDescriptionLabel.getLayoutY()+79);
        statusPreLabel.setLayoutX(20);

        loanStatusLabel.setText(status);
        loanStatusLabel.setFont(fonts.lemonMilkRegular13());
        loanStatusLabel.setMaxHeight(30);
        loanStatusLabel.setMaxWidth(150);
        loanStatusLabel.setLayoutY(prodDescriptionLabel.getLayoutY()+79);
        loanStatusLabel.setLayoutX(80);

        loanDueDateLabel.setText("Due Date: " + this.dueDate);
        loanDueDateLabel.setFont(fonts.lemonMilkRegular13());
        loanDueDateLabel.setMaxHeight(30);
        loanDueDateLabel.setMaxWidth(200);
        loanDueDateLabel.setLayoutY(prodDescriptionLabel.getLayoutY()+140);
        loanDueDateLabel.setLayoutX(20);

        returnButton.setId("loanButton");
        returnButton.setFont(fonts.lemonMilkMedium12());
        returnButton.setMinWidth(70);
        returnButton.setMaxHeight(30);
        returnButton.setLayoutX(160);
        returnButton.setLayoutY(prodDescriptionLabel.getLayoutY()+75);

        returnButton.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                openReturnConfirmationPage(currentLoan);
            }
        }));

        if (status == "active"){
            loanStatusLabel.setId("statusActive");
        } if (status == "complete"){
            loanStatusLabel.setId("statusFulfilled");
        } if (status == "due") {
            loanStatusLabel.setId("statusDue");
        }

        if (hasBeenReturned.equals("yes")){
            returnButton.setDisable(true);
            returnButton.setText("returned");
            returnButton.setMinWidth(85);
        }


        loanElement.setId("listing");
        loanElement.setPrefWidth(275);
        loanElement.setMinHeight(500);

        loanElement.getChildren().add(prodImgView);
        loanElement.getChildren().add(prodTitleLabel);
        loanElement.getChildren().add(prodDescriptionLabel);
        loanElement.getChildren().add(loanStartDateLabel);
        loanElement.getChildren().add(loanStatusLabel);
        loanElement.getChildren().add(statusPreLabel);
        loanElement.getChildren().add(loanDueDateLabel);

        loanElement.getChildren().add(returnButton);

        return loanElement;

    }

    public void openReturnConfirmationPage(Loan currentLoan){
        int userID = this.user.getUserID();

        Group returnGroup = new Group();
        returnScene = new Scene(returnGroup,  300, 200, Color.web("6998AB"));

        areYouSureLabel.setText("Are you sure you want to return this item?");
        areYouSureLabel.setLayoutX(50);
        areYouSureLabel.setLayoutY(50);

        noButton.setId("backButton");
        noButton.setLayoutY(150);
        noButton.setLayoutX(50);
        noButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                returnStage.close();
            }
        });

        yesButton.setId("backButton");
        yesButton.setLayoutY(100);
        yesButton.setLayoutX(50);
        yesButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                returnStage.close();
                dashStage.close();

                currentLoan.returnLoan();

                Connection updatedDB = db.setupDataBase();
                Dashboard updatedDashboard = new Dashboard(userID, db);
                updatedDashboard.createUserDashboard(accountType, userID, updatedDB);
                updatedDashboard.createProductsListScreen();
            }
        });



        returnGroup.getChildren().add(areYouSureLabel);
        returnGroup.getChildren().add(noButton);
        returnGroup.getChildren().add(yesButton);

        returnStage.setTitle("Loan An Item");
        returnStage.setResizable(false);

        returnStage.setScene(returnScene);
        returnStage.show();







    }
}
