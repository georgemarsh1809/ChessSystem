package com.example.chesssys2;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class WriteToFile {

    private String path;
    private boolean appendToFile = false;

    public WriteToFile(String file_path){
        path = file_path;

    }

    public void writeToFile(String title, String cat, String numOnLoan, String numAvailable) throws IOException {
        FileWriter write = new FileWriter(path, appendToFile);

        PrintWriter print_line = new PrintWriter(write);

        print_line.printf( "%s" + "%n" , title);
        print_line.printf( "%s" + "%n" , cat);
        print_line.printf( "%s" + "%n" , numOnLoan);
        print_line.printf( "%s" + "%n" , numAvailable);

        print_line.close();
    }
}
