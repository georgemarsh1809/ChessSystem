package com.example.chesssys2;

import java.sql.SQLException;

public class AdminUser extends User {
    public AdminUser(Database parsedDB, int username) {
        super(parsedDB, username);
        accountType = "admin";

    }

    public boolean approveAccountCreation(int requestID, String requestedEmail, int requestedUserID, String requestedPassword, int requestedAdminAccess){
        try {
            db.createNewUser(requestedEmail, requestedUserID, requestedPassword, requestedAdminAccess);
            db.removeRequest(requestID);
            return true;

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }

    }

    public boolean declineAccountCreation(int requestID){
        db.removeRequest(requestID);
        return true;
    }
}
