package com.example.chesssys2;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.util.List;

public class Dashboard {

    private Group dash = new Group();
    private Scene dashboard = new Scene(dash,  1920, 1080, Color.web("6998AB"));
    private Stage dashStage = new Stage();

    private FontLoader fonts = new FontLoader();

    private Database db;
    private Connection connectDB;

    private int user;

    private ScrollPane findItemsScroll = new ScrollPane();
    private ScrollPane myLoansScroll = new ScrollPane();
    private ScrollPane approvalsScroll = new ScrollPane();

    private GridPane findItemsGrid = new GridPane();
    private GridPane myLoansGrid = new GridPane();
    private GridPane approvalsGrid = new GridPane();

    private Pane headerPane = new Pane();

    private Label idHeaderLabel = new Label("Requested ID");
    private Label emailHeaderLabel = new Label("Requested Email");
    private Label adminAccessHeaderLabel = new Label("Admin Access Request");
    private Label actionHeaderLabel = new Label("Action");

    private Button findItemsButton = new Button("  find items");
    private Button myLoansButton = new Button("  my loans");
    private Button approvalsButton = new Button(" approvals");

    private Label findItemsMainLabel = new Label("find items");
    private Label findItemsSubLabel = new Label("search for items to loan...");

    private Label myLoansMainLabel = new Label("My Loans");
    private Label myLoansSubLabel = new Label("view all of your current open loans...");

    private Label approvalsMainLabel = new Label("approvals");
    private Label approvalsSubLabel = new Label("view all open account creation requests..");

    public Dashboard (int username, Database parsedDB) {
        db = parsedDB;
        connectDB = db.setupDataBase();


        User currentUser = new User(db, username);

        this.user = currentUser.getUserID();

    }

    public void createUserDashboard(String accountType, int userID, Connection parsedConnectDB){
        String css = this.getClass().getResource("app.css").toExternalForm();
        this.dashboard.getStylesheets().add(css);

        connectDB = parsedConnectDB;

        Boolean isAdmin = false;

        if (accountType.equals("admin")){
            isAdmin = true;
        } if (accountType.equals("normal")){
            isAdmin = false;
        }




        createSidebar(isAdmin, userID);


        dashStage.setTitle("Chess Library System");
        dashStage.setResizable(false);

        dashStage.setScene(dashboard);
        dashStage.show();

    }

    public void createSidebar (Boolean isAdmin, int userID){
        Boolean userIsAdmin = isAdmin;

        Pane sideBar = new Pane();
        sideBar.setStyle("-fx-background-color: #1A374D");
        sideBar.setPrefSize(282, 1080);

        Line line = new Line();
        line.setId("lineMyLoans");
        line.setStartX(382);
        line.setEndX(1375);
        line.setStartY(300);
        line.setEndY(300);
        line.setStrokeWidth(6);


        if (userIsAdmin){
            approvalsButton.setId("approvalsButton");
            approvalsButton.setFont(fonts.lemonMilkRegular22());
            approvalsButton.setLayoutX(30);
            approvalsButton.setLayoutY(600);

            approvalsButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    try {
                        dash.getChildren().remove(myLoansScroll);
                        dash.getChildren().remove(myLoansMainLabel);
                        dash.getChildren().remove(myLoansSubLabel);
                        dash.getChildren().remove(findItemsScroll);
                        dash.getChildren().remove(findItemsMainLabel);
                        dash.getChildren().remove(findItemsSubLabel);

                        createApprovalsListScreen();

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });

            sideBar.getChildren().add(approvalsButton);

        }

        Image profileIcon = new Image(this.getClass().getResource("userIcon.png").toExternalForm());
        ImageView profileIconView = new ImageView(profileIcon);

        profileIconView.setPreserveRatio(true);
        profileIconView.setFitHeight(84);
        profileIconView.setLayoutX(99);
        profileIconView.setLayoutY(75);

        findItemsButton.setId("findItemsButton");
        findItemsButton.setFont(fonts.lemonMilkRegular22());
        findItemsButton.setLayoutX(30);
        findItemsButton.setLayoutY(300);

