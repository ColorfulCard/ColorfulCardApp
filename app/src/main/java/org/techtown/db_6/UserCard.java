package org.techtown.db_6;

import com.google.gson.annotations.SerializedName;

public class UserCard {

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

}
