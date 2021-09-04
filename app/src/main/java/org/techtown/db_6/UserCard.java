package org.techtown.db_6;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

//카드 서버에 post&get 할때 사용되는 클래스
public class UserCard implements Serializable {

    private static final long serialVersionUID = 2L;

    @SerializedName("id")
    private String id;

    @SerializedName("cardName")
    private String cardName;
    // @SerializedName으로 일치시켜 주지않을 경우엔 클래스 변수명이 일치해야함

    @SerializedName("cardNum")
    private String cardNum;

    @SerializedName("mealCard")
    private boolean mealCard;

    public UserCard(String id, String cardName, String cardNum, boolean mealCard) {
        this.id=id;
        this.cardName=cardName;
        this.cardNum=cardNum;
        this.mealCard=mealCard;
    }

    public String getId() {
        return id;
    }

    public String getCardName() {
        return cardName;
    }
    public String getCardNum() {
        return cardNum;
    }
    public Boolean isMealCard(){
        return mealCard;
    }

    @Override
    public String toString() {
        return "PostResult{" +
                "cardNum=" + cardNum +
                ", id=" + id +
                ", cardName='" +cardName + '\'' +
                ", mealCard='" + mealCard + '\'' +
                '}';
    }
   //안먹혀서 안씀
    @Override
    public boolean equals(Object object) {
        UserCard card = (UserCard) object;
        // num만 같으면 true를 리턴.
        if (card.cardNum == this.cardNum) {
            return true;
        }
        return false;
    }


}
