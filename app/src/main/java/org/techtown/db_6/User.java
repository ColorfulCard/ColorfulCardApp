package org.techtown.db_6;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private ArrayList<UserCard> cards = new ArrayList<>();

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
}
