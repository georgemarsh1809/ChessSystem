package com.example.chesssys2;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.awt.desktop.*;

public class ProductListing {
    private User user;
    private int userID;
    private String accountType;
    //initialise properties and nodes, i.e. button
    private Database db;
    private int productID;
    private String title;
    private String category;
    private String description;
    private String totalStock;
    private String numOnLoan;
    private String imageName;

    private int numAvailable;

    private FontLoader fonts = new FontLoader();

    private Button loanButton = new Button();
    private Button confirmLoanButton = new Button();
    private Button backToDashButton = new Button();
    private Button exportButton = new Button();
    private Button infoButton = new Button();

    private Pane productListing = new Pane();

    private Label headerLabel = new Label();
    private Line line = new Line();

    private Label titleLabel = new Label();
    private Label descriptionLabel = new Label();
    private Label numAvailableLabel = new Label();

    private Label loanHeaderLabel = new Label();
    private Label productTitleLabel = new Label();
    private Label loanDescriptionLabel = new Label();
    private Label productDescriptionLabel = new Label();

    private Label loanLengthQuery = new Label("How many days do you want to loan this item?");
    private Label todaysDateLabel = new Label();
    private Label dueDateLabel = new Label();
    private Label dueDateLabelPreText = new Label();

    private Label confirmationLabel = new Label();

    private Label exportPopUpLabel = new Label();
    private Label exportTitleLabel = new Label();
    private Label exportTypeLabel = new Label();
    private Label exportNumAvailableLabel = new Label();
    private Label exportNumOnLoanLabel = new Label();
    private Label exportNextDueDate = new Label();
    private Label fileWrittenLabel = new Label("File Written at /Users/georgemarsh/Documents/Uni/Semester 2/OOP Module/fileExports/...");


    private Scene loanScene;
    private Scene exportScene;

    private Stage dashStage;
    private Stage loanStage = new Stage();
    private Stage exportStage = new Stage();


    //constructor
    public ProductListing (Stage parentStage, Database parsedDB, int userID, String productID, String title, String cat, String description, String totalStock, String numOnLoan, String imageName){
        dashStage = parentStage;
        db = parsedDB;
        this.productID = Integer.parseInt(productID);
        this.userID = userID;
        this.user = new User(db, userID);
        this.accountType = user.getAccountType();

        this.title = title;
        this.category = cat;
        this.description = description;
        this.totalStock = totalStock;
        this.numOnLoan = numOnLoan;

        this.imageName = imageName;

        this.numAvailable = Integer.parseInt(this.totalStock) - Integer.parseInt(this.numOnLoan);
    }

    //getters and setters

