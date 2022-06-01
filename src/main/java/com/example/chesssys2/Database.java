package com.example.chesssys2;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class Database {

    public Connection databaseLink = null;
    private Statement dataStatement = null;
    Connection connectDB = setupDataBase();

    public Database() {
        super();
    }

    boolean didAccountMatch;

    public Connection setupDataBase () {
        String url = "jdbc:mysql://localhost:3306/ChessDB";
        String username = "root";
        String password = "password";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection("jdbc:mysql://localhost:3306/ChessDB", "root", "password");
            dataStatement = databaseLink.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
            System.out.println("Connected");

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return databaseLink;
    }

    public boolean getUserAccountQuery (String username, Connection connectDB, String providedAccount){

        List<String> accounts = new ArrayList<String>();

        String query = "SELECT userID, password FROM accounts WHERE userID=" + username;

        //System.out.println(query);

        //String query = "SELECT * FROM accounts";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(query);
            System.out.println(queryOutput);

            while (queryOutput.next())
            {
                int id = queryOutput.getInt("userID");
                String thisPassword = queryOutput.getString("password");

                String account = id + thisPassword;

                if (account.equals(providedAccount)) {
                    didAccountMatch = true;
                    break;
                }
            }

            statement.close();

        } catch (SQLException e) {
            System.out.println("Exception!!");
            didAccountMatch = false;
        }

        //return accounts;
        return didAccountMatch;
    }

    public List<String> getUserDetailsByID(int userID){
        List<String> accountDetails = new ArrayList<String>();


        String query = "SELECT * FROM accounts WHERE userID=" + userID;


        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(query);
            System.out.println(queryOutput);

            while (queryOutput.next())
            {
                int id = queryOutput.getInt("userID");
                accountDetails.add(String.valueOf(id));

                String password = queryOutput.getString("password");
                accountDetails.add(password);

                String email = queryOutput.getString("email");
                accountDetails.add(email);

                int isAdmin = queryOutput.getInt("isAdmin");
                accountDetails.add(String.valueOf(isAdmin));


            }

            statement.close();

        } catch (SQLException e) {
            System.out.println("Exception!!");
            didAccountMatch = false;
        }

        return accountDetails;

    }

    public void createNewUser(String email, int username, String password, int didUserRequestAdminAccess) throws SQLException {

        int isAdmin = 0;

        if (didUserRequestAdminAccess == 1) {
            isAdmin = 1;
        } if (didUserRequestAdminAccess == 0) {
            isAdmin = 0;
        }
        String query = "INSERT INTO accounts (userID, password, email, isAdmin) VALUES ('" + username + "', '" + password + "', '" + email + "', '" + isAdmin + "')";

        try {
            Statement statement = this.connectDB.createStatement();
            statement.executeUpdate(query);

            System.out.println("user added");

            statement.close();

        } catch (SQLException e) {
            System.out.println("Exception!!");
            e.printStackTrace();

        }
    }

    public void newAccountRequest(int username, String email, String password, int didUserRequestAdminAccess){

        String newAccountRequestQuery = "INSERT INTO accountApprovals (requestedID, requestedEmail, requestedPassword, requestedAdminAccess) VALUES " +
                "('" + username + "', '" + email + "', '" + password + "', '" + didUserRequestAdminAccess + "')";

        try {
            Statement statement = this.connectDB.createStatement();
            statement.executeUpdate(newAccountRequestQuery);

            System.out.println("account pending approval");

            statement.close();

        } catch (SQLException e) {
            System.out.println("Exception!!");
            e.printStackTrace();

        }
    }

    public void removeRequest(int requestID){
        String removeRequestQuery = "DELETE FROM accountApprovals WHERE (approvalID) = " + requestID;

        try {
            Statement statement = this.connectDB.createStatement();
            statement.executeUpdate(removeRequestQuery);

            System.out.println("request removed");

            statement.close();

        } catch (SQLException e) {
            System.out.println("Exception!!");
            e.printStackTrace();

        }
    }

    public List<List<String>> searchForProductsLike(String searchTerm){
        List<List<String>> productsTorReturn = new ArrayList<List<String>>();

        String searchQuery = "SELECT * FROM inventory WHERE (title) LIKE '%" + searchTerm + "%'" + " OR (category) LIKE '%\" + searchTerm + \"%'";

        try {
            Statement statement = this.connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(searchQuery);
            System.out.println(queryOutput);

            while (queryOutput.next())
            {
                List<String> currentProduct = new ArrayList<String>();

                String productId = queryOutput.getString("productID");
                currentProduct.add(productId);

                String title = queryOutput.getString("title");
                currentProduct.add(title);

                String cat = queryOutput.getString("category");
                currentProduct.add(cat);

                String desc = queryOutput.getString("description");
                currentProduct.add(desc);

                String totalStock = queryOutput.getString("totalStock");
                currentProduct.add(totalStock);

                String onLoan = queryOutput.getString("onLoan");
                currentProduct.add(onLoan);

                String imageName = queryOutput.getString("imageName");
                currentProduct.add(imageName);

                productsTorReturn.add(currentProduct);

            }

            statement.close();

            System.out.println("items found");

            statement.close();

        } catch (SQLException e) {
            System.out.println("Nothing returned!!");
            e.printStackTrace();

        }

        return productsTorReturn;
    }

    public List<String> getAllAccounts (Connection connectDB) {
        List<String> accounts = new ArrayList<String>();

        String query = "SELECT * FROM accounts" ;

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(query);

            while (queryOutput.next())
            {
                String id = queryOutput.getString("userID");
                accounts.add(id);
            }

            statement.close();

        }   catch (SQLException e) {
            System.out.println("Exception!!");
        }


        return accounts;
    }

    public List<String> getAccountsThatEqual(String username, Connection connectDB) {
        List<String> accounts = new ArrayList<String>();

        String query = "SELECT * FROM accounts WHERE userID = " + username;


        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(query);

            while (queryOutput.next())
            {
                String id = queryOutput.getString("userID");
                accounts.add(id);
            }

            statement.close();

        }   catch (SQLException e) {
            System.out.println("Nothing in list.");
        }



        return accounts;
    }

    public List<List<String>> getAllProducts () {
        List<List<String>> allProducts = new ArrayList<List<String>>();

        String query = "SELECT * FROM inventory" ;

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(query);

            while (queryOutput.next())
            {
                List<String> currentProduct = new ArrayList<String>();

                String productId = queryOutput.getString("productID");
                currentProduct.add(productId);

                String title = queryOutput.getString("title");
                currentProduct.add(title);

                String cat = queryOutput.getString("category");
                currentProduct.add(cat);

                String desc = queryOutput.getString("description");
                currentProduct.add(desc);

                String totalStock = queryOutput.getString("totalStock");
                currentProduct.add(totalStock);

                String onLoan = queryOutput.getString("onLoan");
                currentProduct.add(onLoan);

                String imageName = queryOutput.getString("imageName");
                currentProduct.add(imageName);

                allProducts.add(currentProduct);
            }

            statement.close();

        }   catch (SQLException e) {
            System.out.println("Exception!!");
        }


        return allProducts;
    }

    public List<List<String>> getAllLoans (String userID) {
        List<List<String>> allLoans = new ArrayList<List<String>>();

        String query = "SELECT * FROM loans WHERE userID = " + userID;

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(query);

            while (queryOutput.next())
            {
                List<String> currentLoan = new ArrayList<String>();

                String loanID = queryOutput.getString("loanID");
                currentLoan.add(loanID);

                String productID = queryOutput.getString("productID");
                currentLoan.add(productID);

                String startDate = queryOutput.getString("startDate");
                currentLoan.add(startDate);

                String dueDate = queryOutput.getString("dueDate");
                currentLoan.add(dueDate);

                allLoans.add(currentLoan);
            }

            statement.close();

        }   catch (SQLException e) {
            System.out.println("Loans Exception!!");
        }

        return allLoans;
    }

    public List<List<String>> getAllAccountRequests () {
        List<List<String>> allRequests = new ArrayList<List<String>>();

        String query = "SELECT * FROM accountApprovals" ;

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(query);

            while (queryOutput.next())
            {
                List<String> currentRequest = new ArrayList<String>();

                String approvalID = queryOutput.getString("approvalID");
                currentRequest.add(approvalID);

                String requestedID = queryOutput.getString("requestedID");
                currentRequest.add(requestedID);

                String requestedEmail = queryOutput.getString("requestedEmail");
                currentRequest.add(requestedEmail);

                String requestedPassword = queryOutput.getString("requestedPassword");
                currentRequest.add(requestedPassword);

                String requestedAdminAccess = queryOutput.getString("requestedAdminAccess");
                currentRequest.add(requestedAdminAccess);

                allRequests.add(currentRequest);
            }

            statement.close();

        }   catch (SQLException e) {
            System.out.println("Exception!!");
        }


        return allRequests;
    }

    public List<String> getProductDetailsFromID(String productID){
        List<String> product = new ArrayList<String>();

        String query = "SELECT * FROM inventory WHERE productID = " + productID ;

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(query);

            while (queryOutput.next())
            {
                String productId = queryOutput.getString("productID");
                product.add(productId);

                String title = queryOutput.getString("title");
                product.add(title);

                String cat = queryOutput.getString("category");
                product.add(cat);

                String desc = queryOutput.getString("description");
                product.add(desc);

                String totalStock = queryOutput.getString("totalStock");
                product.add(totalStock);

                String onLoan = queryOutput.getString("onLoan");
                product.add(onLoan);

                String imageName = queryOutput.getString("imageName");
                product.add(imageName);

            }

            statement.close();

        }   catch (SQLException e) {
            System.out.println("Exception!!");
        }

        return product;
    }

    public boolean addNewLoan(int user, int productID, String dueDate) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();

        String today = formatter.format(c.getTime());

        String query = "INSERT INTO loans (productID, userID, startDate, dueDate) VALUES ('" + productID + "', '" + user + "', '" + today + "', '" + dueDate + "')";

        try {
            Statement statement = this.connectDB.createStatement();
            statement.executeUpdate(query);

            System.out.println("loan added");

            updateStockFieldsFor(productID, "loan");

            statement.close();

            return true;


        } catch (SQLException e) {
            System.out.println("Exception!!");
            e.printStackTrace();

            return false;

        }

    }

    public boolean returnAnItem(int productID, int loanID){
        //delete the loan
        //set available to avialable + 1
        //set onLoan to onLoan -1

        String returnQuery = "UPDATE loans SET returned = 1 WHERE loanID = " + loanID;

        try {
            Statement statement = this.connectDB.createStatement();
            statement.executeUpdate(returnQuery);

            System.out.println("loan added");

            updateStockFieldsFor(productID, "return");

            statement.close();

            return true;

        } catch (SQLException e) {
            System.out.println("Exception!!");
            e.printStackTrace();

            return false;

        }

    }

    public void updateStockFieldsFor(int productId, String actionType) {

        String query = "UPDATE inventory SET available = available - 1, onLoan = onLoan + 1 where productID = " + productId ;

        if (actionType.equals("return")){
            query = "UPDATE inventory SET available = available + 1, onLoan = onLoan - 1 where productID = " + productId;
        } if (actionType.equals("loan")){
            query = query;
        }

        try {
            Statement statement = this.connectDB.createStatement();
            statement.executeUpdate(query);

            System.out.println("fields updated");

            statement.close();

        } catch (SQLException e) {
            System.out.println("Exception!!");
            e.printStackTrace();

        }
    }

    public List<String> getLoanDetailsFromID(String loanID) {
        List<String> loan = new ArrayList<String>();

        String query = "SELECT * FROM loans WHERE loanID = " + loanID ;

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(query);

            while (queryOutput.next())
            {
                String id = queryOutput.getString("loanID");
                loan.add(id);

                String prodID = queryOutput.getString("productID");
                loan.add(prodID);

                String userID = queryOutput.getString("userID");
                loan.add(userID);

                String startDate = queryOutput.getString("startDate");
                loan.add(startDate);

                String dueDate = queryOutput.getString("dueDate");
                loan.add(dueDate);

                String isReturned = queryOutput.getString("returned");
                loan.add(isReturned);
            }

            statement.close();

        }   catch (SQLException e) {
            System.out.println("Exception!!");
        }

        return loan;



    }
}
