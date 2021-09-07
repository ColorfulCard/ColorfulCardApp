package org.techtown.db_6;

import android.widget.Button;

public class CardDataItem {

    private String balance;
    private String name;
    private int viewType;
    private Button button;
    private String[] balances;

    public CardDataItem(String name, String balance, Button button , int viewType , String[] balances) {
        this.name = name;
        this.balance = balance;
        this.button = button;
        this.viewType = viewType;
        this.balances= balances;

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
}