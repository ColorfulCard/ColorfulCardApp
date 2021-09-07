package org.techtown.db_6;

import android.widget.Button;

public class DataItem {

    private String balance;
    private String name;
    private int viewType;
    private Button button;
    private String[] balances;
    private String minus;

    public DataItem(String name, String balance, Button button ,int viewType , String[] balances, String minus) {
        this.name = name;
        this.balance = balance;
        this.button = button;
        this.viewType = viewType;
        this.balances= balances;
        this.minus = minus;

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

    public String[] getBalances(){return balances;}

    public String getMinus() {return minus;}
}