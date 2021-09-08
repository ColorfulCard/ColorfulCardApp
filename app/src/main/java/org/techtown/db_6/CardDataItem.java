package org.techtown.db_6;

import android.widget.Button;

public class CardDataItem {

    private String balance;
    private String cardName;
    private int viewType;
    private Button button;
    private String[] balances;
    private String cardNum;
    private String cardType;
    private String userID;

    public CardDataItem(String cardName, String balance, Button button , int viewType , String[] balances, String cardNum, String cardType, String userID) {
        this.cardName = cardName;
        this.balance = balance;
        this.button = button;
        this.viewType = viewType;
        this.balances= balances;
        this.cardNum=cardNum;
        this.cardType=cardType;
        this.userID=userID;
    }

    public String getBalance() {
        return balance;
    }

    public String getCardName() { return cardName; }

    public int getViewType() {
        return viewType;
    }

    public Button getButton() {return button;}

    public String[] getBalances(){return balances;}

    public String getCardNum(){return cardNum;}

    public String getCardType(){return cardType;}

    public String getUserID(){return userID;}
}