package org.techtown.db_6;

import android.widget.Button;

public class DataItem {

    private String balance;
    private String name;
    private int viewType;
    private Button button;

    public DataItem(String name, String balance, Button button ,int viewType ) {
        this.name = name;
        this.balance = balance;
        this.button = button;
        this.viewType = viewType;

    }



    public String getBalance() {
        return balance;
    }

    public String getName() {
        return name;
    }

    public int getViewType() {
        return viewType;
    }

    public Button getButton() {return button;}
}