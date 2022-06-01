package com.example.chesssys2;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Loan {
    int productId;
    int user;
    int loanID;
    String status;
    String loanDueDate; // 7, 14 or 28?
    private final Database db;

    public Loan (Database parsedDB, int userID, int loanID, int productID, String loanDueDate) {
        db = parsedDB;
        this.user = userID;
        this.loanID = loanID;
        this.productId = productID;
        this.loanDueDate = loanDueDate;

    }

    public boolean addLoan() {
        Boolean attemptedLoanAdd = db.addNewLoan(this.user, this.productId, this.loanDueDate);

        return attemptedLoanAdd;
    }

    public boolean returnLoan() {
        Boolean attemptedLoanReturn = db.returnAnItem(this.productId, this.loanID);

        return attemptedLoanReturn;
    }

    public String getStatus() throws ParseException {
        String stringVersionOfLoanID = String.valueOf(this.loanID);
        List<String> loanDetails = db.getLoanDetailsFromID(stringVersionOfLoanID);
        String hasBeenReturned = loanDetails.get(5);
        String loanStatus = "unknown";

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        Date today = new Date();
        Date loanDue = formatter.parse(this.loanDueDate);

        if (loanDue.after(today) && hasBeenReturned.equals("0")){
            loanStatus = "active";
        } if (loanDue.before(today) && hasBeenReturned.equals("1")){
            loanStatus = "complete";
        } if (loanDue.before(today) && hasBeenReturned.equals("0")){
            loanStatus = "due";
        } if (loanDue.after(today) && hasBeenReturned.equals("1")){
            loanStatus = "complete";
        }

        return loanStatus;
    }

    public String getLoanReturned(String loanID){
        String hasItemBeenReturned = "no";

        List<String> loanDetails = db.getLoanDetailsFromID(loanID);

        String returned = loanDetails.get(5);

        if (returned.equals("1")){
            hasItemBeenReturned = "yes";
        }

        return hasItemBeenReturned;


    }
}
