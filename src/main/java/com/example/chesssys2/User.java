package com.example.chesssys2;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

public class User {
    Database db;
    int id;
    String accountType = "normal";
    List<List<String>> userLoans;
    int openLoans;
    String nextDueDate;

    public User (Database parsedDB, int username) {
        db = parsedDB;
        id = username;
    }

    public int getUserID(){
        return id;
    }

    public String getAccountType(){
        return accountType;
    }


    public List<List<String>> getAllLoans () {

         userLoans = db.getAllLoans(String.valueOf(id));

        return userLoans;

    }

}
