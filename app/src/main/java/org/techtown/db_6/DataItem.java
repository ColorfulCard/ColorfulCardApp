package org.techtown.db_6;

import android.widget.Button;


public class DataItem {

    public static class CardData {

        private String balance;
        private String cardName;
        private int viewType;
        private String[] balances;
        private String cardNum;
        private String cardType;
        private String userID;

        protected CardData(String cardName, String balance, int viewType, String[] balances, String cardNum, String cardType, String userID) {
            this.cardName = cardName;
            this.balance = balance;
            this.viewType = viewType;
            this.balances = balances;
            this.cardNum = cardNum;
            this.cardType = cardType;
            this.userID = userID;
        }

        public String getBalance() {
            return balance;
        }
        public String getCardName() {
            return cardName;
        }
        public int getViewType() {
            return viewType;
        }
        public String[] getBalances() {
            return balances;
        }
        public String getCardNum() {
            return cardNum;
        }
        public String getCardType() {
            return cardType;
        }
        public String getUserID() {
            return userID;
        }
    }

    public static class MapData {

        private MemberStore store;
        private int viewType;

        public MapData(MemberStore store, int viewType){
            this.store=store;
            this.viewType=viewType;
        }

        public MemberStore getStore(){return store;}
        public int getViewType(){return  viewType;}
    }


    public static class CommentData{

        private Comment comment;
        private Ccomment ccomment;
        private int viewType;

        public CommentData(Comment comment, int viewType){

            this.comment=comment;
            this.viewType=viewType;
        }

        public CommentData(Ccomment ccomment, int viewType){

            this.ccomment=ccomment;
            this.viewType=viewType;
        }

        public Comment getComment() {return comment;}
        public Ccomment getCcomment() {return ccomment;}
        public int getViewType(){return viewType;}
    }
}