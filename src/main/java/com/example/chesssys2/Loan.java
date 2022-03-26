package com.example.chesssys2;

import java.sql.Connection;

public class Loan {
    int productId;
    String user;
    int loanDurationInDays; // 7, 14 or 28?
    private Database db;

    public Loan (Database parsedDB) {
        Database db = parsedDB;
    }

    public void new7DayLoan () {
        //Add a new loan into the db
    }

    public void new14DayLoan () {
        //Add a new loan into the db
    }

    public void new28DayLoan () {
        //Add a new loan into the db
    }
}