        findItemsButton.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                dash.getChildren().remove(myLoansScroll);
                dash.getChildren().remove(myLoansMainLabel);
                dash.getChildren().remove(myLoansSubLabel);
                dash.getChildren().remove(approvalsScroll);
                dash.getChildren().remove(approvalsMainLabel);
                dash.getChildren().remove(approvalsSubLabel);
                dash.getChildren().remove(line);

                myLoansButton.setFont(fonts.lemonMilkRegular22());

                dash.getChildren().add(line);

                createProductsListScreen();

            }

        }));

        Image findItemsIcon = new Image(this.getClass().getResource("findItemsIcon.png").toExternalForm());
        ImageView findItemsIconView = new ImageView(findItemsIcon);

        findItemsIconView.setFitHeight(20);
        findItemsIconView.setFitWidth(20);
        findItemsButton.setGraphic(findItemsIconView);
        findItemsButton.setContentDisplay(ContentDisplay.LEFT);

        myLoansButton.setId("myLoansButton");
        myLoansButton.setFont(fonts.lemonMilkRegular22());
        myLoansButton.setLayoutX(30);
        myLoansButton.setLayoutY(360);

        Image myLoansIcon = new Image(this.getClass().getResource("chess-king.png").toExternalForm());
        ImageView myLoansIconView = new ImageView(myLoansIcon);
        myLoansIconView.setFitHeight(25);
        myLoansIconView.setFitWidth(20);
        myLoansButton.setGraphic(myLoansIconView);
        myLoansButton.setContentDisplay(ContentDisplay.LEFT);

        myLoansButton.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                dash.getChildren().remove(findItemsScroll);
                dash.getChildren().remove(findItemsMainLabel);
                dash.getChildren().remove(findItemsSubLabel);
                dash.getChildren().remove(approvalsScroll);
                dash.getChildren().remove(approvalsMainLabel);
                dash.getChildren().remove(approvalsSubLabel);

                try {
                    createMyLoansScreen();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                findItemsButton.setFont(fonts.lemonMilkRegular22());
            }

        }));


        sideBar.getChildren().add(findItemsButton);
        sideBar.getChildren().add(myLoansButton);
        dash.getChildren().add(sideBar);
        dash.getChildren().add(profileIconView);
    }

    public void createMyLoansScreen() throws ParseException {
        dash.getChildren().remove(findItemsScroll);
        dash.getChildren().remove(approvalsScroll);
        myLoansButton.setFont(fonts.lemonMilkMedium22());

        myLoansMainLabel.setId("myLoansMainLabel");
        myLoansMainLabel.setFont(fonts.lemonMilkMedium96());
        myLoansMainLabel.setLayoutX(380);
        myLoansMainLabel.setLayoutY(70);

        myLoansSubLabel.setId("findItemsMainLabel");
        myLoansSubLabel.setFont(fonts.lemonMilkMedium36());
        myLoansSubLabel.setLayoutX(380);
        myLoansSubLabel.setLayoutY(230);



//        Image magnifyingGlass = new Image(this.getClass().getResource("magnifying-glass-white.png").toExternalForm());
//        ImageView magnifyingGlassView = new ImageView(magnifyingGlass);
//        magnifyingGlassView.setFitHeight(100);
//        magnifyingGlassView.setFitWidth(100);
//
//        magnifyingGlassView.setLayoutY(70);
//        magnifyingGlassView.setLayoutX(1350);

        placeLoansGrid(); //adds all items to the grid, then to the scroll pane

        myLoansScroll.setContent(myLoansGrid);
        myLoansScroll.setLayoutX(380);
        myLoansScroll.setLayoutY(320);
        myLoansScroll.setPrefHeight(680);
        myLoansScroll.setPrefWidth(1000);
        myLoansScroll.setId("scrollPane");
        myLoansScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        dash.getChildren().add(myLoansMainLabel);
        dash.getChildren().add(myLoansSubLabel);
//        dash.getChildren().add(magnifyingGlassView);
        dash.getChildren().add(myLoansScroll);


    }

    public void createProductsListScreen(){
        dash.getChildren().remove(myLoansScroll);
//        dash.getChildren().remove(approvalsScroll);
//        dash.getChildren().remove(approvalsMainLabel);
//        dash.getChildren().remove(approvalsSubLabel);

        findItemsButton.setFont(fonts.lemonMilkMedium22());

        findItemsMainLabel.setId("findItemsMainLabel");
        findItemsMainLabel.setFont(fonts.lemonMilkMedium96());
        findItemsMainLabel.setLayoutX(380);
        findItemsMainLabel.setLayoutY(70);

        findItemsSubLabel.setId("findItemsMainLabel");
        findItemsSubLabel.setFont(fonts.lemonMilkMedium36());
        findItemsSubLabel.setLayoutX(380);
        findItemsSubLabel.setLayoutY(230);

        Image magnifyingGlass = new Image(this.getClass().getResource("magnifying-glass-white.png").toExternalForm());
        ImageView magnifyingGlassView = new ImageView(magnifyingGlass);
        magnifyingGlassView.setFitHeight(100);
        magnifyingGlassView.setFitWidth(100);

        magnifyingGlassView.setLayoutY(70);
        magnifyingGlassView.setLayoutX(1350);

        placeProdGrid(); //adds all items to the grid, then to the scroll pane

        findItemsScroll.setContent(findItemsGrid);
        findItemsScroll.setLayoutX(380);
        findItemsScroll.setLayoutY(320);
        findItemsScroll.setPrefHeight(680);
        findItemsScroll.setPrefWidth(1000);
        findItemsScroll.setId("scrollPane");
        findItemsScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        dash.getChildren().add(findItemsMainLabel);
        dash.getChildren().add(findItemsSubLabel);
//        dash.getChildren().add(magnifyingGlassView);
        dash.getChildren().add(findItemsScroll);


    }

    public void createApprovalsListScreen() throws ParseException {
        dash.getChildren().remove(findItemsMainLabel);
        dash.getChildren().remove(findItemsSubLabel);
        dash.getChildren().remove(myLoansMainLabel);
        dash.getChildren().remove(myLoansSubLabel);

        dash.getChildren().remove(findItemsScroll);
        dash.getChildren().remove(myLoansScroll);

        approvalsButton.setFont(fonts.lemonMilkMedium22());

        approvalsMainLabel.setId("myLoansMainLabel");
        approvalsMainLabel.setFont(fonts.lemonMilkMedium96());
        approvalsMainLabel.setLayoutX(380);
        approvalsMainLabel.setLayoutY(70);

        approvalsSubLabel.setId("findItemsMainLabel");
        approvalsSubLabel.setFont(fonts.lemonMilkMedium36());
        approvalsSubLabel.setLayoutX(380);
        approvalsSubLabel.setLayoutY(230);

        Line line = new Line();
        line.setId("lineMyLoans");
        line.setStartX(382);
        line.setEndX(1460);
        line.setStartY(300);
        line.setEndY(300);
        line.setStrokeWidth(6);

        placeApprovalsGrid(); //adds all items to the grid, then to the scroll pane

        approvalsScroll.setContent(approvalsGrid);
        approvalsScroll.setId("approvalsScroll");
        approvalsScroll.setLayoutX(380);
        approvalsScroll.setLayoutY(320);
        approvalsScroll.setPrefHeight(680);
        approvalsScroll.setPrefWidth(950);
        approvalsScroll.setId("scrollPane");
        approvalsScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//
        dash.getChildren().add(approvalsMainLabel);
        dash.getChildren().add(approvalsSubLabel);
//        dash.getChildren().add(line);
//        dash.getChildren().add(magnifyingGlassView);
        dash.getChildren().add(approvalsScroll);


    }


    public void placeProdGrid() {
        List<List<String>> allProducts = db.getAllProducts();

        int column = 0;
        int row = 1;

        for (int i = 0; i < allProducts.size() ; i++) {

            String productID = allProducts.get(i).get(0);
            String title = allProducts.get(i).get(1);
            String category = allProducts.get(i).get(2);
            String description = allProducts.get(i).get(3);
            String totalStock = allProducts.get(i).get(4);
            String numOnLoan = allProducts.get(i).get(5);
            String imageName = allProducts.get(i).get(6);

            ProductListing thisListing = new ProductListing(dashStage, db, user, productID, title, category, description, totalStock, numOnLoan, imageName);
            Pane listing = thisListing.createListing(dash);


            if (column == 3) {
                column = 0;
                row++;
            }

            findItemsGrid.add(listing, column++, row); //(child,column,row)

            findItemsGrid.setId("gridPane");
            findItemsGrid.setPrefWidth(1000);
            findItemsGrid.setPrefHeight(680);


            GridPane.setMargin(listing, new Insets(30));
        }

    }

    public void placeLoansGrid() throws ParseException {
        List<List<String>> allLoans = db.getAllLoans(String.valueOf(user));

        int column = 0;
        int row = 1;

        for (int i = 0; i < allLoans.size(); i++) {

            int loanID = Integer.parseInt(allLoans.get(i).get(0));
            String productID = allLoans.get(i).get(1);
            String startDate = allLoans.get(i).get(2);
            String dueDate = allLoans.get(i).get(3);

            LoanListing thisListing = new LoanListing(dashStage, db, user, loanID, productID, startDate, dueDate);
            Pane loan = thisListing.createLoanElement(dash);

            if (column == 3) {
                column = 0;
                row++;
            }

            myLoansGrid.add(loan, column++, row); //(child,column,row)

            myLoansGrid.setId("gridPane");
            myLoansGrid.setPrefWidth(1000);
            myLoansGrid.setPrefHeight(680);


            GridPane.setMargin(loan, new Insets(30));
        }
    }

    public void placeApprovalsGrid(){
        headerPane.getChildren().remove(idHeaderLabel);
        headerPane.getChildren().remove(emailHeaderLabel);
        headerPane.getChildren().remove(adminAccessHeaderLabel);
        headerPane.getChildren().remove(actionHeaderLabel);
        approvalsGrid.getChildren().remove(headerPane);


        List<List<String>> allRequests = db.getAllAccountRequests();

        System.out.println(allRequests);

        headerPane.setId("listingHeader");
        headerPane.setPrefWidth(1000);
        headerPane.setMinHeight(30);

        idHeaderLabel.setLayoutY(6);
        idHeaderLabel.setLayoutX(50);
        idHeaderLabel.setFont(fonts.lemonMilkRegular13());

        emailHeaderLabel.setLayoutY(6);
        emailHeaderLabel.setLayoutX(200);
        emailHeaderLabel.setFont(fonts.lemonMilkRegular13());

        adminAccessHeaderLabel.setLayoutY(6);
        adminAccessHeaderLabel.setLayoutX(420);
        adminAccessHeaderLabel.setFont(fonts.lemonMilkRegular13());

        actionHeaderLabel.setLayoutY(6);
        actionHeaderLabel.setLayoutX(700);
        actionHeaderLabel.setFont(fonts.lemonMilkRegular13());

        headerPane.getChildren().add(idHeaderLabel);
        headerPane.getChildren().add(emailHeaderLabel);
        headerPane.getChildren().add(adminAccessHeaderLabel);
        headerPane.getChildren().add(actionHeaderLabel);


        approvalsGrid.add(headerPane, 1, 1);

        int row = 2;

        for (int i = 0; i < allRequests.size(); i++) {

            int approvalID = Integer.parseInt(allRequests.get(i).get(0)); //not needed?
            int requestedID = Integer.parseInt(allRequests.get(i).get(1));
            String requestedEmail = allRequests.get(i).get(2);
            String requestedPassword = allRequests.get(i).get(3);
            int requestedAdminAccess = Integer.parseInt(allRequests.get(i).get(4));

            ApprovalListing thisListing = new ApprovalListing(dashStage, db, this.user, approvalID, requestedID, requestedEmail, requestedPassword, requestedAdminAccess);
            Pane approval = thisListing.createListing(dash);

            approvalsGrid.add(approval, 1, row++); //(child,column,row)

            approvalsGrid.setId("gridPane");
            approvalsGrid.setPrefWidth(950);
            approvalsGrid.setPrefHeight(680);


            GridPane.setMargin(approval, new Insets(10));
        }
    }
}