    //method renders product listings on the find items screen
    public Pane createListing(Group group){
        Image prodImg = new Image(this.getClass().getResource(imageName).toExternalForm());
        ImageView prodImgView = new ImageView(prodImg);
        prodImgView.setId("imgView");
        prodImgView.setFitHeight(250);
        prodImgView.setFitWidth(250);
        prodImgView.relocate(10, 10);

        titleLabel.setText(this.title);
        titleLabel.setFont(fonts.lemonMilkMedium16());
        titleLabel.setMaxWidth(230);
        titleLabel.setMaxHeight(50);
        titleLabel.setLayoutX(20);
        titleLabel.setLayoutY(prodImgView.getLayoutY()+260);
        titleLabel.setWrapText(true);

        descriptionLabel.setId("descriptionLabel");
        descriptionLabel.setText(this.description);
        descriptionLabel.setFont(fonts.lemonMilkRegular13());
        descriptionLabel.setMaxWidth(230);
        descriptionLabel.setMaxHeight(70);
        descriptionLabel.setLayoutX(20);
        descriptionLabel.setLayoutY(titleLabel.getLayoutY()+60);
        descriptionLabel.setWrapText(true);

        numAvailableLabel.setText("Available: " + numAvailable);
        numAvailableLabel.setFont(fonts.lemonMilkMedium15());
        numAvailableLabel.setMaxHeight(30);
        numAvailableLabel.setMaxWidth(200);
        numAvailableLabel.setLayoutX(20);
        numAvailableLabel.setLayoutY(productListing.getLayoutY()+400);

        loanButton.setId("loanButton");
        loanButton.setText("loan");
        loanButton.setFont(fonts.lemonMilkMedium12());
        loanButton.setMinWidth(70);
        loanButton.setMaxHeight(30);
        loanButton.setLayoutX(numAvailableLabel.getLayoutX());
        loanButton.setLayoutY(productListing.getLayoutY()+448);

        if (this.numAvailable == 0) {
            loanButton.setDisable(true);
        }

        loanButton.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                //System.out.println("Hello World");
                openLoanPage();
            }
        }));


        infoButton.setId("exportButton");
        Image infoImg = new Image(this.getClass().getResource("circle-info-solid.png").toExternalForm());
        ImageView infoImgView = new ImageView(infoImg);
        infoImgView.setFitHeight(25);
        infoImgView.setFitWidth(25);
        infoButton.setGraphic(infoImgView);
        infoButton.setFont(fonts.lemonMilkMedium12());
        infoButton.setPrefWidth(20);
        infoButton.setPrefHeight(20);
        infoButton.setLayoutX(210);
        infoButton.setLayoutY(445);
        infoButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                openExportPage();
            }
        });



        productListing.setId("listing");
        productListing.setPrefWidth(400);
        productListing.setMinHeight(500);


        productListing.getChildren().add(prodImgView);
        productListing.getChildren().add(titleLabel);
        productListing.getChildren().add(numAvailableLabel);
        productListing.getChildren().add(descriptionLabel);
        productListing.getChildren().add(loanButton);
        productListing.getChildren().add(infoButton);



        System.out.println("listing: " + this.productID + ", " + this.title + ", " + this.category + ", " + this.description + ", " + this.totalStock + ", " + this.numOnLoan + ", " + this.imageName);

        return productListing;
    }

    private void openExportPage() {
        Group exportView = new Group();

        exportScene = new Scene(exportView,  900, 200, Color.web("6998AB"));
        String css = this.getClass().getResource("app.css").toExternalForm();
        this.exportScene.getStylesheets().add(css);

        //title
        //description?
        //o	Details about the item
        //o	Number of copies available
        //o	Number of copies on loan
        String titleLabelText = "Name: " + this.title;
        exportTitleLabel.setText(titleLabelText);
        exportTitleLabel.setMaxWidth(800);
        exportTitleLabel.setLayoutY(28);
        exportTitleLabel.setLayoutX(20);
        exportTitleLabel.setFont(fonts.lemonMilkMedium20());

        String typeLabelText = "Category: " + this.category;
        exportTypeLabel.setText(typeLabelText);
        exportTypeLabel.setLayoutY(60);
        exportTypeLabel.setLayoutX(20);
        exportTypeLabel.setFont(fonts.lemonMilkRegular18());

        String numOnLoanLabeltext = "Number On Loan: " + this.numOnLoan;
        exportNumOnLoanLabel.setText(numOnLoanLabeltext);
        exportNumOnLoanLabel.setLayoutY(90);
        exportNumOnLoanLabel.setLayoutX(20);
        exportNumOnLoanLabel.setFont(fonts.lemonMilkRegular18());

        String numAvailableText = "Number Available: " + this.numAvailable;
        exportNumAvailableLabel.setText(numAvailableText);
        exportNumAvailableLabel.setLayoutY(120);
        exportNumAvailableLabel.setLayoutX(20);
        exportNumAvailableLabel.setFont(fonts.lemonMilkRegular18());

        String nextDueDate = "Next Loan Due Date For This Item: " + "dunno yet";
        exportNextDueDate.setText(nextDueDate);
        exportNextDueDate.setLayoutY(150);
        exportNextDueDate.setLayoutX(20);
        exportNextDueDate.setFont(fonts.lemonMilkRegular18());


        //o	Earliest return date for any items on loan ??

        exportButton.setText(" Print to file");
        exportButton.setFont(fonts.lemonMilkMedium13());
        exportButton.setLayoutX(700);
        exportButton.setLayoutY(150);
        exportButton.setPrefWidth(180);
        exportButton.setPrefHeight(20);
        Image printImg = new Image(this.getClass().getResource("print-solid.png").toExternalForm());
        ImageView printImageView = new ImageView(printImg);
        printImageView.setFitHeight(20);
        printImageView.setFitWidth(20);
        exportButton.setGraphic(printImageView);
        exportButton.setContentDisplay(ContentDisplay.LEFT);
        exportButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    boolean fileExported = exportDetailsToFile();
                    if (fileExported){
                        exportView.getChildren().add(fileWrittenLabel);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        fileWrittenLabel.setFont(fonts.lemonMilkMedium12());
        fileWrittenLabel.setLayoutY(10);
        fileWrittenLabel.setPrefWidth(900);
        fileWrittenLabel.setAlignment(Pos.CENTER);


        exportView.getChildren().add(exportTitleLabel);
        exportView.getChildren().add(exportTypeLabel);
        exportView.getChildren().add(exportNumOnLoanLabel);
        exportView.getChildren().add(exportNumAvailableLabel);
        exportView.getChildren().add(exportNextDueDate);
        exportView.getChildren().add(exportButton);


        exportStage.setTitle("Export Details");
        exportStage.setResizable(false);

        exportStage.setScene(exportScene);
        exportStage.show();






    }

    private boolean exportDetailsToFile() throws IOException {
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
        Date todaysDate = new Date();

        String date = formatter.format(todaysDate);

        String path = "/Users/georgemarsh/Documents/Uni/Semester 2/OOP Module/fileExports/prodID" + this.productID + "-" + date + ".txt";


        String title = "Name: " + this.title;
        String cat = "Category: " + this.category;
        String onLoan = "Number On Loan: " + this.numOnLoan;
        String available = "Number Available: " + this.numAvailable;


        //text write test
        try {
            WriteToFile data = new WriteToFile(path);
            data.writeToFile(title, cat, onLoan, available);

            System.out.println("file written");


            return true;
//            File file = new File("C:\\Users\\georgemarsh\\Documents\\Uni\\Semester 2\\OOP Module\\fileExports\\prodID" + this.productID + "-"  + date + ".txt");
//            File file2 = new File("D:/Users/georgemarsh/Documents/add.py");
//
//            try {
//                if(!Desktop.isDesktopSupported()) {//check if Desktop is supported by Platform or not {
//                    System.out.println("not supported");
//                    return;
//                }
//
//                Desktop desktop = Desktop.getDesktop();
//
//                desktop.open(file2);
//                if (file2.getAbsoluteFile().exists()){
//                    desktop.open(file2);
//                } else {
//                    System.out.println("no file");
//                }
//            } catch (Exception e){
//                e.printStackTrace();
//            }

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

    }

    public void openLoanPage() {
        Group loanView = new Group();

        loanScene = new Scene(loanView,  1200, 600, Color.web("6998AB"));
        String css = this.getClass().getResource("app.css").toExternalForm();
        this.loanScene.getStylesheets().add(css);

        Pane sideImagePane = new Pane();
        sideImagePane.setStyle("-fx-background-color: #ffffff");
        sideImagePane.setPrefSize(600, 600);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date todaysDate = new Date();

        Image productImage = new Image(this.getClass().getResource(this.imageName).toExternalForm());
        ImageView productImageView = new ImageView(productImage);
        productImageView.setFitWidth(500);
        productImageView.setFitHeight(500);
        productImageView.relocate(50, 50);


        headerLabel.setText("new loan");
        headerLabel.setFont(fonts.lemonMilkMedium50());
        headerLabel.setLayoutX(750);
        headerLabel.setLayoutY(40);


        line.setId("lineLoanHeader");
        line.setStartX(675);
        line.setEndX(1125);
        line.setStartY(110);
        line.setEndY(110);
        line.setStrokeWidth(2);


        loanHeaderLabel.setText("Title: ");
        loanHeaderLabel.setFont(fonts.lemonMilkMedium27());
        loanHeaderLabel.setLayoutY(150);
        loanHeaderLabel.setLayoutX(630);

        productTitleLabel.setText(this.title);
        productTitleLabel.setFont(fonts.lemonMilkRegular22());
        productTitleLabel.setLayoutY(190);
        productTitleLabel.setLayoutX(630);
        productTitleLabel.setPrefWidth(500);
        productTitleLabel.setWrapText(true);

        loanDescriptionLabel.setText("Description: ");
        loanDescriptionLabel.setFont(fonts.lemonMilkMedium22());
        loanDescriptionLabel.setLayoutY(280);
        loanDescriptionLabel.setLayoutX(630);

        productDescriptionLabel.setText(this.description);
        productDescriptionLabel.setFont(fonts.lemonMilkRegular18());
        productDescriptionLabel.setLayoutX(630);
        productDescriptionLabel.setLayoutY(310);
        productDescriptionLabel.setPrefWidth(500);
        productDescriptionLabel.setWrapText(true);

        loanLengthQuery.setFont(fonts.lemonMilkRegular15());
        loanLengthQuery.setLayoutY(430);
        loanLengthQuery.setLayoutX(630);

        todaysDateLabel.setText("Today's date: " + formatter.format(todaysDate));
        todaysDateLabel.setFont(fonts.lemonMilkRegular15());
        todaysDateLabel.setLayoutX(630);
        todaysDateLabel.setLayoutY(490);

        ComboBox<Integer> loanLengthSelector = new ComboBox<>();
        loanLengthSelector.setId("loanLengthSelector");
        loanLengthSelector.getItems().addAll(7, 14, 28);
        loanLengthSelector.setLayoutY(428);
        loanLengthSelector.setLayoutX(1060);
//        loanLengthSelector.setValue(7);


        //works out the expiry date of the loan, depending on selection
        Calendar c = Calendar.getInstance();
        c.setTime(todaysDate);


        loanLengthSelector.getSelectionModel().selectedItemProperty().addListener((v, oldval, newval) -> {
            loanView.getChildren().remove(confirmLoanButton);
            c.setTime(todaysDate);
            c.add(Calendar.DATE, newval);
            String expiryDate = formatter.format(c.getTime());
            dueDateLabel.setText(expiryDate);
            loanView.getChildren().add(confirmLoanButton);
        });

        dueDateLabel.setLayoutX(770);
        dueDateLabel.setLayoutY(520);
        dueDateLabel.setFont(fonts.lemonMilkMedium15());

        dueDateLabelPreText.setText("Loan Due Date: ");
        dueDateLabelPreText.setFont(fonts.lemonMilkRegular15());
        dueDateLabelPreText.setLayoutX(630);
        dueDateLabelPreText.setLayoutY(520);

        confirmLoanButton.setId("backButton");
        confirmLoanButton.setText("Confirm");
        confirmLoanButton.setFont(fonts.lemonMilkBold14());
        confirmLoanButton.setLayoutY(485);
        confirmLoanButton.setLayoutX(1030);
        confirmLoanButton.setPrefWidth(100);
        confirmLoanButton.setPrefHeight(30);

        int parsedUser = this.userID;
        int parsedProdID = this.productID;
        int blankLoanID = 0;

        confirmLoanButton.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                String dueDate = dueDateLabel.getText();



                Loan newLoan = new Loan(db, parsedUser, blankLoanID, parsedProdID, dueDate);
                Boolean newLoanAdded = newLoan.addLoan();

                confirmationLabel.setText("New Loan Issued");
                confirmationLabel.setFont(fonts.lemonMilkMedium16());
                confirmationLabel.setLayoutY(20);
                confirmationLabel.setLayoutX(800);

                backToDashButton.setText("Back to Dashboard");
                backToDashButton.setId("backButton");
                backToDashButton.setFont(fonts.lemonMilkRegular13());
                backToDashButton.setLayoutX(975);
                backToDashButton.setLayoutY(530);
                backToDashButton.setPrefWidth(175);
                loanView.getChildren().add(backToDashButton);
                loanView.getChildren().add(confirmationLabel);


                backToDashButton.setOnMouseClicked((new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if (newLoanAdded) {
                            loanStage.close();
                            dashStage.close();

                            Connection updatedDB = db.setupDataBase();

                            Dashboard updatedDashboard = new Dashboard(userID, db);
                            updatedDashboard.createUserDashboard(accountType, userID, updatedDB);
                            updatedDashboard.createProductsListScreen();

                        } else {
                            System.out.println("Loan could not be added");
                        }
                    }
                }));


            }
        }));



        //adds all new nodes to parent group
        sideImagePane.getChildren().add(productImageView);
        loanView.getChildren().add(sideImagePane);
        loanView.getChildren().add(headerLabel);
        loanView.getChildren().add(line);
        loanView.getChildren().add(loanHeaderLabel);
        loanView.getChildren().add(productTitleLabel);
        loanView.getChildren().add(loanDescriptionLabel);
        loanView.getChildren().add(productDescriptionLabel);
        loanView.getChildren().add(todaysDateLabel);
        loanView.getChildren().add(loanLengthSelector);
        loanView.getChildren().add(loanLengthQuery);
        loanView.getChildren().add(dueDateLabel);
        loanView.getChildren().add(dueDateLabelPreText);


        loanStage.setTitle("Loan An Item");
        loanStage.setResizable(false);

        loanStage.setScene(loanScene);
        loanStage.show();

    }

}
