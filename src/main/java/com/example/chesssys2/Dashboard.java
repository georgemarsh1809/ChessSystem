package com.example.chesssys2;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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

    private final Group dash = new Group();
    private final Scene dashboard = new Scene(dash,  1920, 1080, Color.web("6998AB"));
    private final Stage dashStage = new Stage();
    private final Stage logoutStage = new Stage();

    private final FontLoader fonts = new FontLoader();

    private final Database db;
    private Connection connectDB;

    private User user;
    private final int username;

    private final ScrollPane findItemsScroll = new ScrollPane();
    private final ScrollPane myLoansScroll = new ScrollPane();
    private final ScrollPane approvalsScroll = new ScrollPane();

    private final GridPane findItemsGrid = new GridPane();
    private final GridPane myLoansGrid = new GridPane();
    private final GridPane approvalsGrid = new GridPane();

    private final Pane headerPane = new Pane();

    private final Circle ppParent = new Circle();

    private final Line line = new Line();

    private final Label idHeaderLabel = new Label("Requested ID");
    private final Label emailHeaderLabel = new Label("Requested Email");
    private final Label adminAccessHeaderLabel = new Label("Admin Access Request");
    private final Label actionHeaderLabel = new Label("Action");

    private final Button findItemsButton = new Button("  find items");
    private final Button myLoansButton = new Button("  my loans");
    private final Button approvalsButton = new Button("  approvals");
    private final Button logoutButton = new Button("  log out");

    private final Label findItemsMainLabel = new Label("find items");
    private final Label findItemsSubLabel = new Label("search for items to loan...");

    private final Label areYouSureLabel = new Label("Are you sure you want to log out?");
    private final Button yesButton = new Button("yes");
    private final Button noButton = new Button("no");

    private final TextField searchBar = new TextField();
    private final Label searchLabel = new Label("Search: ");
    private List<List<String>> productsToDisplay;
    private final List<List<String>> allRequests;

    private final Label myLoansMainLabel = new Label("My Loans");
    private final Label myLoansSubLabel = new Label("view all of your current open loans...");

    private final Label approvalsMainLabel = new Label("approvals");
    private final Label approvalsSubLabel = new Label("view all open account creation requests..");
    private Scene logoutScene;

    public Dashboard (int username, Database parsedDB) {
        db = parsedDB;
        connectDB = db.setupDataBase();

        user = new User(db, username);
        this.username = user.getUserID();

        allRequests = db.getAllAccountRequests();


    }

    public void createUserDashboard(String accountType, Connection parsedConnectDB){
        String css = this.getClass().getResource("app.css").toExternalForm();
        this.dashboard.getStylesheets().add(css);

        connectDB = parsedConnectDB;

        if (accountType.equals("admin")){
            user = new AdminUser(db, username);
            boolean isAdmin = true;
            createSidebar(isAdmin);

        } if (accountType.equals("normal")){
            user = new User(db, username);
            boolean isAdmin = false;
            createSidebar(isAdmin);

        }

        createProductsListScreen();
        dash.getChildren().add(line);

        dashStage.setTitle("Chess Library System");
        dashStage.setResizable(false);

        dashStage.setScene(dashboard);
        dashStage.show();

    }

    public void createSidebar (boolean isAdmin){
        Boolean userIsAdmin = false;

        if (user.getAccountType().equals("admin")) {
            userIsAdmin = true;
        }

        Pane sideBar = new Pane();
        sideBar.setStyle("-fx-background-color: #1A374D");
        sideBar.setPrefSize(282, 1080);

        Image profileIcon = new Image(this.getClass().getResource("userIcon.png").toExternalForm());
        ImageView profileIconView = new ImageView(profileIcon);

        profileIconView.setPreserveRatio(true);
        profileIconView.setFitHeight(84);
        profileIconView.setLayoutX(99);
        profileIconView.setLayoutY(75);

        line.setId("lineMyLoans");
        line.setStartX(382);
        line.setEndX(1375);
        line.setStartY(300);
        line.setEndY(300);
        line.setStrokeWidth(6);

        if (isAdmin){
            approvalsButton.setId("approvalsButton");
            approvalsButton.setFont(fonts.lemonMilkRegular22());
            approvalsButton.setLayoutX(30);
            approvalsButton.setLayoutY(420);

            if (allRequests.size() > 0){
                approvalsButton.setText("  approvals (" + allRequests.size() + ")" );
            }


            Image approvalsIcon = new Image(this.getClass().getResource("circle-question-solid (1).png").toExternalForm());
            ImageView approvalsIconView = new ImageView(approvalsIcon);

            approvalsIconView.setFitHeight(20);
            approvalsIconView.setFitWidth(20);
            approvalsButton.setGraphic(approvalsIconView);
            approvalsButton.setContentDisplay(ContentDisplay.LEFT);

            approvalsButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    try {
                        createApprovalsListScreen();

                        myLoansButton.setFont(fonts.lemonMilkRegular22());
                        findItemsButton.setFont(fonts.lemonMilkRegular22());

                        dash.getChildren().add(line);
                        line.setEndX(1327);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });

            sideBar.getChildren().add(approvalsButton);

        }

        findItemsButton.setId("findItemsButton");
        findItemsButton.setFont(fonts.lemonMilkRegular22());
        findItemsButton.setLayoutX(30);
        findItemsButton.setLayoutY(300);

        findItemsButton.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                myLoansButton.setFont(fonts.lemonMilkRegular22());
                approvalsButton.setFont(fonts.lemonMilkRegular22());

                line.setEndX(1375);

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
                try {
                    createMyLoansScreen();
                    dash.getChildren().add(line);
                    line.setEndX(1375);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                findItemsButton.setFont(fonts.lemonMilkRegular22());
                approvalsButton.setFont(fonts.lemonMilkRegular22());
            }

        }));

        logoutButton.setId("findItemsButton");
        logoutButton.setFont(fonts.lemonMilkRegular22());
        logoutButton.setLayoutX(30);
        logoutButton.setLayoutY(480);

        Image logoutIcon = new Image(this.getClass().getResource("chess-king.png").toExternalForm());
        ImageView logoutIconView = new ImageView(logoutIcon);

        logoutIconView.setFitHeight(25);
        logoutIconView.setFitWidth(20);
        logoutButton.setGraphic(logoutIconView);
        logoutButton.setContentDisplay(ContentDisplay.LEFT);

        logoutButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                createLogoutScreen();
            }
        });


        sideBar.getChildren().add(findItemsButton);
        sideBar.getChildren().add(myLoansButton);
        sideBar.getChildren().add(logoutButton);
        dash.getChildren().add(sideBar);
        dash.getChildren().add(profileIconView);
    }

    private void createLogoutScreen() {

        Group logoutView = new Group();

        logoutScene = new Scene(logoutView,  350, 150, Color.web("6998AB"));
        String css = this.getClass().getResource("app.css").toExternalForm();
        this.logoutScene.getStylesheets().add(css);

        areYouSureLabel.setLayoutY(30);
        areYouSureLabel.setPrefWidth(350);
        areYouSureLabel.setAlignment(Pos.CENTER);
        areYouSureLabel.setFont(fonts.lemonMilkMedium13());

        yesButton.setLayoutX(205);
        yesButton.setLayoutY(90);
        yesButton.setPrefWidth(80);
        yesButton.setId("loginButton");
        yesButton.setFont(fonts.lemonMilkMedium16());
        yesButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                logoutStage.close();
                dashStage.close();

                LoginScreen newLoginScreen = new LoginScreen(db);
                newLoginScreen.CreateLoginScreen();
            }
        });

        noButton.setLayoutX(65);
        noButton.setLayoutY(90);
        noButton.setPrefWidth(80);
        noButton.setId("registerButton");
        noButton.setFont(fonts.lemonMilkMedium16());
        noButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                logoutStage.close();
            }
        });






        logoutView.getChildren().add(areYouSureLabel);
        logoutView.getChildren().add(yesButton);
        logoutView.getChildren().add(noButton);


        logoutStage.setTitle("Loan An Item");
        logoutStage.setResizable(false);

        logoutStage.setScene(logoutScene);
        logoutStage.show();

    }

    public void createMyLoansScreen() throws ParseException {
        dash.getChildren().remove(findItemsScroll);
        dash.getChildren().remove(approvalsScroll);
        dash.getChildren().remove(findItemsMainLabel);
        dash.getChildren().remove(findItemsSubLabel);
        dash.getChildren().remove(searchBar);
        dash.getChildren().remove(searchLabel);
        dash.getChildren().remove(approvalsMainLabel);
        dash.getChildren().remove(approvalsSubLabel);
        dash.getChildren().remove(line);

        myLoansButton.setFont(fonts.lemonMilkMedium22());

        myLoansMainLabel.setId("myLoansMainLabel");
        myLoansMainLabel.setFont(fonts.lemonMilkMedium96());
        myLoansMainLabel.setLayoutX(380);
        myLoansMainLabel.setLayoutY(70);

        myLoansSubLabel.setId("findItemsMainLabel");
        myLoansSubLabel.setFont(fonts.lemonMilkMedium36());
        myLoansSubLabel.setLayoutX(380);
        myLoansSubLabel.setLayoutY(230);

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
        dash.getChildren().add(myLoansScroll);

    }

    public void createProductsListScreen(){
        productsToDisplay = db.getAllProducts();

        dash.getChildren().remove(findItemsScroll);
        dash.getChildren().remove(findItemsMainLabel);
        dash.getChildren().remove(findItemsSubLabel);
        dash.getChildren().remove(searchBar);
        dash.getChildren().remove(searchLabel);
        dash.getChildren().remove(approvalsScroll);
        dash.getChildren().remove(approvalsMainLabel);
        dash.getChildren().remove(approvalsSubLabel);
        dash.getChildren().remove(myLoansScroll);
        dash.getChildren().remove(myLoansMainLabel);
        dash.getChildren().remove(myLoansSubLabel);
        dash.getChildren().remove(approvalsButton);

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

        searchBar.setId("searchBar");
        searchBar.setFont(fonts.lemonMilkMedium14());
        searchBar.setLayoutY(250);
        searchBar.setLayoutX(1200);
        searchBar.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                String searchTerm = searchBar.getText();

                List<List<String>> searchResults = db.searchForProductsLike(searchTerm);

                if (!searchResults.isEmpty()){
                    findItemsGrid.getChildren().clear();
                    productsToDisplay = searchResults;
                    placeProdGrid(productsToDisplay);
                }
            }
        });

        searchLabel.setFont(fonts.lemonMilkMedium14());
        searchLabel.setLayoutY(255);
        searchLabel.setLayoutX(1120);


        placeProdGrid(productsToDisplay); //adds all items to the grid, then to the scroll pane

        findItemsScroll.setContent(findItemsGrid);
        findItemsScroll.setLayoutX(380);
        findItemsScroll.setLayoutY(320);
        findItemsScroll.setPrefHeight(680);
        findItemsScroll.setPrefWidth(1000);
        findItemsScroll.setId("scrollPane");
        findItemsScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        dash.getChildren().add(findItemsMainLabel);
        dash.getChildren().add(findItemsSubLabel);
        dash.getChildren().add(findItemsScroll);
        dash.getChildren().add(searchBar);
        dash.getChildren().add(searchLabel);


    }

    public void createApprovalsListScreen() throws ParseException {
        dash.getChildren().remove(myLoansScroll);
        dash.getChildren().remove(myLoansMainLabel);
        dash.getChildren().remove(myLoansSubLabel);
        dash.getChildren().remove(findItemsScroll);
        dash.getChildren().remove(findItemsMainLabel);
        dash.getChildren().remove(findItemsSubLabel);
        dash.getChildren().remove(searchBar);
        dash.getChildren().remove(searchLabel);
        dash.getChildren().remove(line);

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

        placeApprovalsGrid(); //adds all items to the grid, then to the scroll pane

        approvalsScroll.setContent(approvalsGrid);
        approvalsScroll.setId("approvalsScroll");
        approvalsScroll.setLayoutX(380);
        approvalsScroll.setLayoutY(320);
        approvalsScroll.setPrefHeight(680);
        approvalsScroll.setPrefWidth(950);
        approvalsScroll.setId("scrollPane");
        approvalsScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        dash.getChildren().add(approvalsMainLabel);
        dash.getChildren().add(approvalsSubLabel);
        dash.getChildren().add(approvalsScroll);
    }


    public void placeProdGrid(List<List<String>> productsToPlace) {

        int column = 0;
        int row = 1;

        for (int i = 0; i < productsToPlace.size() ; i++) {

            String productID = productsToPlace.get(i).get(0);
            String title = productsToPlace.get(i).get(1);
            String category = productsToPlace.get(i).get(2);
            String description = productsToPlace.get(i).get(3);
            String totalStock = productsToPlace.get(i).get(4);
            String numOnLoan = productsToPlace.get(i).get(5);
            String imageName = productsToPlace.get(i).get(6);

            ProductListing thisListing = new ProductListing(dashStage, db, this.username, productID, title, category, description, totalStock, numOnLoan, imageName);
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
        List<List<String>> allLoans = db.getAllLoans(String.valueOf(username));

        int column = 0;
        int row = 1;

        for (int i = 0; i < allLoans.size(); i++) {

            int loanID = Integer.parseInt(allLoans.get(i).get(0));
            String productID = allLoans.get(i).get(1);
            String startDate = allLoans.get(i).get(2);
            String dueDate = allLoans.get(i).get(3);

            LoanListing thisListing = new LoanListing(dashStage, db, this.username, loanID, productID, startDate, dueDate);
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

            ApprovalListing thisListing = new ApprovalListing(dashStage, db, this.username, approvalID, requestedID, requestedEmail, requestedPassword, requestedAdminAccess);
            Pane approval = thisListing.createListing(dash);

            approvalsGrid.add(approval, 1, row++); //(child,column,row)

            approvalsGrid.setId("gridPane");
            approvalsGrid.setPrefWidth(950);
            approvalsGrid.setPrefHeight(680);


            GridPane.setMargin(approval, new Insets(10));
        }
    }
}
