package com.example.chesssys2;

import java.sql.Connection;
import java.util.Date;

public class User {
    String user;
    int openLoans;
    String nextDueDate;
    Database db;

    public User (String username, Database parsedDB) {
        user = username;
        db = parsedDB;
    }

    public String[] getCurrentLoans () {
        String[] userLoans = new String[0];

        //Do stuff here

        return userLoans;

    }

    public int getOpenLoans () {
        //Calculate open loans for user here
        //openLoans = calculated value

        return openLoans;
    }

    public String getNextDueDate () {
        //Calculate next due date

        return nextDueDate;
    }

    public void addLoan () {
        Loan loan = new Loan(db);
    }

    public void endLoan () {
        //
    }

}
