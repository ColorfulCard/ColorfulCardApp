package org.techtown.db_6;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private ArrayList<UserCard> cards = new ArrayList<>();
    private ArrayList<String[]> allBalances = new ArrayList<>();

    User(String id, String name){
        this.id = id;
        this.name = name;
    }

    public String getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }

    public void addCard(UserCard card)
    {
        cards.add(new UserCard(card.getId(),card.getCardName(), card.getCardNum(), card.isMealCard()));
    }
    public ArrayList<UserCard> getCards()
    {
        return this.cards;
    }
    public void setCardBalances(String balances[], String totalBalance)
    {
        String elements[]= new String[8];
        for(int i=0;i<balances.length;i++)  //7번돌겠지 0~6
        {
            elements[i]= balances[i];
        }
        elements[7]= totalBalance;

         allBalances.add(elements);
    }
    public ArrayList<String[]> getCardBalances()
    {
        return allBalances;
    }

    /*
       * [0] 이월 잔여금액	14,920 원
         [1] 당월 충전금액	0 원
         [2] 당월 사용금액	7,700 원
         [3] 당월 잔여금액	7,220 원
         [4] 금일 한도금액	0 원
         [5] 금일 사용금액	0 원
         [6] 금일 잔여금액	0 원
         [7] 최종 잔여금액    22,140 원 (이월+ 당월)
       * */

}
