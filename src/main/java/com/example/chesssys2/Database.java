package com.example.chesssys2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public PreparedStatement prepareStatement (String query) throws SQLException {
        return connectDB.prepareStatement(query);
    }


    public boolean createNewUser(String email, String username, String password, Boolean didUserRequestAdminAccess, Connection connectDB) throws SQLException {


        int isAdmin;
        if (didUserRequestAdminAccess) {
            isAdmin = 1;
        } else {
            isAdmin = 0;
        }
        String query = "INSERT INTO accounts (userID, password, email, isAdmin) VALUES (' " + username + "', '" + password + " ', '" + email + " ', '" + isAdmin + " ')";

        try {
            Statement statement = this.connectDB.createStatement();
            statement.executeUpdate(query);

            System.out.println("executed");

            statement.close();

        } catch (SQLException e) {
            System.out.println("Exception!!");
            e.printStackTrace();

        }
        return false;
    }


}
