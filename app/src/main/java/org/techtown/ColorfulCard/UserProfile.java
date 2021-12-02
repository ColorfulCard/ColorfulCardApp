package org.techtown.ColorfulCard;

import com.google.gson.annotations.SerializedName;

//서버에 사용자 프로필 post&get 할 때 쓰이는 클래스 (로그인,회원가입)
public class UserProfile {

    @SerializedName("id")
    private String id;

    @SerializedName("pwd")
    private String pwd;
    // @SerializedName으로 일치시켜 주지않을 경우엔 클래스 변수명이 일치해야함

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    public UserProfile(String id, String pwd, String name, String email) {
    }

    public String getPwd() {
        return pwd;
    }
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {return email;}

}

    